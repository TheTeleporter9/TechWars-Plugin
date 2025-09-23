package org.solocode.techwars.dev;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.solocode.menu.BaseMenu;
import org.solocode.menu.SimpleMenue.Rows;
import org.solocode.techwars.TechWars;
import org.solocode.techwars.teams.Team;

import java.util.HashMap;
import java.util.Map;

/**
 * Menu for managing teams in developer mode
 */
public class TeamManagementMenu extends BaseMenu {
    private final Map<Integer, Team> teamSlots;

    public TeamManagementMenu(Player player) {
        super(Rows.SIX, "§6Team Management");
        this.teamSlots = new HashMap<>();
        onSetItems();
    }

    @Override
    public void onSetItems() {
        int slot = 0;
        for (Team team : TechWars.getInstance().getTeamManager().getAllTeams().values()) {
            if (slot >= 45) break; // Max 45 teams displayed
            
            teamSlots.put(slot, team);
            ItemStack item = createItem(Material.CHEST,
                "§e" + team.getName(),
                "§7Click to manage this team",
                "§7Members: " + team.getMemberNames().size());
            setItem(slot, item);
            slot++;
        }

        // Back button
        ItemStack backButton = createItem(Material.BARRIER, "§cBack", "§7Return to developer menu");
        setItem(49, backButton);
    }

    @Override
    public void click(Player player, int slot) {
        if (slot == 49) {
            new DeveloperMenu(player).open(player);
            return;
        }

        Team team = teamSlots.get(slot);
        if (team != null) {
            // TODO: Create TeamEditMenu class
            player.sendMessage("§cTeam edit menu coming soon!");
        }
    }
}