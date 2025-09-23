package org.solocode.techwars.dev.menus;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.solocode.menu.BaseMenu;
import org.solocode.menu.SimpleMenue.Rows;
import org.solocode.techwars.TechWars;
import org.solocode.teams.Team;
import org.solocode.techwars.TechTree.ResearchTreeLoader;

public class PluginDataMenu extends BaseMenu {
    private final TechWars plugin;

    public PluginDataMenu(Player viewer) {
        super(Rows.SIX, "§6Plugin Data");
        this.plugin = TechWars.getInstance();
        onSetItems();
    }

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

    private int getPlayerCount() {
        int count = 0;
        if (plugin.getConfig().getConfigurationSection("teams") != null) {
            for (String team : plugin.getConfig().getConfigurationSection("teams").getKeys(false)) {
                count += plugin.getConfig().getStringList("teams." + team + ".players").size();
            }
        }
        return count;
    }

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

    private String formatTimestamp(long timestamp) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault())
            .format(Instant.ofEpochMilli(timestamp));
    }
}