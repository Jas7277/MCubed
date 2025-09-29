package jas7277.Controller;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import jas7277.Model.ConsoleHelper;
import jas7277.Model.FileManager;
import jas7277.Model.ServerInfo;

public class EventController {
    //region Variables
    private final FileManager fileManager;
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