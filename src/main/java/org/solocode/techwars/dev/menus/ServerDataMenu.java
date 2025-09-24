package org.solocode.techwars.dev.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.solocode.menu.BaseMenu;
import org.solocode.techwars.dev.menus.DeveloperMenu;

/**
 * A developer menu for displaying various server performance and data statistics.
 * This includes TPS, memory usage, world data, and online player count.
 * It also provides navigation to other developer menus.
 */
public class ServerDataMenu extends BaseMenu {
    
    /**
     * Constructs a new ServerDataMenu.
     * @param viewer The player who is viewing this menu.
     */
    public ServerDataMenu(Player viewer) {
        super(Rows.SIX, "§6Server Data");
        onSetItems();
    }

    /**
     * Sets up the items and their actions within the server data menu.
     * Displays server performance metrics and world-specific information.
     */
    @Override
    public void onSetItems() {
        // Performance Data (centered top)
        double tps = Bukkit.getTPS()[0];
        setItem(13, createItem(Material.CLOCK,
            "§eTPS: " + formatTPS(tps),
            "§7Current: " + String.format("%.2f", tps),
            "§7Last 5m: " + String.format("%.2f", Bukkit.getTPS()[1]),
            "§7Last 15m: " + String.format("%.2f", Bukkit.getTPS()[2])));

        // Memory Usage
        long max = Runtime.getRuntime().maxMemory() / 1048576L;
        long used = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576L;
        setItem(22, createItem(Material.COMPARATOR,
            "§eMemory Usage",
            "§7Used: " + used + "MB",
            "§7Max: " + max + "MB",
            "§7Usage: " + String.format("%.1f", (used * 100.0 / max)) + "%"));

        // World Data
        int slot = 28;
        for (World world : Bukkit.getWorlds()) {
            if (slot >= 35) break;
            setItem(slot++, createItem(Material.GRASS_BLOCK,
                "§e" + world.getName(),
                "§7Loaded Chunks: " + world.getLoadedChunks().length,
                "§7Entities: " + world.getEntities().size(),
                "§7Players: " + world.getPlayers().size()));
        }

        // Online Players
        setItem(31, createItem(Material.PLAYER_HEAD,
            "§ePlayers Online",
            "§7Count: " + Bukkit.getOnlinePlayers().size(),
            "§7Max: " + Bukkit.getMaxPlayers()));

        // Navigation Buttons
        setItem(48, createItem(Material.REDSTONE,
            "§cPlugin Data",
            "§7View plugin-specific data"));

        setItem(50, createItem(Material.BARRIER,
            "§cBack",
            "§7Return to developer menu"));
    }

    /**
     * Handles click events within the server data menu.
     * Navigates to other developer menus based on the clicked slot.
     * @param player The player who clicked an item.
     * @param slot The slot that was clicked.
     */
    @Override
    public void click(Player player, int slot) {
        switch (slot) {
            case 48:
                new PluginDataMenu(player).open(player);
                break;
            case 50:
                new DeveloperMenu(player).open(player);
                break;
        }
    }

    /**
     * Formats the given TPS (ticks per second) value with color coding.
     * @param tps The TPS value.
     * @return A color-coded string representation of the TPS.
     */
    private String formatTPS(double tps) {
        if (tps > 18.0) return "§a" + String.format("%.2f", tps);
        if (tps > 16.0) return "§e" + String.format("%.2f", tps);
        return "§c" + String.format("%.2f", tps);
    }
}