package jas7277.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import jas7277.Core.EventController;
import jas7277.Utilities.ServerInfo;

public class Frame implements Runnable {
    private JTextArea consoleArea;
    private EventController controller;

    @Override
    public void run() {
        controller = new EventController();

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

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu settingsMenu = new JMenu("Settings");
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(fileMenu);
        menuBar.add(settingsMenu);
        menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);

        // Left Panel (Controls & Configs)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        // Server Controls
        JPanel controlsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        controlsPanel.setBorder(BorderFactory.createTitledBorder("Server Controls"));

        JButton startButton = new JButton("Start Server");
        JButton stopButton = new JButton("Stop Server");
        JButton restartButton = new JButton("Restart Server");

        restartButton.addActionListener(_ -> controller.RestartServer(consoleArea));

        controlsPanel.add(startButton);
        controlsPanel.add(stopButton);
        controlsPanel.add(restartButton);

        // Server Settings
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Server Settings"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        settingsPanel.add(new JLabel("Server Type:"));

        // TODO: implement multiple types of servers
        gbc.gridx = 1;
        JComboBox<String> serverType = new JComboBox<>(new String[]{"Vanilla"});
        settingsPanel.add(serverType, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        settingsPanel.add(new JLabel("Version:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> versionDropdown = new JComboBox<>(Objects.requireNonNull(controller.GetServerVersions()));
        settingsPanel.add(versionDropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        settingsPanel.add(new JLabel("RAM (MB):"), gbc);

        // TODO: scale min and max values to match available RAM
        long maxMemoryBytes = Runtime.getRuntime().maxMemory();
        int maxMemoryMB = (int) (maxMemoryBytes / (1024 * 1024));

        int minRam = 512;
        int stepRam = 512;
        int defaultRAM = Math.min(2048, maxMemoryMB);
        gbc.gridx = 1;
        JSpinner RAM = new JSpinner(new SpinnerNumberModel(defaultRAM, minRam, maxMemoryMB, stepRam));
        settingsPanel.add(RAM, gbc);

        RAM.setToolTipText("Max RAM based on RAM available for JVM");

        gbc.gridx = 0;
        gbc.gridy = 3;
        JCheckBox autoEulaCheck = new JCheckBox("Automatically accept EULA");
        autoEulaCheck.setToolTipText("If checked, the EULA will be accepted automatically before starting the server.");

        settingsPanel.add(autoEulaCheck, gbc);

        // File Management
        JPanel filePanel = new JPanel(new GridLayout(0, 2, 5, 5));
        filePanel.setBorder(BorderFactory.createTitledBorder("File Management"));

        // Progress Bar
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        JButton deleteButton = new JButton("Delete Server");
        deleteButton.setToolTipText("Permanently delete the entire server folder");

        deleteButton.setForeground(Color.RED);

        deleteButton.addActionListener(_ -> controller.DeleteServer(frame, (String) serverType.getSelectedItem(), (String) versionDropdown.getSelectedItem()));

        deleteButton.setEnabled(false);

        JButton downloadButton = new JButton("Download Server JAR");

        downloadButton.addActionListener(_ -> {
            ServerInfo server = controller.SelectedServer((String) versionDropdown.getSelectedItem());

            startButton.setEnabled(false);
            stopButton.setEnabled(false);
            restartButton.setEnabled(false);
            downloadButton.setEnabled(false);
            progressBar.setVisible(true);
            progressBar.setValue(0);
            progressBar.setString("Downloading...");

            if (server != null){
                controller.DownloadButtonClicked(server, progressBar, new JButton[]{downloadButton, startButton, stopButton, restartButton});
            }
        });

        File baseDir = new File("servers/");

        new javax.swing.Timer(1000, _ -> {
            String type = (String) serverType.getSelectedItem();
            String version = (String) versionDropdown.getSelectedItem();

            File jarFile = new File(baseDir, type + "/" + version + "/server.jar");

            boolean existsAndValid = jarFile.exists() && jarFile.length() > 0;

            downloadButton.setEnabled(!existsAndValid);
            deleteButton.setEnabled(existsAndValid);
        }).start();

        JButton openFileExplorer = new JButton("Open Server Folder");
        openFileExplorer.addActionListener(_ -> controller.OpenFileExplorer("servers/" + serverType.getSelectedItem() + "/" + versionDropdown.getSelectedItem()));

        filePanel.add(downloadButton);
        filePanel.add(openFileExplorer);
        filePanel.add(Box.createVerticalStrut(5));
        filePanel.add(deleteButton);
        filePanel.add(progressBar);

        leftPanel.add(controlsPanel);
        leftPanel.add(settingsPanel);
        leftPanel.add(filePanel);

        // Right Panel (Console)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setMinimumSize(new Dimension(400, 400));
        rightPanel.setPreferredSize(new Dimension(500, 400));
        consoleArea = new JTextArea();
        consoleArea.setEditable(false);
        JScrollPane consoleScroll = new JScrollPane(consoleArea);
        rightPanel.add(consoleScroll, BorderLayout.CENTER);


        stopButton.addActionListener(_ -> controller.StopServer(consoleArea));

        JPanel commandPanel = new JPanel(new BorderLayout());
        JTextField commandField = new JTextField();
        JButton sendButton = new JButton("Send");
        commandPanel.add(commandField, BorderLayout.CENTER);
        commandPanel.add(sendButton, BorderLayout.EAST);
        rightPanel.add(commandPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(_ -> controller.SendButtonClicked(consoleArea, commandField));

        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300);

        // Status Bar
        JLabel statusBar = new JLabel("Server stopped");
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        startButton.addActionListener(_ -> controller.StartServer(new JButton[]{startButton, downloadButton, deleteButton}, (int) RAM.getValue(), progressBar, (String) serverType.getSelectedItem(), (String) versionDropdown.getSelectedItem(), autoEulaCheck, consoleArea));

        frame.add(splitPane, BorderLayout.CENTER);
        frame.add(statusBar, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    private void onExit() {
        controller.StopServer(consoleArea);
        System.err.println("Exit");
        System.exit(0);
    }
}
