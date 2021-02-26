import com.formdev.flatlaf.FlatDarculaLaf;
import handshake.ResponsePacket;
import handshake.ServerScraper;
import swing.components.CustomPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerChecker extends JFrame {

    private final CustomPanel main;
    private final JLabel errorLabel = new JLabel();

    public static void main(String[] args) {
        FlatDarculaLaf.install();
        new ServerChecker();
    }

    public ServerChecker() {
        this.main = new CustomPanel(new GridLayout(2, 2)).
                addTextField("enter IP", 250, 30, "ip").
                addButton("check", 50, 30, e -> this.processIP()).
                addTextField("", 250, 30, "output").
                addComponent(() -> this.errorLabel);
        this.errorLabel.setForeground(Color.RED);
        this.main.getTextField("output").setEditable(false);

        this.setContentPane(this.main);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("ServerChecker");
        this.pack();
    }

    private void processIP() {
        String ip = this.main.getText("ip").trim();
        if (ip.matches("[a-zA-Z]+")) {
            try {
                ip = InetAddress.getByName(ip).toString();
            } catch (UnknownHostException e) {
                this.main.getTextField("output").setText("");
                this.errorLabel.setText("unrecognized Domain");
            }
        }
        try {
            ResponsePacket output = ServerScraper.fetch(ip);
            this.main.getTextField("output").setText(output.version.name);
            this.errorLabel.setText("");
        } catch (IOException e) {
            e.printStackTrace();
            this.main.getTextField("output").setText("");
            this.errorLabel.setText("invalid IP");
        }
    }
}