package jas7277;

import javax.swing.SwingUtilities;

import jas7277.GUI.Frame;
import jas7277.Utilities.FileManager;

public class Main {
    public static void main(String[] args) {
        FileManager.DownloadManifest();

        SwingUtilities.invokeLater(new Frame());
    }
}