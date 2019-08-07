package fr.syrows.fastinvs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.syrows.fastinvs.contents.Pagination;
import fr.syrows.fastinvs.loader.LoadedInventory;
import fr.syrows.fastinvs.loader.LoadedItem;
import fr.syrows.fastinvs.utils.Logger;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public class FastInvsAPI {

    private static Gson gson;
    private static Plugin plugin;
    private static boolean debuggerEnabled;

    public static void initialize() {

        LoadedInventory.InventoryDeserializer inventoryDeserializer = new LoadedInventory.InventoryDeserializer();
        LoadedItem.ItemDeserializer itemDeserializer = new LoadedItem.ItemDeserializer();

        Pagination.PaginationDeserializer paginationDeserializer = new Pagination.PaginationDeserializer();

        Logger.log(Level.INFO, "Initializing Gson...");

        FastInvsAPI.gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .registerTypeAdapter(LoadedItem.class, itemDeserializer)
                .registerTypeAdapter(LoadedInventory.class, inventoryDeserializer)
                .registerTypeAdapter(Pagination.class, paginationDeserializer)
                .create();

        Logger.log(Level.INFO, "Gson initialized !");
    }

    public static Gson getGson() { return FastInvsAPI.gson; }

    public static Plugin getPlugin() { return plugin; }

    public static void enableDebugger(Plugin plugin) {

        FastInvsAPI.debuggerEnabled = true;
        FastInvsAPI.plugin = plugin;

        Logger.log(Level.INFO, String.format("Debugger enabled for %s", plugin.getName()));
    }

    public static boolean isDebuggerEnabled() { return debuggerEnabled; }
}
