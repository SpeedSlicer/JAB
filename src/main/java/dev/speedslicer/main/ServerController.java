package dev.speedslicer.main;

import dev.speedslicer.game.registry.WeaponRegistry;
import dev.speedslicer.loader.WeaponDataLoader;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Area;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;

import java.io.IOException;

public class ServerController {
    WeaponDataLoader weaponDataLoader;
    WeaponRegistry weaponRegistry;
    InstanceContainer instanceContainer; // TODO replac with actual lobby system
    public ServerController() throws IOException {
        weaponRegistry = new WeaponRegistry();
        weaponDataLoader = new WeaponDataLoader();
        weaponDataLoader.bootstrapLoad(this);
        MinecraftServer minecraftServer = MinecraftServer.init();
        SetupLobby();
        SetupPlayer();
        minecraftServer.start("0.0.0.0", 25565);
    }
    public void SetupLobby(){
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setBlockArea(Area.cube(new Pos(0,30,0), 5), Block.STONE);
    }
    public void SetupPlayer() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 42, 0));
        });
    }
    public WeaponRegistry getWeaponRegistry() {
        return weaponRegistry;
    }
}
