package handshake;

import com.google.gson.Gson;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.regex.Pattern;

public class ServerScraper {

    private static final Gson GSON = new Gson();
    private static DirContext DIR_CONTEXT;

    static {
        Hashtable<String, String> TABLE = new Hashtable<>();
        TABLE.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        TABLE.put("java.naming.provider.url", "dns:");
        TABLE.put("com.sun.jndi.dns.timeout.retries", "1");

        try {
            DIR_CONTEXT = new InitialDirContext(TABLE);
        } catch(NamingException e) {
            e.printStackTrace();
        }
    }

    public static ResponsePacket fetch(String ip) throws IOException {
        InetSocketAddress address = resolveAddress(ip);

        Socket socket = new Socket();
        socket.connect(address, 2000);

        DataOutputStream inStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream outStream = new DataInputStream(socket.getInputStream());
        ByteArrayOutputStream handshakeStream = new ByteArrayOutputStream();
        DataOutputStream handshake = new DataOutputStream(handshakeStream);
        handshake.writeByte(0);
        writeInt(handshake, 4);
        writeInt(handshake, address.getHostString().length());
        handshake.writeBytes(address.getHostString());
        handshake.writeShort(address.getPort());

        writeInt(handshake, 1);
        writeInt(inStream, handshakeStream.size());

        inStream.write(handshakeStream.toByteArray());
        inStream.writeByte(1);
        inStream.writeByte(0);

        readInt(outStream);
        readInt(outStream);

        byte[] bytes = new byte[readInt(outStream)];
        outStream.readFully(bytes);
        String json = new String(bytes);

        inStream.writeByte(9);
        inStream.writeByte(1);
        inStream.writeLong(System.currentTimeMillis());

        readInt(outStream);
        readInt(outStream);

        long pingTime = outStream.readLong();

        inStream.close();
        outStream.close();
        socket.close();

        ResponsePacket response = GSON.fromJson(json, ResponsePacket.class);
        response.rawJson = json;
        response.ping = System.currentTimeMillis() - pingTime;
        return response;
    }

    private static InetSocketAddress resolveAddress(String ip) {
        ip = ip.trim();
        if (ip.matches("[a-zA-Z]+")) {
            try {
                ip = InetAddress.getByName(ip).toString();
            } catch (UnknownHostException e) {
                System.out.println("Domain not recognized");
            }
        }
        String[] s = ip.split(":");
        if(s.length > 1) return new InetSocketAddress(s[0], Integer.parseInt(s[1]));

        try {
            Attribute attribute = DIR_CONTEXT.getAttributes("_minecraft._tcp." + ip, new String[] {"SRV"}).get("srv");

            if(attribute != null) {
                String[] data = attribute.get().toString().split(Pattern.quote(" "), 4);
                return new InetSocketAddress(data[3], Integer.parseInt(data[2]));
            }
        } catch(NamingException | NumberFormatException ignored) {
        }
        return new InetSocketAddress(ip, 25565);
    }

    private static int readInt(DataInputStream packet) throws IOException {
        int i, j;

        for(i = 0, j = 0; ;) {
            int k = packet.readByte();
            i |= (k & 0x7F) << j++ * 7;
            if(j > 5)throw new RuntimeException("int too big");
            if((k & 0x80) != 128)break;
        }

        return i;
    }

    private static void writeInt(DataOutputStream packet, int value) throws IOException {
        while(true) {
            if ((value & 0xFFFFFF80) == 0) {
                packet.writeByte(value);
                return;
            }

            packet.writeByte(value & 0x7F | 0x80);
            value >>>= 7;
        }
    }
}
