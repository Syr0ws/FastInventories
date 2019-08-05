package fr.syrows.fastinvs.inventories.openers;

import fr.syrows.fastinvs.FastInventory;
import fr.syrows.fastinvs.contents.Pagination;
import fr.syrows.fastinvs.inventories.InventoryContents;
import fr.syrows.fastinvs.inventories.SupportedInventories;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface InventoryOpener {

    void open(Player player, FastInventory inv);

    boolean support(SupportedInventories supported);

    default void fill(FastInventory inv, Inventory handle) {

        if(inv.hasPagination()) {

            Pagination pagination = inv.getPagination();

            pagination.setPageContents(inv);
            pagination.setPages(inv);
        }
        InventoryContents contents = inv.getContents();

        contents.fill(handle);
    }
}
