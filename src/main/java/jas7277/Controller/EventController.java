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
    //endregion
}