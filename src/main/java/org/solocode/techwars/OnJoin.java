package org.solocode.techwars;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import java.util.logging.Logger;

public class OnJoin implements Listener {

    private final SupabaseAPI supabaseAPI;
    private final Techwars plugin; // Reference to your main plugin class
    private final Logger logger;

    public OnJoin(Techwars plugin, SupabaseAPI api) {
        this.plugin = plugin;
        this.supabaseAPI = api;
        this.logger = plugin.getLogger();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String minecraftUsername = player.getName();

        supabaseAPI.getPlayerTeam(minecraftUsername, (String teamName) -> {
            // Schedule on main thread for Bukkit API calls
            Bukkit.getScheduler().runTask(plugin, () -> {
                // Get scoreboard first
                Scoreboard scoreboard = plugin.getScoreboard();
                if (scoreboard == null) {
                    ScoreboardManager manager = Bukkit.getScoreboardManager();
                    if (manager != null) {
                        scoreboard = manager.getMainScoreboard();
                    } else {
                        logger.warning("Cannot process player join - scoreboard not available.");
                        return;
                    }
                }

                // Remove player from any existing team first
                Team currentTeam = scoreboard.getEntryTeam(player.getName());
                if (currentTeam != null) {
                    currentTeam.removeEntry(player.getName());
                }

                if (teamName == null || teamName.isEmpty()) {
                    // Player is not in a team
                    player.sendMessage(ChatColor.RED + "You are not assigned to a team.");
                    plugin.setPlayerTeam(player.getUniqueId(), null);
                    return;
                }

                TeamColor teamColor = TeamColor.fromName(teamName);
                if (teamColor == null) {
                    // Invalid team name
                    player.sendMessage(ChatColor.RED + "You are not assigned to a team.");
                    plugin.setPlayerTeam(player.getUniqueId(), null);
                    return;
                }

                // Use the canonical team name (e.g., "Green" instead of "Green Team")
                String canonicalName = teamColor.getTeamName();

                // Get or create the Bukkit team using canonical name
                Team bukkitTeam = plugin.getBukkitTeam(canonicalName);
                if (bukkitTeam == null) {
                    // Team doesn't exist yet, create it
                    bukkitTeam = scoreboard.registerNewTeam(canonicalName);
                    bukkitTeam.setColor(teamColor.getColor());
                    bukkitTeam.setPrefix(teamColor.getColor() + "[" + canonicalName + "] ");
                    bukkitTeam.setDisplayName(teamColor.getColor() + canonicalName);
                    // Store the team in the plugin's map
                    plugin.storeTeam(canonicalName, bukkitTeam);
                }

                // Add player to the team
                bukkitTeam.addEntry(player.getName());
                
                // Store team for later reference
                plugin.setPlayerTeam(player.getUniqueId(), teamColor);
                
                // Set player's scoreboard
                player.setScoreboard(scoreboard);
            });
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Scoreboard scoreboard = plugin.getScoreboard();
        if (scoreboard != null) {
            // Remove player from team (optional - teams persist)
            Team team = scoreboard.getEntryTeam(player.getName());
            if (team != null) {
                team.removeEntry(player.getName());
            }
        }
        plugin.setPlayerTeam(player.getUniqueId(), null);
    }

}
