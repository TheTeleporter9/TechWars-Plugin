package org.solocode.techwars.dev.menus;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.solocode.menu.BaseMenu;
import org.solocode.techwars.TechWars;
import org.solocode.techwars.TechTree.ResearchTreeLoader;

/**
 * A developer menu for displaying various plugin-specific data and statistics.
 * This includes plugin version, team statistics, research data overview, and configuration file info.
 * It also provides navigation to other developer menus.
 */
public class PluginDataMenu extends BaseMenu {
    private final TechWars plugin;

    /**
     * Constructs a new PluginDataMenu.
     * @param viewer The player who is viewing this menu.
     */
    public PluginDataMenu(Player viewer) {
        super(Rows.SIX, "§6Plugin Data");
        this.plugin = TechWars.getInstance();
        onSetItems();
    }

    /**
     * Sets up the items and their actions within the plugin data menu.
     * Displays various plugin statistics and navigation buttons.
     */
    @Override
    public void onSetItems() {
        // Plugin Status
        PluginDescriptionFile desc = plugin.getServer().getPluginManager().getPlugin("TechWars").getDescription();
        setItem(13, createItem(Material.REDSTONE_TORCH,
            "§ePlugin Status",
            "§7Version: " + desc.getVersion(),
            "§7Author: " + String.join(", ", desc.getAuthors())));

        // Team Data
        int teamCount = 0;
        if (plugin.getConfig().getConfigurationSection("teams") != null) {
            teamCount = plugin.getConfig().getConfigurationSection("teams").getKeys(false).size();
        }
        setItem(22, createItem(Material.SHIELD,
            "§eTeam Statistics",
            "§7Total Teams: " + teamCount,
            "§7Total Players: " + getPlayerCount()));

        // Research Data
        var researchSection = plugin.getConfig().getConfigurationSection("research");
        int researchCategories = researchSection != null ? researchSection.getKeys(false).size() : 0;
        setItem(31, createItem(Material.BOOKSHELF,
            "§eResearch Data",
            "§7Available Technologies: " + countTechnologies(),
            "§7Research Categories: " + researchCategories));

        // File Data
        setItem(40, createItem(Material.PAPER,
            "§eConfiguration",
            "§7Config Version: " + plugin.getConfig().getString("version", "N/A"),
            "§7Last Save: " + formatTimestamp(System.currentTimeMillis())));

        // Navigation
        setItem(48, createItem(Material.CLOCK,
            "§cServer Data",
            "§7View server statistics"));

        setItem(50, createItem(Material.BARRIER,
            "§cBack",
            "§7Return to developer menu"));
    }

    /**
     * Handles click events within the plugin data menu.
     * Navigates to other developer menus based on the clicked slot.
     * @param player The player who clicked an item.
     * @param slot The slot that was clicked.
     */
    @Override
    public void click(Player player, int slot) {
        switch (slot) {
            case 48:
                new ServerDataMenu(player).open(player);
                break;
            case 50:
                new DeveloperMenu(player).open(player);
                break;
        }
    }

    /**
     * Counts the total number of players across all configured teams.
     * @return The total number of players.
     */
    private int getPlayerCount() {
        int count = 0;
        if (plugin.getConfig().getConfigurationSection("teams") != null) {
            for (String team : plugin.getConfig().getConfigurationSection("teams").getKeys(false)) {
                count += plugin.getConfig().getStringList("teams." + team + ".players").size();
            }
        }
        return count;
    }

    /**
     * Counts the total number of technologies configured in the research tree.
     * @return The total count of technologies.
     */
    private int countTechnologies() {
        int count = 0;
        var researchSection = plugin.getConfig().getConfigurationSection("research");
        if (researchSection == null) {
            return 0;
        }
        
        for (String category : researchSection.getKeys(false)) {
            var categorySection = plugin.getConfig().getConfigurationSection("research." + category);
            if (categorySection != null) {
                count += categorySection.getKeys(false).size();
            }
        }
        return count;
    }

    /**
     * Formats a given timestamp into a readable string.
     * @param timestamp The timestamp in milliseconds.
     * @return A formatted date and time string.
     */
    private String formatTimestamp(long timestamp) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault())
            .format(Instant.ofEpochMilli(timestamp));
    }
}