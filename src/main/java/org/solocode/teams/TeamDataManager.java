package org.solocode.teams;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TeamDataManager {
    private final Plugin plugin;
    private final File dataFile;
    private final Gson gson;

    public TeamDataManager(Plugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "teamData.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
    }

    public void saveTeamData(Map<String, Map<String, Object>> teamData) {
        try (Writer writer = new FileWriter(dataFile)) {
            gson.toJson(teamData, writer);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save team data: " + e.getMessage());
        }
    }

    public Map<String, Map<String, Object>> loadTeamData() {
        if (!dataFile.exists()) {
            return new HashMap<>();
        }

        try (Reader reader = new FileReader(dataFile)) {
            Type type = new TypeToken<Map<String, Map<String, Object>>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not load team data: " + e.getMessage());
            return new HashMap<>();
        }
    }
}