package fr.syrows.fastinvs.inventories;

import fr.syrows.fastinvs.FastInventory;
import fr.syrows.fastinvs.contents.ClickableItem;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryContents {

    private SupportedInventories supported;

    private int rows, columns;
    private ClickableItem[][] contents;

    public InventoryContents(FastInventory inv) {

        InventoryType type = inv.getType();

        if(!SupportedInventories.isSupported(type))
            throw new IllegalArgumentException(String.format("InventoryType %s is not supported.", type.name()));

        this.supported = SupportedInventories.get(type);

        this.rows = inv.getRows();
        this.columns = inv.getColumns();

        this.contents = new ClickableItem[this.rows][this.columns];
    }

    public InventoryContents(Inventory inv) {

        InventoryType type = inv.getType();

        if(!SupportedInventories.isSupported(type))
            throw new IllegalArgumentException(String.format("InventoryType %s is not supported.", type.name()));

        SupportedInventories supported = SupportedInventories.get(type);

        this.supported = supported;

        this.rows = inv.getSize() / supported.getDefaultColumns();
        this.columns = inv.getSize() / this.rows;

        this.contents = new ClickableItem[this.rows][this.columns];

        for(int i = 0; i < inv.getSize(); i++) {

            ItemStack stack = inv.getItem(i);

            if (stack != null) set(i, new ClickableItem(stack));
        }
    }

    public InventoryContents set(int row, int column, ClickableItem item) {

        if(row > this.rows) return this;

        if(column > this.columns) return this;

        this.contents[row - 1][column - 1] = item;

        return this;
    }

    public InventoryContents set(int slot, ClickableItem item) {

        int row = slot / this.supported.getDefaultColumns(),
                column = slot % this.supported.getDefaultColumns();

        if(row > this.rows) return this;

        if(column > this.columns) return this;

        this.contents[row][column] = item;

        return this;
    }

    public InventoryContents fillRow(int row, ClickableItem item) {

        if(row > this.rows) return this;

        for(int column = 1; column < this.columns + 1; column++)
            set(row, column, item);

        return this;
    }

    public InventoryContents fillColumn(int column, ClickableItem item) {

        if(column > this.columns) return this;

        for(int row = 1; row < this.rows + 1; row++)
            set(row, column, item);

        return this;
    }

    public InventoryContents clear(int row, int column) {

        set(row, column, null);

        return this;
    }

    public InventoryContents clear(int slot) {

        set(slot, null);

        return this;
    }

    public InventoryContents clearRow(int row) {

        if(row > this.rows) return this;

        for(int column = 1; column < this.columns + 1; column++)
            set(row, column, null);

        return this;
    }

    public InventoryContents clearColumn(int column) {

        if(column > this.columns) return this;

        for(int row = 1; row < this.rows + 1; row++)
            set(row, column, null);

        return this;
    }

    public InventoryContents clearAll() {

        for(int row = 1; row < this.rows + 1; row++) {

            for(int column = 1; column < this.columns + 1; column++) {

                set(row, column, null);
            }
        }
        return this;
    }

    public ClickableItem get(int row, int column) {

        if(row > this.rows) return null;

        if(column > this.columns) return null;

        return this.contents[row - 1][column - 1];
    }

    public ClickableItem get(int slot) {

        int row = slot / this.supported.getDefaultColumns(),
                column = slot % this.supported.getDefaultColumns();

        if(row > this.rows) return null;

        if(column > this.columns) return null;

        return this.contents[row][column];
    }

    public void fill(Inventory inv) {

        for(int row = 0; row < this.contents.length; row++) {

            for(int column = 0; column < this.contents[row].length; column++) {

                ClickableItem clickable = this.contents[row][column];

                if(clickable != null)
                    inv.setItem((9 * row) + column, clickable.getItemStack());
            }
        }
    }

    public boolean isEmpty(int row, int column) {
        return get(row, column) == null;
    }

    public boolean isEmpty(int slot) { return get(slot) == null; }

    public ClickableItem[][] getContents() {
        return contents;
    }
}
