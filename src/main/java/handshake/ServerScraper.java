package handshake;

import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerScraper {

    private static final Gson GSON = new Gson();

    /*public static void main(String[] args) throws IOException {
        handshake.ResponsePacket packet = handshake.ServerScraper.fetch("51.161.38.45");
        System.out.println(packet.version.name);
    }

    public static ResponsePacket fetch(String ip) throws IOException {
        return fetch(ip, 25565);
    }*/

    public static ResponsePacket fetch(String ip, int host) throws IOException {
        InetSocketAddress address = new InetSocketAddress(ip, host);

        Socket socket = new Socket();
        socket.connect(address, 7000);

        DataOutputStream inStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream outStream = new DataInputStream(socket.getInputStream());
        ByteArrayOutputStream handshakeStream = new ByteArrayOutputStream();
        DataOutputStream handshake = new DataOutputStream(handshakeStream);
        handshake.writeByte(0x00);
        writeInt(handshake, 4);
        writeInt(handshake, address.getHostString().length());
        handshake.writeBytes(address.getHostString());
        handshake.writeShort(address.getPort());

        writeInt(handshake, 1);
        writeInt(inStream, handshakeStream.size());

        inStream.write(handshakeStream.toByteArray());
        inStream.writeByte(0x01);
        inStream.writeByte(0x00);

        readInt(outStream);
        readInt(outStream);

        byte[] bytes = new byte[readInt(outStream)];
        outStream.readFully(bytes);
        String json = new String(bytes);

        inStream.writeByte(0x09);
        inStream.writeByte(0x01);
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

    public static int readInt(DataInputStream packet) throws IOException {
        int i, j;

        for(i = 0, j = 0; ;) {
            int k = packet.readByte();
            i |= (k & 0x7F) << j++ * 7;
            if(j > 5)throw new RuntimeException("int too big");
            if((k & 0x80) != 128)break;
        }

        return i;
    }

    public static void writeInt(DataOutputStream packet, int value) throws IOException {
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
