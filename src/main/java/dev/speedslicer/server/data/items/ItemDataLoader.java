package dev.speedslicer.server.data.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.speedslicer.api.item.ItemCategories;
import dev.speedslicer.api.item.data.ItemData;
import dev.speedslicer.server.main.Main;
import dev.speedslicer.server.main.ServerController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class ItemDataLoader {
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public void bootstrapLoad(ServerController serverController)
            throws IOException {
        Main.getLogger().info("Generic item bootstrap load");
        Path folder = Path.of("data", "items");
        Path categoryFile = folder.resolve("category.json");
        Files.createDirectories(folder);

        if (!Files.exists(categoryFile)) {
            ItemCategories categories =
                    new ItemCategories(List.of("weapon", "armor", "misc"));

            Files.writeString(categoryFile, gson.toJson(categories));
        }
        ItemCategories categories = gson.fromJson(Files.readString(categoryFile), ItemCategories.class);
        for (String c : categories.categories()) {
            serverController.getItemRegistry().createRegistry(c);
            var lFolder = folder.resolve(c);
            Files.createDirectories(lFolder);

            try (var files = Files.list(lFolder)) {
                files.filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".json"))
                        .forEach(x -> {
                            try {
                                serverController.getItemRegistry().addItem(gson.fromJson(Files.readString(x), ItemData.class));
                            } catch (IOException e) {
                                Main.getLogger().error("Failed to load {}", x);
                                throw new RuntimeException(e);
                            }
                        });
            }        }
    }
}
