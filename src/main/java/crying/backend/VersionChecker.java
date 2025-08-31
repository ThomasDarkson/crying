package crying.backend;

import crying.Crying;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class VersionChecker {
    private static final String VERSION_URL = "https://raw.githubusercontent.com/ThomasDarkson/crying/refs/heads/version/version.txt";

    private CryingVersion latestVersion;

    public VersionChecker() {
        checkForUpdates();
    }

    private VersionChecker checkForUpdates() {
        new Thread(() -> {
            try {
                URI uri = new URI(VERSION_URL);

                HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build();
                HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().timeout(Duration.ofSeconds(3)).build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200)
                    return;

                String latest = response.body().trim();
                this.latestVersion = parseVersion(latest);

                if (latestVersion != null && isNewer(latestVersion, Crying.VERSION)) {
                    Crying.LOGGER.warn("A new version is available for Crying Tools. Current: " + Crying.VERSION + ", Latest: " + latestVersion);
                }
                else
                    this.latestVersion = null;
            } 
            catch (Exception e) {
                this.latestVersion = null;
            }
        }, "crying_tools_version_check").start();
        return this;
    }

    public final CryingVersion getLatestVersion() {
        return this.latestVersion;
    }

    private CryingVersion parseVersion(String versionString) {
        try {
            String[] parts = versionString.split("\\.");
            int major = Integer.parseInt(parts[0]);
            int minor = Integer.parseInt(parts[1]);
            int patch = Integer.parseInt(parts[2]);
            return CryingVersion.version(major, minor, patch);
        } 
        catch (Exception e) {
            return null;
        }
    }

    private boolean isNewer(CryingVersion remote, CryingVersion local) {
        if (remote == null) 
            return false;

        if (remote.major > local.major) 
            return true;
        if (remote.major == local.major && remote.minor > local.minor) 
            return true;
        return remote.major == local.major && remote.minor == local.minor && remote.patch > local.patch;
    }
}
