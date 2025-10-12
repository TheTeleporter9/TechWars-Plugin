package org.solocode.techwars.TechTree;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.solocode.menu.PagedMenu;
import java.util.function.Consumer;

/**
 * Example implementation of PagedMenu
 * This demonstrates how to create a multi-page menu with different pages
 */
public class MyPagedMenu extends PagedMenu {

    public MyPagedMenu() {
        // Initialize with 6 rows and a title
        super(Rows.SIX, "Tech Tree Menu");

        // Setup all pages when the menu is created
        setupPages();

        // IMPORTANT: Switch to a default page
        switchToPage("main");
    }

    /**
     * Setup all pages with their respective items
     */
    private void setupPages() {
        setupMainPage();
        setupWeaponsPage();
        setupArmorPage();
    }

    /**
     * Setup the main page with navigation options
     */
    private void setupMainPage() {
        // Create navigation items
        ItemStack weaponsItem = createItem(Material.DIAMOND_SWORD, "§6Weapons Tech", "§7Click to view weapon technologies");
        ItemStack armorItem = createItem(Material.DIAMOND_CHESTPLATE, "§9Armor Tech", "§7Click to view armor technologies");
        ItemStack infoItem = createItem(Material.BOOK, "§aInfo", "§7Welcome to the Tech Tree!");

        // Add items to main page
        setItem("main", 10, weaponsItem, player -> {
            switchToPage("weapons");
            player.sendMessage("§6Switched to Weapons page!");
        });

        setItem("main", 12, armorItem, player -> {
            switchToPage("armor");
            player.sendMessage("§9Switched to Armor page!");
        });

        setItem("main", 14, infoItem, player -> {
            player.sendMessage("§aWelcome to the Tech Tree system!");
        });
    }

    /**
     * Setup the weapons page
     */
    private void setupWeaponsPage() {
        // Back button
        ItemStack backItem = createItem(Material.ARROW, "§cBack", "§7Return to main page");
        setItem("weapons", 45, backItem, player -> {
            switchToPage("main");
            player.sendMessage("§7Returned to main page");
        });

        // Weapon items
        ItemStack basicSword = createItem(Material.IRON_SWORD, "§7Basic Sword Tech", "§7Unlocks basic sword crafting");
        ItemStack advancedSword = createItem(Material.DIAMOND_SWORD, "§6Advanced Sword Tech", "§7Unlocks diamond sword crafting");

        setItem("weapons", 10, basicSword, player -> {
            player.sendMessage("§7You researched Basic Sword Technology!");
        });

        setItem("weapons", 11, advancedSword, player -> {
            player.sendMessage("§6You researched Advanced Sword Technology!");
        });
    }

    /**
     * Setup the armor page
     */
    private void setupArmorPage() {
        // Back button
        ItemStack backItem = createItem(Material.ARROW, "§cBack", "§7Return to main page");
        setItem("armor", 45, backItem, player -> {
            switchToPage("main");
            player.sendMessage("§7Returned to main page");
        });

        // Armor items
        ItemStack basicArmor = createItem(Material.IRON_CHESTPLATE, "§7Basic Armor Tech", "§7Unlocks basic armor crafting");
        ItemStack advancedArmor = createItem(Material.DIAMOND_CHESTPLATE, "§9Advanced Armor Tech", "§7Unlocks diamond armor crafting");

        setItem("armor", 10, basicArmor, player -> {
            player.sendMessage("§7You researched Basic Armor Technology!");
        });

        setItem("armor", 11, advancedArmor, player -> {
            player.sendMessage("§9You researched Advanced Armor Technology!");
        });
    }

    /**
     * Helper method to create items with display name and lore
     */
    private ItemStack createItem(Material material, String displayName, String lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(java.util.Arrays.asList(lore));
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Implementation of the abstract method from PagedMenu
     * This method should register click actions based on your SimpleMenu implementation
     */
    @Override
    protected void setItemClickAction(int slot, Consumer<Player> action) {
        // This depends on how your SimpleMenu handles click actions
        // You might need to store these in a map and handle them in your click event

        // Example implementation (adapt based on your SimpleMenu):
        // clickActions.put(slot, action);

        // Or if SimpleMenu has a method like:
        // super.setClickAction(slot, action);

        // For now, this is a placeholder - you'll need to implement this
        // based on your SimpleMenu's click handling system
        System.out.println("Setting click action for slot " + slot);
    }
}