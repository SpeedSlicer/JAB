package dev.speedslicer.server.bootstrap.data.playerdata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import dev.speedslicer.api.APIVersion;
import dev.speedslicer.api.player.PlayerData;
import dev.speedslicer.server.ServerSettings;
import dev.speedslicer.server.main.Main;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {

    private static final Path PLAYER_DATA_FOLDER =
            Path.of("data", "player");

    private final ConcurrentHashMap<UUID, PlayerData> activePlayerData;
    private final Gson gson;

    public PlayerDataManager() {
        activePlayerData = new ConcurrentHashMap<>();
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public void registerPlayer(UUID playerId) {
        PlayerData playerData = loadPlayerData(playerId);
        activePlayerData.put(playerId, playerData);
    }

    public void unregisterPlayer(UUID playerId) {
        PlayerData playerData = activePlayerData.remove(playerId);

        if (playerData == null) {
            Main.getLogger().warn(
                    "Tried to save player {}, but they were not registered",
                    playerId
            );
            return;
        }

        savePlayerData(playerData);
    }

    private PlayerData loadPlayerData(UUID playerId) {
        Path file = getPlayerFile(playerId);

        if (Files.notExists(file)) {
            Main.getLogger().info(
                    "No player data found for {}, creating new data",
                    playerId
            );

            return new PlayerData(playerId);
        }

        try {
            String json = Files.readString(file, StandardCharsets.UTF_8);
            PlayerData data = gson.fromJson(json, PlayerData.class);

            if (data == null) {
                Main.getLogger().warn(
                        "Player data file for {} was empty",
                        playerId
                );

                return new PlayerData(playerId);
            }
            if (data.getVersion() != APIVersion.playerDataVersion) {
                Main.getLogger().warn("Loading non-similar version of weapon data for {}, update your JSON!", data.getVersion());
                if (ServerSettings.safeMode) {
                    Main.getLogger().error("Safe mode on! Aborting load");
                    throw new RuntimeException();
                }
            }
            return data;
        } catch (IOException | JsonParseException exception) {
            Main.getLogger().error(
                    "Could not load player data for {} from {}",
                    playerId,
                    file.toAbsolutePath(),
                    exception
            );

            return new PlayerData(playerId);
        }
    }

    private void savePlayerData(PlayerData playerData) {
        Path file = getPlayerFile(playerData.getUuid());

        try {
            Files.createDirectories(PLAYER_DATA_FOLDER);

            String json = gson.toJson(playerData);

            Files.writeString(
                    file,
                    json,
                    StandardCharsets.UTF_8
            );
        } catch (IOException exception) {
            Main.getLogger().error(
                    "Could not save player data for {}",
                    playerData.getUuid(),
                    exception
            );
        }
    }

    private Path getPlayerFile(UUID playerId) {
        return PLAYER_DATA_FOLDER.resolve(playerId + ".json");
    }

    public PlayerData getPlayerData(UUID playerId) {
        return activePlayerData.get(playerId);
    }
}