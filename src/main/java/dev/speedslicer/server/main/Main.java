package dev.speedslicer.server.main;

import dev.speedslicer.api.jsonExample.ExampleGenerator;

import net.minestom.server.network.socket.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(Main.class);
    public static ServerController instance;
    public static ExampleGenerator exampleGenerator;
    static void main(String[] args) throws IOException {
        exampleGenerator = new ExampleGenerator();
        LOGGER.info("START");
        LOGGER.info("Generating Examples");
        exampleGenerator.generateExamples();
        LOGGER.info("Initializing Server");
        ServerController instance = new ServerController();
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
