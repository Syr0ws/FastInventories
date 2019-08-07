package fr.syrows.fastinvs.inventories.openers;

import fr.syrows.fastinvs.FastInventory;
import fr.syrows.fastinvs.inventories.SupportedInventories;
import fr.syrows.fastinvs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CommonInventoryOpener implements InventoryOpener {

    @Override
    public void open(Player player, FastInventory inv) {

        Inventory handle = Bukkit.createInventory(null, inv.getSize(), Utils.parseColors(inv.getTitle()));

        fill(inv, handle);

        player.openInventory(handle);
    }

    @Override
    public boolean support(SupportedInventories supported) {
        return supported == SupportedInventories.CHEST;
    }
}
