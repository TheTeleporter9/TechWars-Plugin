package org.solocode.techwars.dev;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.solocode.menu.BaseMenu;
import org.solocode.menu.SimpleMenue.Rows;
import org.solocode.techwars.dev.menus.*;

/**
 * Developer menu for managing plugin data, teams, players, and research tree in-game.
 * Accessible only to players listed as developers in the plugin's configuration.
 */
public class DeveloperMenu extends BaseMenu {

    /**
     * Constructs a new DeveloperMenu.
     * @param player The player for whom this menu is created.
     */
    public DeveloperMenu(Player player) {
        super(Rows.SIX, "§6Developer Menu");
        onSetItems();
    }

    /**
     * Sets up the items and their actions within the developer menu.
     * This method is called when the menu is opened or refreshed.
     */
    @Override
    public void onSetItems() {
        // Server Data
        setItem(10, createItem(Material.CLOCK,
            "§eServer Data",
            "§7View server performance",
            "§7and statistics"));

        // Plugin Data
        setItem(12, createItem(Material.REDSTONE,
            "§ePlugin Data",
            "§7View plugin-specific",
            "§7information"));

        // Team Management
        setItem(14, createItem(Material.SHIELD,
            "§eTeam Management",
            "§7Manage teams and",
            "§7their members"));

        // Player Management
        setItem(16, createItem(Material.PLAYER_HEAD,
            "§ePlayer Management",
            "§7Manage individual",
            "§7player data"));

        // Research Management
        setItem(28, createItem(Material.ENCHANTED_BOOK,
            "§eResearch Management",
            "§7Manage team research",
            "§7progress and stages"));

        // Close Button
        setItem(49, createItem(Material.BARRIER, "§cClose", "§7Click to close"));
    }

    /**
     * Handles click events within the developer menu.
     * @param player The player who clicked an item.
     * @param slot The slot that was clicked.
     */
    @Override
    public void click(Player player, int slot) {
        switch(slot) {
            case 10:
                new org.solocode.techwars.dev.menus.ServerDataMenu(player).open(player);
                break;
            case 12:
                new org.solocode.techwars.dev.menus.PluginDataMenu(player).open(player);
                break;
            case 14:
                new org.solocode.techwars.dev.menus.TeamEditMenu(player, null).open(player);
                break;
            case 16:
                new org.solocode.techwars.dev.menus.PlayerManagementMenu(player, player.getName(), null).open(player);
                break;
            case 28:
                new org.solocode.techwars.dev.menus.ResearchTreeManagementMenu(player, null).open(player);
                break;
            case 49:
                player.closeInventory();
                break;
        }
    }
}