package gui;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Domain {

    public static boolean isDomain(String ip){
        return ip.matches("[a-zA-Z]+");
    }

    public static String getIP(String domain) throws UnknownHostException {
        return InetAddress.getByName(domain).toString();
    }
    /*public static InetSocketAddress splitIP(String ip) {
        String[] split = ip.split(":",2);
        return new InetSocketAddress(split[0], Integer.parseInt(split[1]));
    } */
}
