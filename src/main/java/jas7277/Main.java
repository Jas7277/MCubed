package jas7277;

import javax.swing.SwingUtilities;

import jas7277.GUI.Frame;
import jas7277.Utilities.FileManager;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File manifest = new File(FileManager.manifest_filename);

        if (!manifest.exists()) {
            FileManager.DownloadManifest();
        }

        SwingUtilities.invokeLater(new Frame());
    }
}