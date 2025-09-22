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
            } catch (IOException e) {
                Bukkit.getLogger().warning("Error reading researchTree.yml file" + e.toString());
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

