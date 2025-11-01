package org.solocode.techwars;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Techwars extends JavaPlugin {

    private SupabaseAPI supabaseAPI;
    private Scoreboard scoreboard;
    private final Map<String, Team> teams = new HashMap<>();
    private final Map<UUID, TeamColor> playerTeams = new HashMap<>();

    @Override
    public void onEnable() {
        // Save default config if it doesn't exist
        saveDefaultConfig();
        
        // Load config
        reloadConfig();
        
        // Initialize Supabase API with config
        try {
            supabaseAPI = new SupabaseAPI(getConfig());
        } catch (IllegalStateException e) {
            getLogger().severe(e.getMessage());
            getLogger().severe("Plugin will be disabled!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Initialize scoreboard - delay by 1 tick to ensure server is fully loaded
        Bukkit.getScheduler().runTaskLater(this, () -> {
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            if (manager != null) {
                scoreboard = manager.getMainScoreboard();
                // Fetch and create teams after scoreboard is ready
                fetchAndCreateTeams();
            } else {
                getLogger().severe("Failed to get scoreboard manager! Teams will not work.");
            }
        }, 1L);

        // Register OnJoin listener
        getServer().getPluginManager().registerEvents(new OnJoin(this, supabaseAPI), this);
        
        // Register ChatHandler listener
        getServer().getPluginManager().registerEvents(new ChatHandler(this), this);
        
        // Register command executor
        if (getCommand("refreshteams") != null) {
            getCommand("refreshteams").setExecutor(new RefreshTeamsCommand(this));
        }

        getLogger().info("TechwarsPlugin enabled!");
    }
    
    // Public method to refresh teams from database
    public void refreshTeams() {
        getLogger().info("Refreshing teams from database...");
        fetchAndCreateTeams();
        
        // Also refresh team assignments for all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            refreshPlayerTeam(player);
        }
    }
    
    // Refresh a single player's team
    public void refreshPlayerTeam(Player player) {
        String minecraftUsername = player.getName();
        supabaseAPI.getPlayerTeam(minecraftUsername, (String teamName) -> {
            Bukkit.getScheduler().runTask(this, () -> {
                Scoreboard scoreboard = getScoreboard();
                if (scoreboard == null) {
                    ScoreboardManager manager = Bukkit.getScoreboardManager();
                    if (manager != null) {
                        scoreboard = manager.getMainScoreboard();
                    } else {
                        return;
                    }
                }

                // Remove player from current team
                Team currentTeam = scoreboard.getEntryTeam(player.getName());
                if (currentTeam != null) {
                    currentTeam.removeEntry(player.getName());
                }

                if (teamName == null || teamName.isEmpty()) {
                    setPlayerTeam(player.getUniqueId(), null);
                    return;
                }

                TeamColor teamColor = TeamColor.fromName(teamName);
                if (teamColor == null) {
                    setPlayerTeam(player.getUniqueId(), null);
                    return;
                }

                String canonicalName = teamColor.getTeamName();
                Team bukkitTeam = getBukkitTeam(canonicalName);
                if (bukkitTeam == null) {
                    bukkitTeam = scoreboard.registerNewTeam(canonicalName);
                    bukkitTeam.setColor(teamColor.getColor());
                    bukkitTeam.setPrefix(teamColor.getColor() + "[" + canonicalName + "] ");
                    bukkitTeam.setDisplayName(teamColor.getColor() + canonicalName);
                    storeTeam(canonicalName, bukkitTeam);
                }

                bukkitTeam.addEntry(player.getName());
                setPlayerTeam(player.getUniqueId(), teamColor);
                player.setScoreboard(scoreboard);
                
                player.sendMessage(ChatColor.GREEN + "Your team has been updated!");
            });
        });
    }

    private void fetchAndCreateTeams() {
        supabaseAPI.getAllTeams((Map<String, List<String>> teamsData) -> {
            // Run on main thread for Bukkit API calls
            Bukkit.getScheduler().runTask(this, () -> {
                // Ensure scoreboard is available
                if (scoreboard == null) {
                    ScoreboardManager manager = Bukkit.getScoreboardManager();
                    if (manager != null) {
                        scoreboard = manager.getMainScoreboard();
                    } else {
                        getLogger().warning("Scoreboard manager is still not available. Retrying in 1 tick...");
                        Bukkit.getScheduler().runTaskLater(this, () -> fetchAndCreateTeams(), 1L);
                        return;
                    }
                }

                // Unregister existing teams that we're going to recreate
                // We need to check canonical names, not database names
                Set<String> canonicalNamesToCreate = new HashSet<>();
                for (String dbTeamName : teamsData.keySet()) {
                    TeamColor teamColor = TeamColor.fromName(dbTeamName);
                    if (teamColor != null) {
                        canonicalNamesToCreate.add(teamColor.getTeamName());
                    }
                }
                
                // Unregister teams that we're about to recreate
                for (String canonicalName : canonicalNamesToCreate) {
                    Team existingTeam = scoreboard.getTeam(canonicalName);
                    if (existingTeam != null) {
                        existingTeam.unregister();
                    }
                }
                teams.clear();

                // Print and create teams
                getLogger().info("=== Teams and Members ===");
                
                if (teamsData.isEmpty()) {
                    getLogger().warning("No teams found in database!");
                } else {
                    getLogger().info("Found " + teamsData.size() + " team(s) in database");
                }
                
                for (Map.Entry<String, List<String>> entry : teamsData.entrySet()) {
                    String teamName = entry.getKey();
                    List<String> members = entry.getValue();
                    
                    getLogger().info("Processing team: '" + teamName + "' with " + members.size() + " member(s)");
                    
                    TeamColor teamColor = TeamColor.fromName(teamName);
                    if (teamColor != null) {
                        // Use the canonical team name (e.g., "Green" instead of "Green Team")
                        String canonicalName = teamColor.getTeamName();
                        
                        // Get existing team or create new one
                        Team team = scoreboard.getTeam(canonicalName);
                        if (team == null) {
                            // Team doesn't exist, create it
                            team = scoreboard.registerNewTeam(canonicalName);
                        }
                        
                        // Configure team (update if it already existed)
                        team.setColor(teamColor.getColor());
                        team.setPrefix(teamColor.getColor() + "[" + canonicalName + "] ");
                        team.setDisplayName(teamColor.getColor() + canonicalName);
                        
                        // Store team using canonical name
                        teams.put(canonicalName, team);
                        
                        // Print team info with colored output
                        getLogger().info(teamColor.getColor() + canonicalName + ChatColor.RESET + ": " + String.join(", ", members));
                    } else {
                        getLogger().warning("Unknown team name: '" + teamName + "' - skipping");
                    }
                }
                
                getLogger().info("========================");
            });
        });
    }

    @Override
    public void onDisable() {
        // Clean up teams
        for (Team team : teams.values()) {
            team.unregister();
        }
        teams.clear();
        playerTeams.clear();
        getLogger().info("TechwarsPlugin disabled!");
    }

    // Optional: provide getter for SupabaseAPI
    public SupabaseAPI getSupabaseAPI() {
        return supabaseAPI;
    }
    
    // Get player's team
    public TeamColor getPlayerTeam(UUID playerId) {
        return playerTeams.get(playerId);
    }
    
    // Set player's team
    public void setPlayerTeam(UUID playerId, TeamColor team) {
        if (team == null) {
            playerTeams.remove(playerId);
        } else {
            playerTeams.put(playerId, team);
        }
    }
    
    // Get Bukkit Team by name
    public Team getBukkitTeam(String teamName) {
        return teams.get(teamName);
    }
    
    // Store a team in the teams map
    public void storeTeam(String teamName, Team team) {
        teams.put(teamName, team);
    }
    
    // Get scoreboard
    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}