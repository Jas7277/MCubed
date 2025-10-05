package jas7277.View;

import jas7277.Controller.LeftPanelController;
import jas7277.Model.ServerInfo;
import jas7277.Model.ServerTypes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Objects;

public class LeftPanel extends JPanel {
    LeftPanelController controller;
    boolean serverRunning;
    public LeftPanel() {
        super();
        controller = new LeftPanelController();
        serverRunning = false;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Server Controls
        JPanel controlsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        controlsPanel.setBorder(BorderFactory.createTitledBorder("Server Controls"));

        JButton startButton = new JButton("Start Server");
        JButton stopButton = new JButton("Stop Server");
        stopButton.setEnabled(false);
        JButton restartButton = new JButton("Restart Server");
        restartButton.setEnabled(false);

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

        long maxMemoryBytes = Runtime.getRuntime().maxMemory();
        int maxMemoryMB = (int) (maxMemoryBytes / (1024 * 1024));

        int minRam = 512;
        int stepRam = 512;
        int defaultRAM = Math.min(2048, maxMemoryMB);
        gbc.gridx = 1;
        JSpinner RAM = new JSpinner(new SpinnerNumberModel(defaultRAM, minRam, maxMemoryMB, stepRam));
        settingsPanel.add(RAM, gbc);

        RAM.setToolTipText("Max RAM based on RAM available for JVM");

        // File Management
        JPanel filePanel = new JPanel(new GridLayout(0, 2, 5, 5));
        filePanel.setBorder(BorderFactory.createTitledBorder("File Management"));

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        JButton deleteButton = new JButton("Delete Server");
        deleteButton.setToolTipText("Permanently delete the entire server folder.");

        deleteButton.setForeground(Color.RED);
        deleteButton.setEnabled(false);

        JButton downloadButton = new JButton("Download Server JAR");

        JButton openFileExplorer = new JButton("Open Server Folder");

        filePanel.add(downloadButton);
        filePanel.add(openFileExplorer);
        filePanel.add(Box.createVerticalStrut(5));
        filePanel.add(deleteButton);
        filePanel.add(progressBar);

        this.add(controlsPanel);
        this.add(settingsPanel);
        this.add(filePanel);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverRunning = true;
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                restartButton.setEnabled(true);
                serverType.setEnabled(false);
                versionDropdown.setEnabled(false);
                RAM.setEnabled(false);
                controller.StartServer(
                        Objects.requireNonNull(serverType.getSelectedItem()).toString(),
                        Objects.requireNonNull(versionDropdown.getSelectedItem()).toString(),
                        (int) RAM.getValue());
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverRunning = false;
                stopButton.setEnabled(false);
                restartButton.setEnabled(false);
                startButton.setEnabled(true);
                serverType.setEnabled(true);
                versionDropdown.setEnabled(true);
                RAM.setEnabled(true);
                controller.StopServer();
            }
        });

        restartButton.addActionListener(_ -> controller.RestartServer());

        File baseDir = new File("servers/");

        new javax.swing.Timer(1000, _ -> {
            downloadButton.setEnabled(false);
            deleteButton.setEnabled(false);
            if (!serverRunning) {
                String type = (String) serverType.getSelectedItem();
                String version = (String) versionDropdown.getSelectedItem();

                File jarFile = new File(baseDir, type + "/" + version + "/server.jar");

                boolean existsAndValid = jarFile.exists() && jarFile.length() > 0;

                startButton.setEnabled(existsAndValid);
                downloadButton.setEnabled(!existsAndValid);
                deleteButton.setEnabled(existsAndValid);
                openFileExplorer.setEnabled(existsAndValid);

                if (openFileExplorer.getActionListeners().length == 0) {
                    openFileExplorer.addActionListener(_ -> controller.OpenFileExplorer(baseDir + "/" + type + "/" + version));
                }

                if (deleteButton.getActionListeners().length == 0) {
                    deleteButton.addActionListener(_ -> controller.DeleteServer(this.getParent(), baseDir + "/" + type + "/" + version));
                }

                if (downloadButton.getActionListeners().length == 0) {
                    downloadButton.addActionListener(_ -> controller.DownloadButtonClicked(new ServerInfo(ServerTypes.VANILLA, version, "")));
                }
            }
        }).start();
    }
}
