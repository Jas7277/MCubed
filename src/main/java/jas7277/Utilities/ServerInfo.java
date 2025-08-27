package jas7277.Utilities;

public class ServerInfo {
    private final String id;
    private final String jsonUrl;
    private final String downloadUrl;

    public ServerInfo(String id, String jsonUrl, String downloadUrl) {
        this.id = id;
        this.jsonUrl = jsonUrl;
        this.downloadUrl = downloadUrl;
    }

    public String getId() {
        return this.id;
    }

    public String getJsonUrl() {
        return this.jsonUrl;
    }

    public String getDownloadUrl() {
        return this.downloadUrl;
    }

}
