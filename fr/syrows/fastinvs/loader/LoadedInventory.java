package fr.syrows.fastinvs.loader;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import fr.syrows.fastinvs.FastInvsAPI;
import fr.syrows.fastinvs.contents.Pagination;
import org.bukkit.event.inventory.InventoryType;

import java.lang.reflect.Type;
import java.util.Map;

public class LoadedInventory {

    private String title;
    private InventoryType type;
    private int size;

    private Pagination pagination;

    private Map<String, LoadedItem> contents;

    public String getTitle() {
        return title;
    }

    public InventoryType getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public Pagination getPagination() { return pagination; }

    public Map<String, LoadedItem> getContents() {
        return contents;
    }

    public static class InventoryDeserializer implements JsonDeserializer<LoadedInventory> {

        @Override
        public LoadedInventory deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {

            JsonObject object = element.getAsJsonObject();

            String title = object.get("title").getAsString();

            InventoryType invType = object.has("type") ?
                    InventoryType.valueOf(object.get("type").getAsString()) : InventoryType.CHEST;

            int size = object.has("size") ?
                    object.get("size").getAsInt() : invType.getDefaultSize();

            LoadedInventory loadedInv = new LoadedInventory();

            loadedInv.title = title;
            loadedInv.type = invType;
            loadedInv.size = size;

            Type contentType = new TypeToken<Map<String, LoadedItem>>(){}.getType();
            loadedInv.contents = FastInvsAPI.getGson().fromJson(object.get("contents"), contentType);

            if(object.has("pagination"))
                loadedInv.pagination = FastInvsAPI.getGson().fromJson(object.get("pagination"), Pagination.class);

            return loadedInv;
        }
    }
}
