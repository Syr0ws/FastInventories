package fr.syrows.fastinvs.inventories;

import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class InventoryListener {

    public static final List<Class> SUPPORTED_TYPES = Arrays.asList(InventoryClickEvent.class,
            InventoryOpenEvent.class, InventoryCloseEvent.class, InventoryDragEvent.class);

    private Class type;
    private Consumer<Event> consumer;

    public InventoryListener(Class type, Consumer<Event> consumer) {
        this.type = type;
        this.consumer = consumer;
    }

    public Class getType() {
        return type;
    }

    public Consumer<Event> getConsumer() {
        return consumer;
    }

    public void accept(Event event) {
        consumer.accept(event);
    }
}
