package jas7277.Controller;

import jas7277.Model.ConsoleHelper;
import jas7277.Model.FileManager;
import jas7277.Model.ServerInfo;
import jas7277.Model.ServerProcesses;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;

public class LeftPanelController {
    private final FileManager fileManager;
    private ArrayList<ServerInfo> servers;
    private final ConsoleHelper helper;
    private Process serverProcess;
    private String serverType;
    private String serverVersion;
    private int RAM;

    public LeftPanelController() {
        fileManager = new FileManager();
        servers = new ArrayList<>();
        helper = new ConsoleHelper();
    }

    public void StartServer(String serverType, String serverVersion, int RAM) {
        this.serverType = serverType;
        this.serverVersion = serverVersion;
        this.RAM = RAM;
        String serverDir = "servers/" + this.serverType + "/" + this.serverVersion;
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
            @Override
            protected void done() {

            }
        }.execute();
    }

    public void StopServer() {
        if (serverProcess != null && serverProcess.isAlive()) {
            helper.SendCommand("stop\n");
        }
    }

    public void RestartServer() {
        StopServer();
        StartServer(serverType, serverVersion, RAM);
    }

    public String[] GetServerVersions() {
        servers = fileManager.ReadServerVersions();
        String[] versions;

        if (servers == null) {
            try {
                servers = fileManager.GetServersFromFile("servers.json");
                fileManager.SaveServerVersions(servers);
            } catch (IOException e) {
                System.err.println("Error retrieving the server info! Try resetting the server manifest file");
                return null;
            }
        }

        versions = new String[servers.toArray().length];
        int count = 0;
        for (ServerInfo server : servers) {
            versions[count] = server.id();
            count++;
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

    private void WriteEulaFile(String serverDir) {
        try (FileWriter writer = new FileWriter(new File(serverDir, "eula.txt"))) {
            writer.write("eula=true\n");
        } catch (IOException e) {
            helper.AppendConsole("Failed to write eula.txt " + e.getMessage(), Color.RED);
        }
    }
}
