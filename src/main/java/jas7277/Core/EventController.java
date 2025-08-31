package jas7277.Core;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import jas7277.Utilities.ConsoleHelper;
import jas7277.Utilities.FileManager;
import jas7277.Utilities.ServerInfo;

public class EventController {
    //region Variables
    private final FileManager fileManager;
    private  ArrayList<ServerInfo> servers;
    private Process serverProcess;
    private boolean shouldRestart;
    private boolean shouldStop;
    //endregion

    //region Public Methods
    public EventController() {
        fileManager = new FileManager();
    }

    public void DownloadButtonClicked(ServerInfo server, JProgressBar progressBar, JButton[] actionButtons) {
        new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() {
                Path path = Paths.get("servers/Vanilla/" + server.id());

                try {
                    Files.createDirectories(path);

                    String name = path + "/server.jar";

                    File file = new File(name);

                    if (!file.exists()) {
                        fileManager.DownloadFile(server.downloadUrl(), name, this::publish);
                    }
                } catch (IOException e) {
                    System.err.println("Error writing the new server JAR file!");
                }
                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                progressBar.setValue(chunks.getLast());
            }

            @Override
            protected void done() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {

                }

                progressBar.setValue(100);
                progressBar.setString("Download complete");
                progressBar.setStringPainted(true);

                for (JButton button : actionButtons) {
                    button.setEnabled(true);
                }
                progressBar.setVisible(false);
            }
        }.execute();
    }

    public String[] GetServerVersions() {
        String[] versions = fileManager.ReadServerVersions();

        if (versions == null) {
            try {
                servers = fileManager.GetServersFromFile("servers.json");
                versions = new String[servers.toArray().length];

                int count = 0;
                for (ServerInfo server : servers) {
                    versions[count] = server.id();
                    count++;
                }

                fileManager.SaveServerVersions(versions);
            } catch (IOException e) {
                System.err.println("Error retrieving the server info! Try resetting the server manifest file");
                return null;
            }
        }

        return versions;
    }

    public ServerInfo SelectedServer(String id) {
        for (ServerInfo server : servers) {
            if (server.id().equals(id)) {
                return server;
            }
        }
        return null;
    }

    public void StartServer(JButton[] actionButtons, int RAM, JProgressBar progressBar, String serverType, String serverVersion, JCheckBox autoEulaCheck, JTextArea consoleArea) {
        shouldRestart = false;
        shouldStop = false;

        for (JButton button : actionButtons) {
            button.setEnabled(false);
        }

        progressBar.setIndeterminate(true);
        progressBar.setString("Starting server...");

        String serverDir = "servers/" + serverType + "/" + serverVersion;
        if (autoEulaCheck.isSelected()) {
            File eulaFile = new File(serverDir, "eula.txt");
            try (FileWriter writer = new FileWriter(eulaFile)) {
                writer.write("eula=true\n");
                writer.flush();
                System.out.println("EULA accepted automatically");
            } catch (IOException e) {
                System.err.println("Failed to write eula.txt: " + e.getMessage());
                return;
            }
        }

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    ProcessBuilder builder = new ProcessBuilder(
                            "java", "-Xmx" + RAM + "M", "-Xms" + RAM + "M", "-jar", "server.jar", "nogui"
                    );
                    builder.directory(new File(serverDir));
                    builder.redirectErrorStream(true);
                    serverProcess = builder.start();

                    new Thread(() -> {
                        ConsoleHelper helper = new ConsoleHelper(consoleArea, serverProcess);
                        helper.Clear();
                        try (BufferedReader reader = new BufferedReader(
                                new InputStreamReader(serverProcess.getInputStream())))  {
                            String line;

                            while ((line = reader.readLine()) != null) {
                                String timestamp = "[" + LocalTime.now() + "] " + line + "\n";
                                helper.AppendConsole(timestamp);
                            }
                        } catch (IOException e) {
                            helper.AppendConsole("Failed to start server: " + e.getMessage());
                        }
                    }).start();
                    serverProcess.waitFor();
                } catch (IOException | InterruptedException e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                            null,
                            "Failed to start server: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    ));
                }
                return null;
            }

            @Override
            protected void done() {
                progressBar.setIndeterminate(false);

                if (shouldRestart) {
                    StartServer(actionButtons, RAM, progressBar, serverType, serverVersion, autoEulaCheck, consoleArea);
                }

                if (shouldStop) {
                    progressBar.setString("Server stopped");

                    for (JButton button : actionButtons) {
                        button.setEnabled(true);
                    }
                }
            }
        }.execute();
    }

    public void DeleteServer(JFrame frame, String serverType, String serverVersion) {
        String serverDir = "servers/" + serverType + "/" + serverVersion;

        int choice = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to permanently delete the entire server folder?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            FileManager manager = new FileManager();
            manager.DeleteDirectoryRecursively(frame, new File(serverDir).toPath());
            JOptionPane.showMessageDialog(
                    frame,
                    "Server folder deleted successfully.",
                    "Deleted",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public void StopServer(JTextArea consoleArea) {
        if (serverProcess != null) {
            ConsoleHelper helper = new ConsoleHelper(consoleArea, serverProcess);
            helper.SendCommand("stop\n");
            shouldStop = true;
        }
    }

    public void RestartServer(JTextArea consoleArea) {
        shouldRestart = true;

        ConsoleHelper helper = new ConsoleHelper(consoleArea, serverProcess);
        helper.SendCommand("stop\n");
    }

    public void SendButtonClicked(JTextArea consoleArea, JTextField commandField) {
        if (serverProcess != null) {
            ConsoleHelper helper = new ConsoleHelper(consoleArea, serverProcess);
            helper.SendCommand(commandField.getText() + "\n");
            commandField.setText("");
        }
    }

    public void OpenFileExplorer(String path) {
        try {
            File directory = new File(path);
            if (directory.exists() && directory.isDirectory()) {
                Desktop.getDesktop().open(directory);
            } else {
                System.out.println("Directory does not exist!");
            }
        } catch (IOException e) {
            System.err.println("Error opening the file explorer!");
        } catch (UnsupportedOperationException e) {
            System.err.println("The current platform does not support the Desktop module!");
        }
    }
    //endregion
}