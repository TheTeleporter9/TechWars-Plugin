package org.solocode.techwars.dev.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.solocode.menu.BaseMenu;
import org.solocode.menu.SimpleMenue.Rows;
import org.solocode.teams.Team;
import org.bukkit.OfflinePlayer;
import org.solocode.techwars.TechWars;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A menu for developers to manage teams and their members. This menu allows for selection of a team
 * to view its members, or to create a new team. When a team is selected, it displays options to manage
 * individual members, view research progress, and access team settings.
 */
public class TeamEditMenu extends BaseMenu {
    private final Team team;
    private final Map<Integer, String> memberSlots = new HashMap<>();

    /**
     * Constructs a new TeamEditMenu.
     * If `team` is null, the menu will display a list of available teams for selection.
     * If `team` is provided, the menu will display options to manage members of that specific team.
     * @param viewer The player who is viewing this menu.
     * @param team The team to manage, or null to show team selection.
     */
    public TeamEditMenu(Player viewer, Team team) {
        super(Rows.SIX, "§6Team Management");
        this.team = team;
        onSetItems();
    }

    /**
     * Sets up the items in the menu based on whether a team is selected or not.
     * Displays team selection interface or team member management options.
     */
    @Override
    public void onSetItems() {
        if (team == null) {
            // Show team selection interface
            int slot = 10;
            var teamsSection = TechWars.getInstance().getConfig().getConfigurationSection("Teams");
            if (teamsSection == null) {
                setItem(22, createItem(Material.BARRIER, "§cNo Teams",
                    "§7No teams are configured",
                    "§7Click the button below to create one"));
                return;
            }
            
            Collection<String> teamNames = teamsSection.getKeys(false);
            for (String teamName : teamNames) {
                if (slot >= 34) break; // Max display limit
                
                // Skip slots to maintain centered grid layout
                if (slot % 9 == 7) slot += 3;
                
                memberSlots.put(slot, teamName);
                var members = TechWars.getInstance().getConfig()
                    .getStringList("Teams." + teamName);
                int memberCount = members.size();
                String displayHead = members.isEmpty() ? teamName : members.get(0);
                
                setItem(slot, createPlayerHead(displayHead,
                    "§e" + teamName,
                    "§7Members: " + memberCount,
                    "§7Leader: " + (members.isEmpty() ? "None" : members.get(0)),
                    "",
                    "§7Click to view team members"));
                slot++;
            }
            
            // Create New Team button
            setItem(40, createItem(Material.EMERALD,
                "§aCreate New Team",
                "§7Click to create a new team"));
        } else {
            // Show team member management
            int slot = 10;
            for (String memberName : team.getMemberNames()) {
                if (slot >= 34) break; // Max display limit

                // Skip slots to maintain centered grid layout
                if (slot % 9 == 7) slot += 3;
                
                memberSlots.put(slot, memberName);
                var members = team.getMemberNames();
                boolean isLeader = members.iterator().next().equals(memberName);
                
                setItem(slot, createPlayerHead(memberName, 
                    "§e" + memberName,
                    "§7Role: " + (isLeader ? "§6Leader" : "§7Member"),
                    "§7Team: §e" + team.getName(),
                    "",
                    "§7Click to manage player"));
                slot++;
            }

            // Add Member button
            setItem(38, createItem(Material.EMERALD,
                "§aAdd Member",
                "§7Click to add a new member"));

            // Research Progress Button
            setItem(40, createItem(Material.BOOK, 
                "§bResearch Progress", 
                "§7View and modify team research"));

            // Team Settings button
            setItem(42, createItem(Material.REDSTONE_TORCH,
                "§eTeam Settings",
                "§7Manage team configuration"));
        }
        
        // Back button
        setItem(49, createItem(Material.BARRIER, "§cBack",
            "§7Return to developer menu"));
    }

    /**
     * Handles click events within the team edit menu.
     * Processes team selection, creation, member management, and navigation.
     * @param player The player who clicked an item.
     * @param slot The slot that was clicked.
     */
    @Override
    public void click(Player player, int slot) {
        if (slot == 49) {
            new DeveloperMenu(player).open(player);
            return;
        }

        if (team == null) {
            // Team selection mode
            if (slot == 40) {
                // Create new team
                player.sendMessage("§cTeam creation coming soon!");
                return;
            }

            String teamName = memberSlots.get(slot);
            if (teamName != null) {
                var teamMembers = TechWars.getInstance().getConfig().getStringList("Teams." + teamName);
                if (!teamMembers.isEmpty()) {
                    Team selectedTeam = TechWars.getInstance().getTeamManager().getTeam(teamName).orElse(null);
                    if (selectedTeam != null) {
                        new TeamEditMenu(player, selectedTeam).open(player);
                    } else {
                        player.sendMessage("§cTeam not found in TeamManager!");
                    }
                } else {
                    player.sendMessage("§cTeam has no members!");
                }
            }
        } else {
            // Team management mode
            if (slot == 40) {
                new ResearchProgressMenu(player, team).open(player);
                return;
            }

            if (slot == 38) {
                // Add member
                player.sendMessage("§cMember addition coming soon!");
                return;
            }

            if (slot == 42) {
                // Team settings
                player.sendMessage("§cTeam settings coming soon!");
                return;
            }

            String memberName = memberSlots.get(slot);
            if (memberName != null) {
                new PlayerManagementMenu(player, memberName, team).open(player);
            }
        }
    }

    /**
     * Creates an ItemStack representing a player head.
     * @param playerName The name of the player whose head to display.
     * @param displayName The display name of the item.
     * @param lore Additional lore lines for the item.
     * @return An ItemStack of a player head.
     */
    private ItemStack createPlayerHead(String playerName, String displayName, String... lore) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.displayName(net.kyori.adventure.text.Component.text(displayName));
            meta.lore(java.util.Arrays.stream(lore)
                    .map(line -> net.kyori.adventure.text.Component.text(line))
                    .toList());
            
            // Try to find the player (online or offline)
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(playerName);
            if (offlinePlayer != null) {
                meta.setOwningPlayer(offlinePlayer);
            }
            
            head.setItemMeta(meta);
        }
        return head;
    }
}