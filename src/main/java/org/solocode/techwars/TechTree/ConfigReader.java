package org.solocode.techwars.TechTree;


import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


import java.io.File;
import java.io.IOException;

public class ConfigReader {

    private static File file;
    private static FileConfiguration customFile;

    public static void setup() {
        file = new File(Bukkit.getServer()
                .getPluginManager()
                .getPlugin("TechWars")
                .getDataFolder(),
                "researchTree.yml");

        if(!file.exists()) {
            try {
                file.createNewFile();
                customFile = YamlConfiguration.loadConfiguration(file);
                
                // Set default values
                customFile.set("unlockname", "Research");
                customFile.set("unlockstages", 3);
                
                // Example stage structure
                customFile.set("stages.1.UnlockItems", new String[]{"STONE_PICKAXE", "STONE_AXE"});
                customFile.set("stages.1.UnlockedRecipes", new String[]{"STONE_TOOLS"});
                
                customFile.set("stages.2.UnlockItems", new String[]{"IRON_PICKAXE", "IRON_AXE"});
                customFile.set("stages.2.UnlockedRecipes", new String[]{"IRON_TOOLS"});
                
                customFile.set("stages.3.UnlockItems", new String[]{"DIAMOND_PICKAXE", "DIAMOND_AXE"});
                customFile.set("stages.3.UnlockedRecipes", new String[]{"DIAMOND_TOOLS"});
                
                save();
            } catch (IOException e) {
                Bukkit.getLogger().warning("Error creating researchTree.yml file: " + e.toString());
            }
        }

        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return customFile;
    }

    public static void save() {
        try {
            customFile.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().warning("Could not save researchTree.yml file");
        }
    }

    public static void  reload() {
        customFile = YamlConfiguration.loadConfiguration(file);
    }

}

