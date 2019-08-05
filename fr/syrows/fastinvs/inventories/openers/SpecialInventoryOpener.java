package fr.syrows.fastinvs.inventories.openers;

import fr.syrows.fastinvs.FastInventory;
import fr.syrows.fastinvs.inventories.SupportedInventories;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

import static fr.syrows.fastinvs.inventories.SupportedInventories.*;

public class SpecialInventoryOpener implements InventoryOpener {

    @Override
    public void open(Player player, FastInventory inv) {

        Inventory handle = Bukkit.createInventory(null, inv.getType(), inv.getTitle());

        fill(inv, handle);

        player.openInventory(handle);
    }

    @Override
    public boolean support(SupportedInventories supported) {
        List<SupportedInventories> list = Arrays.asList(ENDER_CHEST, DROPPER, DISPENSER, HOPPER);
        return list.contains(supported);
    }
}
