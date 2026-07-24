package dev.speedslicer.server.bootstrap.data.playerdata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.speedslicer.api.player.PlayerData;
import dev.speedslicer.server.main.Main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager {
    HashMap<UUID, PlayerData> activePlayerData;
    Gson gson;
    public PlayerDataManager() {
        activePlayerData = new HashMap<>();
        gson = new GsonBuilder().create();
    }

    public void registerPlayer(UUID playerID) throws IOException {
        Path folder = Path.of("data", "player");
        Files.createDirectories(folder);
        activePlayerData.put(playerID, getPlayerdata(playerID));
    }

    public void unregisterPlayer(UUID playerID) throws IOException {
        Path file = Path.of("data", "player", playerID.toString()+".json");
        Files.write(file, gson.toJson(activePlayerData.get(playerID)).getBytes());
        activePlayerData.remove(playerID);
    }

    PlayerData getPlayerdata(UUID playerID) {
        Path file = Path.of("data", "player", playerID.toString()+".json");
        PlayerData data = null;
        if (Files.exists(file)) {
            try {
                data = gson.fromJson(Files.readString(file), PlayerData.class);
            }
            catch (IOException io) {
                Main.getLogger().error(io.getLocalizedMessage());
                io.printStackTrace();
                Main.getLogger().error("PlayerData doesn't exist?");
            }
        }
        else {
            try {
                Files.createFile(file);
                data = new PlayerData(playerID);
            }
            catch (IOException io) {
                Main.getLogger().error("Unable to create file");
                io.printStackTrace();
            }
        }
        return data;
    }
}
