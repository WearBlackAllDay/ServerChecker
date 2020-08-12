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
    public static Integer getPort(JTextField in){
        int port;
        try{
            port = Integer.parseInt(in.getText());
        }
        catch (NumberFormatException e){
            port = 25565;
        }
        return port;
    }
}
