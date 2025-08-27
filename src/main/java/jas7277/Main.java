package jas7277;

import jas7277.GUI.Frame;
import jas7277.Utilities.FileManager;

import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        FileManager.DownloadManifest();

        SwingUtilities.invokeLater(new Frame());
    }
}