package org.solocode.techwars.dev;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.solocode.menu.BaseMenu;
import org.solocode.techwars.TechWars;
import org.solocode.teams.Team;

import java.util.HashMap;
import java.util.Map;

/**
 * Menu for managing teams in developer mode.
 * Allows developers to view available teams and select one for further management.
 */
public class TeamManagementMenu extends BaseMenu {
    private final Map<Integer, Team> teamSlots;

    /**
     * Constructs a new TeamManagementMenu.
     * @param player The player for whom this menu is created.
     */
    public TeamManagementMenu(Player player) {
        super(Rows.SIX, "§6Team Management");
        this.teamSlots = new HashMap<>();
        onSetItems();
    }

    /**
     * Sets up the items and their actions within the team management menu.
     * Displays a list of all available teams and a back button.
     */
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

    /**
     * Handles click events within the team management menu.
     * Allows selection of a team or navigating back to the developer menu.
     * @param player The player who clicked an item.
     * @param slot The slot that was clicked.
     */
    @Override
    public void click(Player player, int slot) {
        if (slot == 49) {
            new DeveloperMenu(player).open(player);
            return;
        }

        Team team = teamSlots.get(slot);
        if (team != null) {
            // TODO: Create TeamEditMenu class. This functionality is handled by TeamEditMenu now.
            player.sendMessage("§cTeam edit menu coming soon!");
        }
    }
}