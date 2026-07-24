package dev.speedslicer.server.main;

import dev.speedslicer.api.player.PlayerData;
import dev.speedslicer.server.bootstrap.data.playerdata.PlayerDataManager;
import dev.speedslicer.server.bootstrap.registry.WeaponRegistry;
import dev.speedslicer.server.bootstrap.data.weapons.WeaponDataLoader;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Area;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.common.DisconnectPacket;

import java.io.IOException;

public class ServerController {
    WeaponDataLoader weaponDataLoader;
    WeaponRegistry weaponRegistry;
    InstanceContainer instanceContainer; // TODO replac with actual lobby system
    PlayerDataManager playerDataManager;
    public ServerController() throws IOException {
        weaponRegistry = new WeaponRegistry();
        weaponDataLoader = new WeaponDataLoader();
        playerDataManager = new PlayerDataManager();
        weaponDataLoader.bootstrapLoad(this);
        MinecraftServer minecraftServer = MinecraftServer.init();
        SetupLobby();
        SetupGlobalPackets();
        minecraftServer.start("0.0.0.0", 25565);
    }
    public void SetupLobby(){
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setBlockArea(Area.cube(new Pos(0,30,0), 5), Block.STONE);
    }
    public void SetupGlobalPackets() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 42, 0));
            try {
                playerDataManager.registerPlayer(player.getUuid());
            } catch (IOException e) {
                player.kick("Failed to get player data. Something broke!");
                Main.getLogger().error(player.getUsername() + " attempted to log in without any data. Possbile bot?");
                throw new RuntimeException(e);
            }
        });
        globalEventHandler.addListener(PlayerDisconnectEvent.class, event -> {
           final Player player = event.getPlayer();
           try {
               playerDataManager.unregisterPlayer(player.getUuid());
           }
           catch (IOException e) {
               Main.getLogger().error("Cannot save player " + player.getUsername() + "/" + player.getUuid() + " to server!");
           }
        });
    }
    public WeaponRegistry getWeaponRegistry() {
        return weaponRegistry;
    }
}
