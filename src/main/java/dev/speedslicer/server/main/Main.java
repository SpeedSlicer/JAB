package dev.speedslicer.server.main;

import dev.speedslicer.server.bootstrap.registry.WeaponRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {
    public static ServerController instance;
    private static final Logger LOGGER =
            LoggerFactory.getLogger(WeaponRegistry.class);
    public static void main(String[] args) throws IOException {
        LOGGER.info("Beginning");
        ServerController instance = new ServerController();
    }
    public static Logger getLogger() {
        return LOGGER;
    }
}
