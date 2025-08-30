package jas7277.Utilities;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ConsoleHelper {
    private final JTextArea consoleArea;
    private final Process serverProcess;
    private final BufferedWriter serverWriter;

    public ConsoleHelper(JTextArea consoleArea, Process serverProcess) {
        this.consoleArea = consoleArea;
        this.serverProcess = serverProcess;
        this.serverWriter = new BufferedWriter(new OutputStreamWriter(serverProcess.getOutputStream()));
    }

    public void AppendConsole(String text) {
        SwingUtilities.invokeLater(() -> {
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
                AppendConsole(command);
            } catch (IOException e) {
                AppendConsole("Failed to send command: " + e.getMessage() + "\n");
            }
        } else {
            AppendConsole("Server is not running!\n");
        }
    }
}
