package org.solocode.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * Menu interface for both SimpleMenu and PagedMenu.
 * Supports per-item actions and optional page-specific operations.
 */
public interface Menu extends InventoryHolder {

    /**
     * Called when a player clicks a slot.
     */
    void click(Player player, int slot);

    /**
     * Set an item in the current page (or for simple menu, the only page).
     */
    void setItem(int slot, ItemStack item);

    /**
     * Set an item with a click action in the current page.
     */
    void setItem(int slot, ItemStack item, Consumer<Player> action);

    /**
     * Set an item with optional click action on a specific page.
     * Required by interface, but logic is handled in concrete implementations.
     */
    void setItem(String pageName, int slot, ItemStack item, Consumer<Player> action);

    /**
     * Populate or refresh the menu contents.
     */
    void onSetItems();

    /**
     * Opens the menu for a player.
     */
    default void open(Player player) {
        onSetItems();
        player.openInventory(getInventory());
    }
}