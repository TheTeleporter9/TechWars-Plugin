package org.solocode.techwars.dev.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.solocode.menu.BaseMenu;
import org.solocode.menu.SimpleMenue.Rows;
import org.solocode.teams.Team;

public class DeveloperMenu extends BaseMenu {
    
    public DeveloperMenu(Player viewer) {
        super(Rows.THREE, "§6Developer Menu");
        onSetItems();
    }

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
    }

    @Override
    public void click(Player player, int slot) {
        switch (slot) {
            case 10:
                new ServerDataMenu(player).open(player);
                break;
            case 12:
                new PluginDataMenu(player).open(player);
                break;
            case 14:
                new TeamEditMenu(player, null).open(player);
                break;
            case 16:
                new PlayerManagementMenu(player, player.getName(), null).open(player);
                break;
        }
    }
}