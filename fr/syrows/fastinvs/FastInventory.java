package fr.syrows.fastinvs;

import fr.syrows.fastinvs.contents.Pagination;
import fr.syrows.fastinvs.inventories.InventoryContents;
import fr.syrows.fastinvs.inventories.InventoryListener;
import fr.syrows.fastinvs.inventories.InventoryManager;
import fr.syrows.fastinvs.inventories.SupportedInventories;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastInventory {

    private String id, title;
    private InventoryType type;
    private int size;

    private boolean closeable;

    private Pagination pagination;
    private InventoryContents contents;
    private InventoryManager invManager;

    private List<InventoryListener> listeners;
    private List<Integer> interactiveSlots;

    public String getId() { return id; }

    public String getTitle() { return title; }

    public InventoryType getType() { return type; }

    public int getSize() { return size; }

    public boolean isCloseable() { return closeable; }

    public void setCloseable(boolean closeable) { this.closeable = closeable; }

    public Pagination getPagination() { return pagination; }

    public boolean hasPagination() { return this.pagination != null; }

    public InventoryContents getInventoryContents() { return contents; }

    public List<InventoryListener> getListeners() { return listeners; }

    public List<Integer> getInteractiveSlots() { return interactiveSlots; }

    public void setInteractiveSlots(int... slots) {

        Arrays.stream(slots)
                .filter(slot -> slot >= 0 && slot < getSize())
                .forEach(slot -> this.interactiveSlots.add(slot));
    }

    public void removeInteractiveSlots(int... slots) {

        Arrays.stream(slots)
                .filter(slot -> this.interactiveSlots.contains(slot))
                .forEach(slot -> this.interactiveSlots.remove(slot));
    }

    public boolean isSlotInteractive(int slot) { return this.interactiveSlots.contains(slot); }

    public boolean hasInteractiveSlots() { return this.interactiveSlots.size() != 0; }

    public int getRows() { return this.size / SupportedInventories.get(this.type).getDefaultColumns(); }

    public int getColumns() { return this.size / getRows(); }

    public void open(Player player) {
        this.invManager.open(player, this);
    }

    public void close(Player player) {
        this.invManager.close(player);
    }

    public static class InventoryBuilder {

        private String id, title;
        private InventoryType type;
        private int size;

        private Pagination pagination;

        private InventoryManager invManager;

        private List<InventoryListener> listeners;
        private List<Integer> interactiveSlots;

        public InventoryBuilder(InventoryManager invManager) {

            this.id = "unknown";
            this.title = "";
            this.type = InventoryType.CHEST;
            this.size = 54;

            this.listeners = new ArrayList<>();
            this.interactiveSlots = new ArrayList<>();

            if(invManager == null)
                throw new IllegalArgumentException("InventoryManage cannot be null.");

            this.invManager = invManager;
        }

        public InventoryBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public InventoryBuilder withTitle(String title) {

            if(title == null)
                throw new IllegalArgumentException("Title cannot be null.");

            if(title.length() > 32)
                throw new IllegalArgumentException("Title cannot exceed 32 characters.");

            this.title = title;
            return this;
        }

        public InventoryBuilder withSize(int size) {

            if(this.type == InventoryType.CHEST) {

                if(size < 0)
                    throw new IllegalArgumentException("Inventory size cannot be a negative number.");

                if(size > 54)
                    throw new IllegalArgumentException("Inventory size cannot exceed 54.");

                if(size % 9 != 0)
                    throw new IllegalArgumentException("Inventory size must be a multiple of 9.");

                this.size = size;
            }
            this.size = size;
            return this;
        }

        public InventoryBuilder withType(InventoryType type) {

            if(!SupportedInventories.isSupported(type))
                throw new IllegalArgumentException("This InventoryType is not supported.");

            this.type = type;
            return this;
        }

        public InventoryBuilder withPagination(Pagination pagination) {
            this.pagination = pagination;
            return this;
        }

        public InventoryBuilder withListeners(InventoryListener... listeners) {
            this.listeners.addAll(Arrays.asList(listeners));
            return this;
        }

        public InventoryBuilder withInteractiveSlots(int... slots) {
            for(int slot : slots) this.interactiveSlots.add(slot);
            return this;
        }

        public FastInventory build() {

            FastInventory inv = new FastInventory();

            inv.id = this.id;
            inv.title = this.title;
            inv.type = this.type;

            inv.size = this.type == InventoryType.CHEST ? this.size : this.type.getDefaultSize();

            inv.closeable = true;

            inv.listeners = this.listeners;
            inv.interactiveSlots = this.interactiveSlots;

            inv.pagination = this.pagination;

            inv.contents = new InventoryContents(inv);

            inv.invManager = this.invManager;

            return inv;
        }
    }
}
