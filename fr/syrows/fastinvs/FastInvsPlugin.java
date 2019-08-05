package fr.syrows.fastinvs;

import fr.syrows.fastinvs.inventories.InventoryManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FastInvsPlugin extends JavaPlugin {

    private InventoryManager invManager;

    @Override
    public void onEnable() {

        FastInvsAPI.initialize();

        this.invManager = new InventoryManager(this);
    }

    public InventoryManager getInvManager() { return invManager; }
}
