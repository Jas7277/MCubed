package jas7277.Utilities;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileManager {
    private static final String manifest_url = "https://piston-meta.mojang.com/mc/game/version_manifest.json";
    private static final String manifest_filename = "servers.json";

    public static void DownloadFile(String url, String file, Consumer<Integer> onProgress) throws IOException {
        URL link = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) link.openConnection();
        conn.setConnectTimeout(10_000);
        conn.setReadTimeout(15_000);

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Server returned HTTP " + responseCode + ": " + conn.getResponseMessage());
        }

        int contentLength = conn.getContentLength();
        if (contentLength <= 0) {
            throw new IOException("Invalid content length: " + contentLength);
        }

        try (InputStream inputStream = conn.getInputStream()) {
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[8192];
            int bytesRead;
            int totalRead = 0;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalRead += bytesRead;

                if (onProgress != null) {
                    int percent = (int) ((totalRead * 100) / contentLength);
                    onProgress.accept(percent);
                }
            }

            outputStream.flush();
            outputStream.close();
        } finally {
            conn.disconnect();
        }
    }

    public static ArrayList<ServerInfo> GetServersFromFile(String jsonFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode root = objectMapper.readTree(Paths.get(jsonFile).toFile());
        JsonNode versionsNode = root.path("versions");

        ArrayList<ServerInfo> serversList = new ArrayList<ServerInfo>();

        for (JsonNode versionNode : versionsNode) {
            String id = versionNode.path("id").asText();

            if (!ContainsLetterRegex(id)) {
                String jsonUrl = versionNode.path("url").asText();

                serversList.add(new ServerInfo(id, jsonUrl, GetDownloadFromServerJson(jsonUrl)));
            }
        }

        return serversList;
    }

    private static String GetDownloadFromServerJson(String jsonUrl) {
        try {
            DownloadFile(jsonUrl, "download.json", null);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(Paths.get("download.json").toFile());

            return root.path("downloads")
                    .path("server")
                    .path("url")
                    .asText();
        } catch (IOException e) {
            System.err.println("Error downloading the json file for the server download link");
            return null;
        }
    }

    public static void DownloadManifest() {
        File file = new File(manifest_filename);

        if (file.exists()) {
            return;
        }

        try {
            FileManager.DownloadFile(manifest_url, manifest_filename, null);
        } catch (IOException e) {
            System.err.println("Error retrieving the version_manifest.json!");
        }
    }

    public void DeleteDirectoryRecursively(Path path) throws IOException {
        if (!Files.exists(path)) return;
        Files.walk(path)
                .sorted(Comparator.reverseOrder()) // Delete children before parents
                .forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
    }

    private static boolean ContainsLetterRegex(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        Pattern p = Pattern.compile("[a-zA-Z]");
        Matcher m = p.matcher(str);

        return m.find();
    }
}
