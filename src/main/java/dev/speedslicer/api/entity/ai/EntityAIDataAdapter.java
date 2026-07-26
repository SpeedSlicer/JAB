package dev.speedslicer.api.entity.ai;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Map;

public final class EntityAIDataAdapter extends TypeAdapter<EntityAIData> {
    private static final TypeToken<Map<String, JsonElement>> DATA_TYPE =
            new TypeToken<>() {
            };

    private final Gson gson;

    public EntityAIDataAdapter() {
        this(new Gson());
    }

    public EntityAIDataAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, EntityAIData value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        out.name("id").value(value.id());
        out.name("data");
        gson.toJson(value.data(), DATA_TYPE.getType(), out);
        out.endObject();
    }

    @Override
    public EntityAIData read(JsonReader in) throws IOException {
        JsonElement value = gson.fromJson(in, JsonElement.class);

        if (value == null || value.isJsonNull()) {
            return null;
        }

        if (value.isJsonPrimitive() && value.getAsJsonPrimitive().isString()) {
            return new EntityAIData(value.getAsString());
        }

        if (!value.isJsonObject()) {
            throw new JsonParseException(
                    "AI entry must be a string or object"
            );
        }

        JsonObject object = value.getAsJsonObject();
        JsonElement idElement = object.get("id");

        if (idElement == null || !idElement.isJsonPrimitive()) {
            throw new JsonParseException("AI entry is missing an id");
        }

        Map<String, JsonElement> data = object.has("data")
                ? gson.fromJson(object.get("data"), DATA_TYPE)
                : Map.of();

        return new EntityAIData(idElement.getAsString(), data);
    }
}
