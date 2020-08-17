package gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import handshake.ResponsePacket;
import handshake.ServerScraper;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.UnknownHostException;

public class GUI {
    public GUI() {
        initComponents();
    }

    private void checkButtonPressed()  {
        String ip = inputIP.getText();

        if(Domain.isDomain(ip)){
            try {
                ip = Domain.getIP(ip);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                errorLabel.setText("unrecognized Domain");
                verText.setText("");
                throw new RuntimeException();
            }
        }
        try {
            ResponsePacket output = ServerScraper.fetch(ip);
            verText.setText(output.version.name);
            errorLabel.setText("");
        } catch (IOException e) {
            e.printStackTrace();
            verText.setText("");
            errorLabel.setText("invalid IP");
        }
    }

    private void initComponents() {
        //GEN-BEGIN:initComponents
        mainFrame = new JFrame();
        inputIP = new JTextField();
        checkButton = new JButton();
        verLabel = new JLabel();
        verText = new JTextField();
        errorLabel = new JLabel();

        //======== mainFrame ========
        {
            Container mainFrameContentPane = mainFrame.getContentPane();
            mainFrameContentPane.setLayout(new GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));

            //---- inputIP ----
            inputIP.setMinimumSize(new Dimension(250, 30));
            PromptSupport.setPrompt("enter IP",inputIP);

            mainFrameContentPane.add(inputIP, new GridConstraints(0, 0, 1, 3,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));

            //---- checkButton ----
            checkButton.setText("check");
            checkButton.setMinimumSize(new Dimension(50, 30));
            checkButton.addActionListener(e -> checkButtonPressed());
            mainFrameContentPane.add(checkButton, new GridConstraints(0, 3, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));

            //---- verLabel ----
            verLabel.setText("version:");
            verLabel.setMinimumSize(new Dimension(40, 16));
            mainFrameContentPane.add(verLabel, new GridConstraints(1, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));

            //---- verText ----
            verText.setMinimumSize(new Dimension(150, 30));
            verText.setEditable(false);
            mainFrameContentPane.add(verText, new GridConstraints(1, 1, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));

            //---- errorLabel ----
            errorLabel.setForeground(Color.red);
            mainFrameContentPane.add(errorLabel, new GridConstraints(1, 2, 1, 2,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));
            mainFrame.setSize(445, 340);
            mainFrame.setLocationRelativeTo(mainFrame.getOwner());
        }
        //GEN-END:initComponents
    }

    //GEN-BEGIN:variables
    public JFrame mainFrame;
    public JTextField inputIP;
    public JButton checkButton;
    public JLabel verLabel;
    public JTextField verText;
    public JLabel errorLabel;
    //GEN-END:variables
}
