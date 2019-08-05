package fr.syrows.fastinvs.inventories;

import fr.syrows.fastinvs.FastInventory;
import fr.syrows.fastinvs.FastInvsAPI;
import fr.syrows.fastinvs.contents.ClickableItem;
import fr.syrows.fastinvs.contents.Pagination;
import fr.syrows.fastinvs.inventories.openers.CommonInventoryOpener;
import fr.syrows.fastinvs.inventories.openers.InventoryOpener;
import fr.syrows.fastinvs.inventories.openers.SpecialInventoryOpener;
import fr.syrows.fastinvs.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.*;
import java.util.logging.Level;

public class InventoryManager {

    private final List<InventoryOpener> openers;

    private Map<Player, FastInventory> inventories = new HashMap<>();

    public InventoryManager(Plugin plugin) {

        this.openers = Arrays.asList(new CommonInventoryOpener(),
                new SpecialInventoryOpener());

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new InventoryListeners(), plugin);
    }

    public void open(Player player, FastInventory inv) {

        if(hasOpenedInventory(player)) close(player);

        findOpener(inv).open(player, inv);

        setInventory(player, inv);

        Logger.log(Level.INFO, String.format("Opening inventory %s for %s.", inv.getId(), player.getName()));
    }

    public void close(Player player) {

        FastInventory inv = getOpenedInventory(player);

        setInventory(player, null);

        player.closeInventory();

        Logger.log(Level.INFO, String.format("Closing inventory %s for %s.", inv.getId(), player.getName()));
    }

    public void update(Player player) {

        if(!hasOpenedInventory(player)) return;

        FastInventory inv = getOpenedInventory(player);
        Inventory handle = player.getOpenInventory().getTopInventory();

        for(int i = 0; i < handle.getSize(); i++) {

            ClickableItem clickable = inv.getContents().get(i);
            ItemStack stack = handle.getItem(i);

            if(clickable == null) {
                handle.setItem(i, null);
                continue;
            }

            if(stack != clickable.getItemStack())
                handle.setItem(i, clickable.getItemStack());
        }
        player.updateInventory();

        Logger.log(Level.INFO, String.format("Inventory %s updated for %s.", inv.getId(), player.getName()));
    }

    public boolean hasOpenedInventory(Player player) {
        return this.inventories.containsKey(player);
    }

    public FastInventory getOpenedInventory(Player player) {
        return this.inventories.getOrDefault(player, null);
    }

    public List<Player> getPlayersWithAnOpenedInventory() { return new ArrayList<>(this.inventories.keySet()); }

    private InventoryOpener findOpener(FastInventory inv) {

        SupportedInventories supported = SupportedInventories.get(inv.getType());

        Optional<InventoryOpener> inventoryOpener = this.openers
                .stream()
                .filter(opener -> opener.support(supported))
                .findAny();

        if(!inventoryOpener.isPresent())
            throw new NullPointerException(String.format("Cannot find an opener for the type %s.", supported.name()));

        return inventoryOpener.get();
    }

    private void setInventory(Player player, FastInventory inv) {
        if(inv == null) this.inventories.remove(player);
        else this.inventories.put(player, inv);
    }

    private class InventoryListeners implements Listener {

        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {

            Player player = (Player) event.getWhoClicked();

            if(!hasOpenedInventory(player)) return;

            ItemStack item = event.getCurrentItem();

            if(item == null) return;

            FastInventory inv = getOpenedInventory(player);

            int slot = event.getSlot();

            InventoryAction action = event.getAction();

            List<InventoryAction> actions = Arrays.asList(InventoryAction.MOVE_TO_OTHER_INVENTORY,
                    InventoryAction.NOTHING, InventoryAction.UNKNOWN);

            if(!inv.hasInteractiveSlots() && actions.contains(action)) {
                event.setCancelled(true);
                return;
            }

            if(inv.hasInteractiveSlots() && (action == InventoryAction.COLLECT_TO_CURSOR ||
                    action == InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                event.setCancelled(true);
                return;
            }

            if(event.getClickedInventory() == player.getOpenInventory().getTopInventory()) {

                if(!inv.isSlotInteractive(slot)) event.setCancelled(true);

                inv.getListeners().stream()
                        .filter(listener -> listener.getType() == InventoryClickEvent.class)
                        .forEach(listener -> listener.accept(event));

                ClickableItem clickable = inv.getContents().get(slot);

                if(clickable == null) return;

                if(clickable.hasConsumer()) clickable.accept(event);

                if(!inv.hasPagination()) return;

                Pagination pagination = inv.getPagination();

                if(clickable.getId().equals("previousPage")) {

                    if(!pagination.hasPrevious()) return;

                    pagination.previous();

                    pagination.setPageContents(inv);
                    pagination.setPages(inv);

                    update(player);

                } else if(clickable.getId().equals("nextPage")) {

                    if(!pagination.hasNext()) return;

                    pagination.next();

                    pagination.setPageContents(inv);
                    pagination.setPages(inv);

                    update(player);
                }
            }

            if(event.getClickedInventory() == player.getInventory())
                event.setCancelled(action == InventoryAction.COLLECT_TO_CURSOR || action == InventoryAction.MOVE_TO_OTHER_INVENTORY);
        }

        @EventHandler
        public void onInventoryDrag(InventoryDragEvent event) {

            Player player = (Player) event.getView().getPlayer();

            if(!hasOpenedInventory(player)) return;

            FastInventory inv = getOpenedInventory(player);

            inv.getListeners().stream()
                    .filter(listener -> listener.getType() == InventoryDragEvent.class)
                    .forEach(listener -> listener.accept(event));

            event.setCancelled(true);
        }

        @EventHandler
        public void onInventoryOpen(InventoryOpenEvent event) {

            Player player = (Player) event.getPlayer();

            if(!hasOpenedInventory(player)) return;

            FastInventory inv = getOpenedInventory(player);

            inv.getListeners().stream()
                    .filter(listener -> listener.getType() == InventoryOpenEvent.class)
                    .forEach(listener -> listener.accept(event));
        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent event) {

            Player player = (Player) event.getPlayer();

            if(!hasOpenedInventory(player)) return;

            FastInventory inv = getOpenedInventory(player);

            inv.getListeners().stream()
                    .filter(listener -> listener.getType() == InventoryCloseEvent.class)
                    .forEach(listener -> listener.accept(event));

            setInventory(player, null);

            if(!inv.isCloseable())
                Bukkit.getScheduler().runTaskLater(FastInvsAPI.getPlugin(), () -> inv.open(player), 1L);
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {

            Player player = event.getPlayer();

            if(hasOpenedInventory(player)) close(player);
        }

        @EventHandler
        public void onPluginDisable(PluginDisableEvent event) {

            if(event.getPlugin() == FastInvsAPI.getPlugin())
                getPlayersWithAnOpenedInventory().forEach(InventoryManager.this::close);
        }
    }
}
