package jas7277.Model;

import java.io.Serializable;

public record ServerInfo(ServerTypes type, String version, String downloadUrl) implements Serializable {
}
