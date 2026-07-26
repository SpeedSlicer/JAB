package dev.speedslicer;

import net.minestom.server.MinecraftServer;

public final class MinestomTestBootstrap {
    private static boolean initialized;

    private MinestomTestBootstrap() {
    }

    public static synchronized void ensureInitialized() {
        if (!initialized) {
            MinecraftServer.init();
            initialized = true;
        }
    }
}
