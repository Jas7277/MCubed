package jas7277.View;

import java.awt.*;
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

        frame.add(new MainPanel(), BorderLayout.CENTER);
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
}
