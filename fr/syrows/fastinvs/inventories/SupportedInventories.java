package fr.syrows.fastinvs.inventories;

import org.bukkit.event.inventory.InventoryType;

public enum SupportedInventories {

    CHEST(6, 9), ENDER_CHEST(3, 9), DROPPER(3, 3), DISPENSER(3, 3), HOPPER(1, 5);

    private int rows, columns;

    SupportedInventories(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public int getDefaultRows() { return this.rows; }

    public int getDefaultColumns() { return this.columns; }

    public static boolean isSupported(InventoryType type) {

        try {

            SupportedInventories.valueOf(type.name());
            return true;

        } catch (IllegalArgumentException e) {

            return false;
        }
    }

    public static SupportedInventories get(InventoryType type) {
        return isSupported(type) ? SupportedInventories.valueOf(type.name()) : null;
    }
}
