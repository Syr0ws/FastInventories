package fr.syrows.fastinvs.loader;

import com.google.gson.reflect.TypeToken;
import fr.syrows.fastinvs.FastInventory;
import fr.syrows.fastinvs.FastInvsAPI;
import fr.syrows.fastinvs.contents.ClickableItem;
import fr.syrows.fastinvs.contents.Pagination;
import fr.syrows.fastinvs.inventories.InventoryContents;
import fr.syrows.fastinvs.inventories.InventoryManager;
import fr.syrows.fastinvs.tools.SlotIterator;
import fr.syrows.fastinvs.utils.FileUtils;
import fr.syrows.fastinvs.utils.Logger;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Level;

public class InventoryLoader {

    private Plugin plugin;

    private String resourceFolderPath, fileName;
    private boolean create;

    private Map<String, LoadedInventory> loadedInventories;

    public InventoryLoader(Plugin plugin, String resourceFolderPath, String fileName, boolean create) {
        this.plugin = plugin;
        this.resourceFolderPath = resourceFolderPath;
        this.fileName = fileName;
        this.create = create;
    }

    public void loadInventories() {

        BufferedReader reader = null;

        String resourcePath = this.resourceFolderPath.equals("") ?
                this.fileName : String.format("%s/%s", this.resourceFolderPath, this.fileName);

        if(this.create) {

            FileUtils.createDirectory(this.plugin.getDataFolder().toPath());

            Path path = Paths.get(this.plugin.getDataFolder() + File.separator + this.fileName);
            FileUtils.createFileFromResource(this.plugin, path, resourcePath, false);

            try {
                reader = new BufferedReader(new FileReader(path.toFile()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else {

            InputStream stream = this.plugin.getResource(resourcePath);
            reader = new BufferedReader(new InputStreamReader(stream));
        }
        if(reader == null) return;

        Logger.log(Level.INFO, String.format("Loading inventories from %s %s.", this.create ? "file" : "resource", fileName));

        Type type = new TypeToken<Map<String, LoadedInventory>>(){}.getType();
        this.loadedInventories = FastInvsAPI.getGson().fromJson(reader, type);

        Logger.log(Level.INFO, "Inventories loaded.");
    }

    public FastInventory getAsFastInventoryWithoutPagination(String id, InventoryManager invManager) {

        if(!this.loadedInventories.containsKey(id))
            throw new NullPointerException(String.format("Cannot find an inventory with the id %s.", id));

        LoadedInventory loadedInv = this.loadedInventories.get(id);

        FastInventory inv = new FastInventory.InventoryBuilder(invManager)
                .withId(id).withTitle(loadedInv.getTitle())
                .withSize(loadedInv.getSize())
                .withType(loadedInv.getType())
                .build();

        setContents(loadedInv, inv);

        return inv;
    }

    public FastInventory getAsFastInventoryWithPagination(String id, InventoryManager invManager, SlotIterator iterator) {

        if(!this.loadedInventories.containsKey(id))
            throw new NullPointerException(String.format("Cannot find an inventory with the id %s.", id));

        LoadedInventory loadedInv = this.loadedInventories.get(id);

        Pagination loadedPagination = loadedInv.getPagination();

        Pagination pagination = new Pagination.PaginationBuilder(iterator)
                .withPreviousPage(loadedPagination.getPreviousPage().getItemStack(), loadedPagination.getPreviousPageSlot())
                .withNextPage(loadedPagination.getNextPage().getItemStack(), loadedPagination.getNextPageSlot())
                .build();

        FastInventory inv = new FastInventory.InventoryBuilder(invManager)
                .withId(id).withTitle(loadedInv.getTitle())
                .withSize(loadedInv.getSize())
                .withType(loadedInv.getType())
                .withPagination(pagination)
                .build();

        setContents(loadedInv, inv);

        return inv;
    }

    private void setContents(LoadedInventory loadedInv, FastInventory inv) {

        InventoryContents contents = inv.getInventoryContents();

        loadedInv.getContents().forEach((itemId, loadedItem) -> {

            ClickableItem clickable = new ClickableItem(itemId, loadedItem.getItemStack());

            for(int slot : loadedItem.getSlots()) contents.set(slot, clickable);
        });
    }

    public String getResourceFolderPath() {
        return resourceFolderPath;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean canBeCreated() {
        return create;
    }
}
