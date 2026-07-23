package dev.speedslicer.main;

import dev.speedslicer.game.registry.WeaponRegistry;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.socket.Server;

public class Main {
    public static ServerController instance;
    public static void main(String[] args) {
        ServerController instance = new ServerController();
    }

}
