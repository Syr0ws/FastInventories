package fr.syrows.fastinvs.inventories;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class InventoryListener<T> {

    private static List<Class> SUPPORTED_LISTENERS = Arrays.asList(InventoryClickEvent.class,
            InventoryDragEvent.class, InventoryOpenEvent.class, InventoryCloseEvent.class, PlayerQuitEvent.class, PluginDisableEvent.class);

    private Class<T> type;
    private Consumer<T> consumer;

    public InventoryListener(Class<T> type, Consumer<T> consumer) {

        if(!SUPPORTED_LISTENERS.contains(type))
            throw new IllegalArgumentException(String.format("Listener %s is not supported.", type.getName()));

        this.type = type;
        this.consumer = consumer;
    }

    public Class<T> getType() {
        return type;
    }

    public void accept(T event) {
        consumer.accept(event);
    }
}
