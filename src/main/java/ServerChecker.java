import com.formdev.flatlaf.FlatDarculaLaf;
import handshake.ResponsePacket;
import handshake.ServerScraper;
import wearblackallday.swing.components.CustomPanel;

import javax.swing.*;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.IOException;

public class ServerChecker extends JFrame {

    public static void main(String[] args) {
        FlatDarculaLaf.install();
        new ServerChecker();
    }

    public ServerChecker() {
        JTextField output = new JTextField();
        JLabel errorLabel = new JLabel();

        this.setContentPane(new CustomPanel(new GridLayout(2, 2), 250, 30).
                addTextField("enter IP", "ip").
                addButton("check", (panel, button, event) -> {
                    try {
                        ResponsePacket packet = ServerScraper.fetch(panel.getText("ip"));
                        output.setText(packet.version.name);
                        errorLabel.setText("");
                    } catch (IOException e) {
                        e.printStackTrace();
                        output.setText("");
                        errorLabel.setText("invalid IP");
                    }
                }).
                addComponent(() -> output, (panel, text) -> text.setEditable(false)).
                addComponent(() -> errorLabel, (panel, label) -> label.setForeground(Color.RED)));
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("ServerChecker");
        this.pack();
        this.setLocationRelativeTo(null);
    }
}