package org.solocode.teams;

import org.bukkit.entity.Player;
import org.solocode.techwars.TechWars;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Manages teams and provides API access to team functionality.
 * This is the main entry point for interacting with the team system.
 */
public class TeamManager {
    private final Map<String, Team> teams;
    private final TechWars plugin;

    public TeamManager(TechWars plugin) {
        this.plugin = plugin;
        this.teams = new HashMap<>();
        loadTeams();
    }

    /**
     * Loads teams from the config file
     */
    public void loadTeams() {
        teams.clear();
        ConfigurationSection teamsSection = plugin.getConfig().getConfigurationSection("Teams");
        if (teamsSection == null) return;

        for (String teamName : teamsSection.getKeys(false)) {
            Team team = new Team(teamName);
            for (String playerName : teamsSection.getStringList(teamName)) {
                team.addOfflineMember(playerName);
            }
            teams.put(teamName, team);
        }
    }

    /**
     * Gets a team by its name
     * @param teamName the name of the team to get
     * @return the team if it exists
     */
    public Optional<Team> getTeam(String teamName) {
        return Optional.ofNullable(teams.get(teamName));
    }

    /**
     * Gets the team a player belongs to
     * @param player the player to check
     * @return the team the player belongs to, if any
     */
    public Optional<Team> getPlayerTeam(Player player) {
        return teams.values().stream()
                .filter(team -> team.isMember(player))
                .findFirst();
    }

    /**
     * Creates a new team
     * @param teamName the name of the new team
     * @return the created team, or empty if team already exists
     */
    public Optional<Team> createTeam(String teamName) {
        if (teams.containsKey(teamName)) {
            return Optional.empty();
        }
        Team team = new Team(teamName);
        teams.put(teamName, team);
        return Optional.of(team);
    }

    /**
     * Gets all registered teams
     * @return map of team names to teams
     */
    public Map<String, Team> getAllTeams() {
        return new HashMap<>(teams);
    }
}