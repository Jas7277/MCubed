package jas7277.Controller;

import jas7277.Model.*;
import jas7277.View.LeftPanel;
import static jas7277.Model.Constants.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class LeftPanelController {
    private final FileManager fileManager;
    private ArrayList<ServerInfo> servers;
    private final ConsoleHelper helper;
    private Process serverProcess;
    private String serverType;
    private String serverVersion;
    private int RAM;
    private final LeftPanel view;
    private boolean serverRunning;

    public LeftPanelController(LeftPanel view) {
        fileManager = new FileManager();
        servers = new ArrayList<>();
        helper = new ConsoleHelper();
        this.view = view;

        if (!(new File(MANIFEST_FILENAME).exists())) {
            fileManager.DownloadManifest(() -> SwingUtilities.invokeLater(() ->
                    refreshVersions(GetServerVersions())
            ));
        }
    }

    public void StartServer(String serverType, String serverVersion, int RAM) {
        this.serverType = serverType;
        this.serverVersion = serverVersion;
        this.RAM = RAM;
        String serverDir;

        if (serverType.equals("Vanilla")) {
            serverDir = VANILLA_SERVERS_DIR + "/" + serverVersion;
        } else {
            serverDir = "";
        }

        WriteEulaFile(serverDir);

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

                    ServerProcesses.processes.add(serverProcess);

                    helper.ProcessStarted(serverProcess);

                    new Thread(() -> {
                        helper.Clear();
                        try (BufferedReader reader = new BufferedReader(
                                new InputStreamReader(serverProcess.getInputStream()))) {
                            String line;

                            while ((line = reader.readLine()) != null) {
                                String timestamp = "[" + LocalTime.now() + "] " + line + "\n";
                                helper.AppendConsole(timestamp, Color.BLACK);

                                if (line.contains("For help, type \"help\"")) {
                                    serverRunning = true;
                                }
                            }
                        } catch (IOException e) {
                            helper.AppendConsole("Failed to start server: " + e.getMessage(), Color.RED);
                        }
                    }).start();
                    serverProcess.waitFor();
                } catch (IOException | InterruptedException e) {
                    helper.AppendConsole("Failed to start server: " + e.getMessage(), Color.RED);
                }
                return null;
            }
        }.execute();
    }

    public boolean StopServer() {
        if (serverProcess != null && serverProcess.isAlive() && serverRunning) {
            helper.SendCommand("stop\n");
            serverRunning = false;
            return true;
        }
        return false;
    }

    public void RestartServer() {
        StopServer();
        StartServer(serverType, serverVersion, RAM);
    }

    private void ResetServerInfo() {
        try {
            servers = fileManager.GetServersFromFile("servers.json");
        } catch (IOException e) {
            helper.AppendConsole("Error resetting the server info!", Color.RED);
        }
    }

    public String[] GetServerVersions() {
        servers = fileManager.ReadServerVersions();
        String[] versions;

        if (servers == null) {
            ResetServerInfo();
        }

        versions = new String[servers.toArray().length];
        int count = 0;
        for (ServerInfo server : servers) {
            versions[count] = server.version();
            count++;
        }

        return versions;
    }

    public String GetServerUrl(ServerInfo server) {
        servers = fileManager.ReadServerVersions();

        if (servers == null) {
            ResetServerInfo();
        }

        for (ServerInfo s : servers) {
            if (server.type().equals(s.type()) && server.version().equals(s.version())) {
                return s.downloadUrl();
            }
        }
        return null;
    }

    private void WriteEulaFile(String serverDir) {
        try (FileWriter writer = new FileWriter(new File(serverDir, EULA_FILENAME))) {
            writer.write("eula=true\n");
        } catch (IOException e) {
            helper.AppendConsole("Failed to write eula.txt " + e.getMessage(), Color.RED);
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

    public void DeleteServer(Container frame, String path) {

        int choice = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to permanently delete the entire server folder?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            FileManager manager = new FileManager();
            manager.DeleteDirectoryRecursively(frame, new File(path).toPath());
            JOptionPane.showMessageDialog(
                    frame,
                    "Server folder deleted successfully.",
                    "Deleted",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public void DownloadButtonClicked(ServerInfo server) {
        new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() {
                Path path = Paths.get("servers/Vanilla/" + server.version());

                try {
                    Files.createDirectories(path);

                    String name = path + "/server.jar";

                    File file = new File(name);

                    if (!file.exists()) {
                        fileManager.DownloadFile(GetServerUrl(server), name, this::publish);
                    }
                } catch (IOException e) {
                    System.err.println("Error writing the new server JAR file!");
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {

                }
            }
        }.execute();
    }

    public void refreshVersions(String[] versions) {
        ResetServerInfo();
        SwingUtilities.invokeLater(() -> {
            JComboBox<String> box =  view.getVersionBox();
            String selected = (String) box.getSelectedItem();

            DefaultComboBoxModel<String> model =
                    new DefaultComboBoxModel<>(versions);

            box.setModel(model);

            if (selected != null && Arrays.asList(versions).contains(selected)) {
                box.setSelectedItem(selected);
            }
        });
    }
}
