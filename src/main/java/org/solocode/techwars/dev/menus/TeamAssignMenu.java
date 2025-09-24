package org.solocode.techwars.dev.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.solocode.menu.BaseMenu;
import org.solocode.menu.SimpleMenue.Rows;
import org.solocode.teams.Team;
import org.solocode.techwars.TechWars;

public class TeamAssignMenu extends BaseMenu {
    private final String targetPlayerName;
    private final Team currentTeam;

    public TeamAssignMenu(Player viewer, String targetPlayerName, Team currentTeam) {
        super(Rows.SIX, "§6Team Assignment: " + targetPlayerName);
        this.targetPlayerName = targetPlayerName;
        this.currentTeam = currentTeam;
        onSetItems();
    }

    @Override
    public void onSetItems() {
        int slot = 10;
        for (Team team : TechWars.getInstance().getTeamManager().getAllTeams().values()) {
            if (slot >= 34) break;
            if (slot % 9 == 7) slot += 3;
            
            boolean isCurrentTeam = currentTeam != null && currentTeam.getName().equals(team.getName());
            
            setItem(slot++, createItem(
                isCurrentTeam ? Material.LIME_WOOL : Material.WHITE_WOOL,
                "§e" + team.getName(),
                isCurrentTeam ? "§aCurrent Team" : "§7Click to assign",
                "§7Members: §f" + team.getMemberNames().size(),
                "§7Color: " + team.getColor()));
        }

        // Remove from team button
        if (currentTeam != null) {
            setItem(40, createItem(Material.BARRIER,
                "§cRemove from Team",
                "§7Click to remove player from their team"));
        }

        // Back button
        setItem(49, createItem(Material.ARROW,
            "§cBack",
            "§7Return to player management"));
    }

    @Override
    public void click(Player player, int slot) {
        if (slot == 49) {
            // Back button
            new PlayerManagementMenu(player, targetPlayerName, currentTeam).open(player);
            return;
        }

        if (slot == 40 && currentTeam != null) {
            // Remove from current team
            Player targetPlayer = player.getServer().getPlayer(targetPlayerName);
            if (targetPlayer != null) {
                currentTeam.removeMember(targetPlayer);
                // Save changes to persistent storage
                TechWars.getInstance().getTeamManager().saveTeamData();
                player.sendMessage("§aRemoved " + targetPlayerName + " from team " + currentTeam.getName());
                new PlayerManagementMenu(player, targetPlayerName, null).open(player);
            }
            return;
        }

        // Check if clicked on a team
        ItemStack clickedItem = getInventory().getItem(slot);
        if (clickedItem != null && (clickedItem.getType() == Material.LIME_WOOL || 
                                  clickedItem.getType() == Material.WHITE_WOOL)) {
            String teamName = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
                .plainText().serialize(clickedItem.getItemMeta().displayName())
                .substring(2); // Remove color code
            
            Team newTeam = TechWars.getInstance().getTeamManager().getTeam(teamName).orElse(null);
            if (newTeam != null) {
                Player targetPlayer = player.getServer().getPlayer(targetPlayerName);
                if (targetPlayer != null) {
                    // Remove from current team if any
                    if (currentTeam != null) {
                        currentTeam.removeMember(targetPlayer);
                    }
                    
                    // Add to new team
                    newTeam.addMember(targetPlayer);
                    // Save changes to persistent storage
                    TechWars.getInstance().getTeamManager().saveTeamData();
                    player.sendMessage("§aAssigned " + targetPlayerName + " to team " + newTeam.getName());
                    new PlayerManagementMenu(player, targetPlayerName, newTeam).open(player);
                }
            }
        }
    }
}