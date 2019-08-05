package fr.syrows.fastinvs.loader;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import fr.syrows.fastinvs.FastInvsAPI;
import fr.syrows.fastinvs.utils.ItemUtils;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class LoadedItem {

    private ItemStack stack;
    private int[] slots;

    public int[] getSlots() {
        return slots;
    }

    public ItemStack getItemStack() {
        return stack;
    }

    public static class ItemDeserializer implements JsonDeserializer<LoadedItem> {

        @Override
        public LoadedItem deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {

            JsonObject object = element.getAsJsonObject();

            String itemInBase64 = object.get("itemInBase64").getAsString();

            int[] slots = new int[0];

            if(object.has("slot")) {

                slots = new int[1];
                slots[0] = object.get("slot").getAsInt();

            } else if(object.has("slots")) {

                Type slotType = new TypeToken<int[]>(){}.getType();
                slots = FastInvsAPI.getGson().fromJson(object.get("slots"), slotType);
            }
            LoadedItem loadedItem = new LoadedItem();

            loadedItem.stack = ItemUtils.deserializeFromBase64(itemInBase64);
            loadedItem.slots = slots;

            return loadedItem;
        }
    }
}
