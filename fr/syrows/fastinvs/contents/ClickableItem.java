package fr.syrows.fastinvs.contents;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ClickableItem {

    private String id;
    private ItemStack stack;
    private Consumer<InventoryClickEvent> consumer;

    public ClickableItem(String id, ItemStack stack, Consumer<InventoryClickEvent> consumer) {
        this.id = id != null ? id : "unknown";
        this.stack = stack;
        this.consumer = consumer;
    }

    public ClickableItem(String id, ItemStack stack) {
        this(id, stack, null);
    }

    public ClickableItem(ItemStack stack, Consumer<InventoryClickEvent> consumer) {
        this("unknown", stack, consumer);
    }

    public ClickableItem(ItemStack stack) {
        this("unknown", stack, null);
    }

    public String getId() { return id; }

    public ItemStack getItemStack() { return stack; }

    public boolean hasConsumer() { return this.consumer != null; }

    public void accept(InventoryClickEvent event) { if(hasConsumer()) this.consumer.accept(event); }
}
