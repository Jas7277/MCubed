package jas7277.Model;

public final class Constants {
    private Constants() {
        // Nothing to instantiate, just for hiding the constructor
    }
    // File constants
    public static final String MANIFEST_FILENAME = "servers.json";
    public static final String MANIFEST_URL = "https://piston-meta.mojang.com/mc/game/version_manifest.json";
    public static final String SERVER_VERSIONS_FILE = "src/main/data/server-versions.ser";
    public static final String SERVERS_DIR = "servers/";
    public static final String SERVER_JAR_NAME = "server.jar";
    public static final String EULA_FILENAME = "eula.txt";

    // Thread/Timer constants
    public static final int TIMER_INTERVAL_MS = 1000;
    public static final int THREAD_POOL_SIZE = 5;
    public static final long PROCESS_TIMEOUT_SECONDS = 60;

    // Server settings
    public static String DEFAULT_JAVA_ARGS = "-Xmx1024M -Xms1024M";
    public static int DEFAULT_PORT = 25565;
}
