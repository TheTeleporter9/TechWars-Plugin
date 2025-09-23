package org.solocode.techwars.dev;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.solocode.menu.BaseMenu;
import org.solocode.menu.SimpleMenue.Rows;
import org.solocode.techwars.TechWars;

/**
 * Developer menu for managing teams and plugin data
 */
public class DeveloperMenu extends BaseMenu {

    public DeveloperMenu(Player player) {
        super(Rows.SIX, "§6Developer Menu");
        onSetItems();
    }

    @Override
    public void onSetItems() {
        // Team Management Section
        setItem(10, createItem(Material.BOOK, "§eTeam Management", "§7Click to manage teams"));

        // Data Management Section
        setItem(12, createItem(Material.COMMAND_BLOCK, "§bData Management", "§7Click to manage team data"));

        // Reload Config
        setItem(14, createItem(Material.REDSTONE, "§cReload Config", "§7Click to reload the plugin configuration"));

        // Close Button
        setItem(49, createItem(Material.BARRIER, "§cClose", "§7Click to close the menu"));
    }


    @Override
    public void click(Player player, int slot) {
        switch(slot) {
            case 10:
                new TeamManagementMenu(player).open(player);
                break;
            case 12:
                // TODO: Implement DataManagementMenu
                player.sendMessage("§cData management coming soon!");
                break;
            case 14:
                TechWars.getInstance().reloadConfig();
                TechWars.getInstance().getTeamManager().loadTeams();
                player.sendMessage("§aConfiguration reloaded!");
                break;
            case 49:
                player.closeInventory();
                break;
        }
    }
}