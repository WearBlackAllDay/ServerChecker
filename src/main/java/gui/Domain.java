package gui;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Domain {

    public static boolean isDomain(String ip){
        return ip.matches("[a-zA-Z]+");
    }

    public static String getIP(String domain) throws UnknownHostException {
        return InetAddress.getByName(domain).toString();
    }

}
