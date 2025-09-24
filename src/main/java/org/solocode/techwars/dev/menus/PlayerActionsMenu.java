package org.solocode.techwars.dev.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.solocode.menu.BaseMenu;
import org.solocode.teams.Team;

/**
 * A developer menu providing various actions for a specific target player.
 * This menu allows developers to kick a player from their team, kick them from the server,
 * or manage their inventory. It is typically accessed from the PlayerManagementMenu.
 */
public class PlayerActionsMenu extends BaseMenu {
    private final String targetPlayerName;
    private final Team team;

    /**
     * Constructs a new PlayerActionsMenu.
     * @param viewer The player who is viewing this menu.
     * @param targetPlayerName The name of the player on whom actions will be performed.
     * @param team The team of the target player, or null if the player is not in a team.
     */
    public PlayerActionsMenu(Player viewer, String targetPlayerName, Team team) {
        super(Rows.THREE, "§6Managing: " + targetPlayerName);
        this.targetPlayerName = targetPlayerName;
        this.team = team;
        onSetItems();
    }

    /**
     * Sets up the items and their actions within the player actions menu.
     * Displays options to kick the player from team/server or manage their inventory.
     */
    @Override
    public void onSetItems() {
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        boolean isOnline = targetPlayer != null;

        // Kick from Team
        setItem(11, createItem(Material.BARRIER,
            "§cKick from Team",
            "§7Remove player from their team",
            isOnline && team != null ? "§aAvailable" : "§cRequires team"));

        // Kick from Server
        setItem(13, createItem(Material.TNT,
            "§4Kick from Server",
            "§7Kick player from server",
            isOnline ? "§aAvailable" : "§cPlayer offline"));

        // Manage Inventory
        setItem(15, createItem(Material.CHEST,
            "§eManage Inventory",
            "§7View and modify inventory",
            isOnline ? "§aAvailable" : "§cPlayer offline"));

        // Back Button
        setItem(22, createItem(Material.BARRIER,
            "§cBack",
            "§7Return to player list"));
    }

    /**
     * Handles click events within the player actions menu.
     * Processes actions like kicking from team/server, managing inventory, or navigating back.
     * @param player The player who clicked an item.
     * @param slot The slot that was clicked.
     */
    @Override
    public void click(Player player, int slot) {
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

        switch(slot) {
            case 11: // Kick from Team
                if (targetPlayer != null && team != null) {
                    team.removeMember(targetPlayer);
                    targetPlayer.sendMessage("§cYou have been removed from team " + team.getName());
                    player.sendMessage("§aPlayer removed from team");
                }
                break;

            case 13: // Kick from Server
                if (targetPlayer != null) {
                    targetPlayer.kick(net.kyori.adventure.text.Component.text("Kicked by administrator"));
                    player.sendMessage("§aPlayer kicked from server");
                }
                break;

            case 15: // Inventory Management
                if (targetPlayer != null) {
                    new PlayerInventoryMenu(player, targetPlayer).open(player);
                } else {
                    player.sendMessage("§cPlayer must be online to manage inventory");
                }
                break;

            case 22: // Back
                new PlayerManagementMenu(player, null, null).open(player);
                break;
        }
    }
}