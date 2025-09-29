package jas7277.Model;

import jas7277.View.RightPanel;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ConsoleHelper {
    private final JTextArea consoleArea;
    private Process serverProcess;
    private BufferedWriter serverWriter;

    public ConsoleHelper() {
        this.consoleArea = RightPanel.consoleArea;
    }

    public void ProcessStarted(Process serverProcess) {
        this.serverProcess = serverProcess;
        this.serverWriter = new BufferedWriter(new OutputStreamWriter(serverProcess.getOutputStream()));
    }

    public void AppendConsole(String text, Color color) {
        SwingUtilities.invokeLater(() -> {
            consoleArea.setForeground(color);
            consoleArea.append(text);
            consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
        });
    }

    public void Clear() {
        SwingUtilities.invokeLater(() -> consoleArea.setText(null));
    }

    public void SendCommand(String command) {
        if (serverWriter != null && serverProcess != null && serverProcess.isAlive()) {
            try {
                serverWriter.write(command);
                serverWriter.flush();
                AppendConsole(command, Color.BLACK);
            } catch (IOException e) {
                AppendConsole("Failed to send command: " + e.getMessage() + "\n", Color.RED);
            }
        } else {
            AppendConsole("Server is not running!\n", Color.RED);
        }
    }
}
