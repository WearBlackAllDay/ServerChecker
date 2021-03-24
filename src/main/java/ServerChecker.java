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

        output.setEditable(false);
        errorLabel.setForeground(Color.RED);

        CustomPanel customPanel = new CustomPanel(new GridLayout(2, 2), 250, 30).
                addTextField("enter IP", "ip").
                addButton("check", (button, event) -> {
                    try {
                        ResponsePacket packet = ServerScraper.fetch(((CustomPanel)button.getParent()).getText("ip"));
                        output.setText(packet.version.name);
                        errorLabel.setText("");
                    } catch (IOException e) {
                        e.printStackTrace();
                        output.setText("");
                        errorLabel.setText("invalid IP");
                    }
                }).
                addComponent(() -> output).
                addComponent(() -> errorLabel);
        this.setContentPane(customPanel);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("ServerChecker");
        this.pack();
        this.setLocationRelativeTo(null);
    }
}