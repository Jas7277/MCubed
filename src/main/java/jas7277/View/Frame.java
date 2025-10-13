package jas7277.View;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;
import jas7277.Model.ServerProcesses;

public class Frame implements Runnable {
    @Override
    public void run() {
        JFrame frame = new JFrame("MCubed");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                onExit();
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        frame.setResizable(false);

        // Create New Server Dialog
        JTextField nameField = new JTextField();
        JTextField dirField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Server Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Server Directory"));
        panel.add(dirField);

        // Server tabs
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        addServerTab(tabbedPane, "new_server", new MainPanel());

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    private void onExit() {
        for (Process serverProcess : ServerProcesses.processes) {
            serverProcess.destroy();
        }
        System.err.println("Exit");
        System.exit(0);
    }

    private void addServerTab(JTabbedPane tabbedPane, String serverName, Component comp) {
        tabbedPane.add(comp);

        JPanel tabHeader = new JPanel(new BorderLayout());
        tabHeader.setOpaque(false);

        JLabel nameLabel = new JLabel(serverName);
        Font emojiFont = new Font("Segoe UI Emoji", Font.PLAIN, 14);
        JButton renameButton = new JButton("✏️");
        renameButton.setFont(emojiFont);
        renameButton.setMargin(new Insets(4, 2, 0, -25));
        renameButton.setBorderPainted(false);
        renameButton.setFocusPainted(false);
        renameButton.setContentAreaFilled(false);
        renameButton.setToolTipText("Rename Server");

        renameButton.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog(tabbedPane, "Rename server:", serverName);
            if (newName != null && !newName.trim().isEmpty()) {
                nameLabel.setText(newName.trim());
            }
        });

        tabHeader.add(nameLabel, BorderLayout.CENTER);
        tabHeader.add(renameButton, BorderLayout.EAST);

        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, tabHeader);

        tabbedPane.addTab("+", null);
        tabbedPane.addChangeListener(e -> {
            int index = tabbedPane.getSelectedIndex();
            if (index == tabbedPane.getTabCount() - 1) {
                tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 2);
                String name = JOptionPane.showInputDialog(tabbedPane, "Enter new server name:");
                if (name != null && !name.trim().isEmpty()) {
                    tabbedPane.insertTab(name.trim(), null, new MainPanel(), null, tabbedPane.getTabCount() - 1);
                    tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 2);
                }
                else {
                    tabbedPane.setSelectedIndex(0);
                }
            }
        });
    }
}
