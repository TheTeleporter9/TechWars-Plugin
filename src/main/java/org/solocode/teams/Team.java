package org.solocode.teams;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a team in the TechWars plugin.
 * Each team maintains its own set of players and data storage.
 */
public class Team {
    private final String name;
    private final Set<UUID> members;
    private final Set<String> offlineMembers;
    private final Map<String, Object> teamData;

    public Team(String name) {
        this.name = name;
        this.members = new HashSet<>();
        this.offlineMembers = new HashSet<>();
        this.teamData = new HashMap<>();
    }

    /**
     * Adds an offline player to the team
     * @param playerName the name of the player to add
     */
    public void addOfflineMember(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            addMember(player);
        } else {
            offlineMembers.add(playerName);
        }
    }

    /**
     * Gets the name of the team
     * @return team name
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a player to the team
     * @param player the player to add
     */
    public void addMember(Player player) {
        members.add(player.getUniqueId());
    }

    /**
     * Removes a player from the team
     * @param player the player to remove
     */
    public void removeMember(Player player) {
        members.remove(player.getUniqueId());
    }

    /**
     * Checks if a player is a member of this team
     * @param player the player to check
     * @return true if player is a member, false otherwise
     */
    public boolean isMember(Player player) {
        return members.contains(player.getUniqueId()) || offlineMembers.contains(player.getName());
    }

    /**
     * Stores data for the team
     * @param key the key to store the data under
     * @param value the data to store
     */
    public void setData(String key, Object value) {
        teamData.put(key, value);
    }

    /**
     * Retrieves data stored for the team
     * @param key the key of the data to retrieve
     * @param <T> the type of data to retrieve
     * @return the stored data, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T getData(String key) {
        return (T) teamData.get(key);
    }

    /**
     * Gets all member names of the team
     * @return set of member names
     */
    public Set<String> getMemberNames() {
        Set<String> allMembers = new HashSet<>(offlineMembers);
        for (UUID uuid : members) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                allMembers.add(player.getName());
            }
        }
        return allMembers;
    }
}