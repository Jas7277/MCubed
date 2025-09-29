package jas7277;

import javax.swing.SwingUtilities;

import jas7277.View.Frame;
import jas7277.Model.FileManager;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
        File manifest = new File(FileManager.manifest_filename);

        if (!manifest.exists()) {
            fileManager.DownloadManifest();
        }

        SwingUtilities.invokeLater(new Frame());
    }
}