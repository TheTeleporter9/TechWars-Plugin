package org.solocode.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * PagedMenu:
 * - Custom slot placement per page
 * - Per-item click actions per page
 * - Next/Previous buttons automatically
 */
public abstract class PagedMenu extends SimpleMenu {

    /** Items per page: pageNumber -> (slot -> ItemStack) */
    private final Map<Integer, Map<Integer, ItemStack>> allItems = new HashMap<>();

    /** Actions per page: pageNumber -> (slot -> action) */
    private final Map<Integer, Map<Integer, Consumer<Player>>> actionsByPage = new HashMap<>();

    /** Current page displayed */
    private int currentPage = 0;

    /** Number of slots usable for items per page */
    private final int itemsPerPage;

    public PagedMenu(Rows rows, String title, int itemsPerPage) {
        super(rows, title);
        this.itemsPerPage = itemsPerPage;
    }

    /**
     * Set an item and optional action for a specific page and slot.
     *
     * @param page   the page number (0-based)
     * @param slot   the inventory slot
     * @param item   the ItemStack to display
     * @param action the click action (nullable)
     */
    public void setItem(int page, int slot, ItemStack item, Consumer<Player> action) {
        // Store the item
        allItems.computeIfAbsent(page, k -> new HashMap<>()).put(slot, item);

        // Store the action if it exists
        if (action != null) {
            actionsByPage.computeIfAbsent(page, k -> new HashMap<>()).put(slot, action);
        }
    }

    @Override
    public void onSetItems() {
        getInventory().clear();
        this.actions.clear();

        Map<Integer, ItemStack> pageItems = allItems.getOrDefault(currentPage, new HashMap<>());
        Map<Integer, Consumer<Player>> pageActions = actionsByPage.getOrDefault(currentPage, new HashMap<>());

        // Place items and assign their actions
        for (Map.Entry<Integer, ItemStack> entry : pageItems.entrySet()) {
            int slot = entry.getKey();
            ItemStack item = entry.getValue();
            getInventory().setItem(slot, item);

            if (pageActions.containsKey(slot)) {
                actions.put(slot, pageActions.get(slot));
            }
        }

        // Previous page button
        if (currentPage > 0) {
            int prevSlot = getInventory().getSize() - 9; // example slot
            getInventory().setItem(prevSlot, createPrevItem());
            actions.put(prevSlot, player -> {
                currentPage--;
                open(player);
            });
        }

        // Next page button
        if (allItems.containsKey(currentPage + 1)) {
            int nextSlot = getInventory().getSize() - 1; // example slot
            getInventory().setItem(nextSlot, createNextItem());
            actions.put(nextSlot, player -> {
                currentPage++;
                open(player);
            });
        }
    }

    /** Icon for Next Page button (subclass defines) */
    protected abstract ItemStack createNextItem();

    /** Icon for Previous Page button (subclass defines) */
    protected abstract ItemStack createPrevItem();
}
