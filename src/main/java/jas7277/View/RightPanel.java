package jas7277.View;

import javax.swing.*;
import java.awt.*;

public class RightPanel extends JPanel {
    public static JTextArea consoleArea = new JTextArea();

    public RightPanel() {
        super(new BorderLayout());
        this.setMinimumSize(new Dimension(400, 400));
        this.setPreferredSize(new Dimension(500, 400));
        consoleArea.setEditable(false);
        JScrollPane consoleScroll = new JScrollPane(consoleArea);
        this.add(consoleScroll, BorderLayout.CENTER);

        JPanel commandPanel = new JPanel(new BorderLayout());
        JTextField commandField = new JTextField();
        JButton sendButton = new JButton("Send");
        commandPanel.add(commandField, BorderLayout.CENTER);
        commandPanel.add(sendButton, BorderLayout.EAST);
        this.add(commandPanel, BorderLayout.SOUTH);
    }
}
