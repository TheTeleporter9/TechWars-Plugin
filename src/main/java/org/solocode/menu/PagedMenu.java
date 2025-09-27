package org.solocode.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * PagedMenu - A multi-page menu system for Minecraft plugins
 *
 * This abstract class extends SimpleMenu to provide functionality for creating
 * menus with multiple pages that can be navigated by players. Each page can
 * contain different items with their own click actions.
 *
 * Key Features:
 * - Multiple named pages support
 * - Individual item actions per page
 * - Easy page switching mechanism
 * - Automatic item loading based on current page
 *
 * Usage Example:
 * ```java
 * public class MyPagedMenu extends PagedMenu {
 *     public MyPagedMenu() {
 *         super(Rows.SIX, "My Multi-Page Menu");
 *         setupPages();
 *     }
 *
 *     private void setupPages() {
 *         // Add items to "page1"
 *         setItem("page1", 0, new ItemStack(Material.DIAMOND), player -> {
 *             player.sendMessage("Clicked diamond!");
 *         });
 *
 *         // Switch to page1 initially
 *         switchToPage("page1");
 *     }
 * }
 * ```
 *
 * @author SoloCode
 * @since 1.0
 */
public abstract class PagedMenu extends SimpleMenu {

    /**
     * Storage structure for all pages and their items
     * Structure: Map<PageName, Map<Slot, Map<ItemStack, Action>>>
     *
     * This nested map structure allows for:
     * - Multiple pages (outer key: String pageName)
     * - Multiple slots per page (middle key: Integer slot)
     * - Item and action mapping per slot (inner key: ItemStack, value: Consumer<Player>)
     */
    private Map<String, Map<Integer, Map<ItemStack, Consumer<Player>>>> SAVED_PAGES = new HashMap<>();

    /**
     * The currently active page being displayed to the player
     * Empty string indicates no page is currently active
     */
    private String currentPage = "";

    /**
     * Constructor for PagedMenu
     *
     * @param rows The number of rows for the inventory (use Rows enum)
     * @param title The title displayed at the top of the inventory
     */
    public PagedMenu(Rows rows, String title) {
        super(rows, title);
    }

    /**
     * Adds an item with an action to a specific page and slot
     *
     * This method allows you to define what items appear on each page
     * and what happens when a player clicks on them.
     *
     * @param pageName The name/identifier of the page (e.g., "main", "weapons", "armor")
     * @param slot The inventory slot position (0-53 for a 6-row inventory)
     * @param item The ItemStack to display in the slot
     * @param action The action to execute when the item is clicked (Consumer<Player>)
     *
     * Example:
     * ```java
     * setItem("weapons", 0, swordItem, player -> {
     *     player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
     *     player.sendMessage("You received a sword!");
     * });
     * ```
     */
    public void setItem(String pageName, int slot, ItemStack item, Consumer<Player> action) {
        SAVED_PAGES
                .computeIfAbsent(pageName, key -> new HashMap<>())
                .computeIfAbsent(slot, key -> new HashMap<>())
                .put(item, action);
    }

    /**
     * Switches the menu to display a specific page
     *
     * This method changes the current page and triggers a refresh
     * of the inventory to show the items from the new page.
     *
     * @param pageName The name of the page to switch to
     * @return true if the page exists and switch was successful, false otherwise
     *
     * Example:
     * ```java
     * if (switchToPage("armor")) {
     *     player.sendMessage("Switched to armor page!");
     * } else {
     *     player.sendMessage("Armor page not found!");
     * }
     * ```
     */
    public boolean switchToPage(String pageName) {
        if (SAVED_PAGES.containsKey(pageName)) {
            this.currentPage = pageName;
            refreshInventory();
            return true;
        }
        return false;
    }

    /**
     * Gets the name of the currently active page
     *
     * @return The current page name, or empty string if no page is active
     */
    public String getCurrentPage() {
        return currentPage;
    }

    /**
     * Checks if a page with the given name exists
     *
     * @param pageName The name of the page to check
     * @return true if the page exists, false otherwise
     */
    public boolean hasPage(String pageName) {
        return SAVED_PAGES.containsKey(pageName);
    }

    /**
     * Removes a page and all its items
     *
     * @param pageName The name of the page to remove
     * @return true if the page was removed, false if it didn't exist
     */
    public boolean removePage(String pageName) {
        return SAVED_PAGES.remove(pageName) != null;
    }

    /**
     * Gets the total number of pages in this menu
     *
     * @return The number of pages
     */
    public int getPageCount() {
        return SAVED_PAGES.size();
    }

    /**
     * Clears the current inventory and refreshes it with items from the current page
     * This method should be called after switching pages or modifying page content
     */
    private void refreshInventory() {
        // Clear current inventory
        getInventory().clear();

        // Load items from current page
        onSetItems();
    }

    /**
     * Override of SimpleMenu's onSetItems method
     *
     * This method is automatically called when the inventory needs to be populated.
     * It loads items from the currently active page into the inventory.
     *
     * The method iterates through the current page's items and sets them
     * in their respective slots with their associated click actions.
     */
    @Override
    public void onSetItems() {
        // Only load items from the current page
        if (currentPage.isEmpty() || !SAVED_PAGES.containsKey(currentPage)) {
            return;
        }

        Map<Integer, Map<ItemStack, Consumer<Player>>> currentPageItems = SAVED_PAGES.get(currentPage);

        for (Map.Entry<Integer, Map<ItemStack, Consumer<Player>>> slotEntry : currentPageItems.entrySet()) {
            int slot = slotEntry.getKey();
            Map<ItemStack, Consumer<Player>> items = slotEntry.getValue();

            for (Map.Entry<ItemStack, Consumer<Player>> itemEntry : items.entrySet()) {
                ItemStack item = itemEntry.getKey();
                Consumer<Player> action = itemEntry.getValue();

                // Set the item in the inventory at the specified slot
                getInventory().setItem(slot, item);

                // Register the click action for this item
                // Note: This assumes SimpleMenu has a method to register click actions
                // You may need to adapt this based on your SimpleMenu implementation
                setItemClickAction(slot, action);
            }
        }
    }

    /**
     * Abstract method to register click actions for items
     * This method should be implemented based on your SimpleMenu's click handling system
     *
     * @param slot The slot where the item is placed
     * @param action The action to execute when the item is clicked
     */
    protected abstract void setItemClickAction(int slot, Consumer<Player> action);
}