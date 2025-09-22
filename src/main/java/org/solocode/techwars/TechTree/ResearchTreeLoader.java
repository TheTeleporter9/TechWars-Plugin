package org.solocode.techwars.TechTree;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads a research tree from the researchTree.yml file using ConfigReader.
 */
public class ResearchTreeLoader {

    private final Map<Integer, ResearchStage> stages = new HashMap<>();
    private String unlockName;
    private int unlockStages;

    public ResearchTreeLoader() {
        load();
    }

    /**
     * Load the research tree from the YAML file
     */
    public void load() {
        FileConfiguration config = ConfigReader.get();

        // Get general info
        unlockName = config.getString("unlockname", "DefaultUnlock");
        unlockStages = config.getInt("unlockstages", 0);

        stages.clear();

        // If the YAML contains a "stages" section, loop through each stage
        if (config.getConfigurationSection("stages") != null) {
            for (String key : config.getConfigurationSection("stages").getKeys(false)) {
                int stageNumber;
                try {
                    stageNumber = Integer.parseInt(key);
                } catch (NumberFormatException e) {
                    continue; // skip invalid stage keys
                }

                List<String> unlockItems = config.getStringList("stages." + key + ".UnlockItems");
                List<String> unlockedRecipes = config.getStringList("stages." + key + ".UnlockedRecipes");

                ResearchStage stage = new ResearchStage(stageNumber, unlockItems, unlockedRecipes);
                stages.put(stageNumber, stage);
            }
        }
    }

    // Getters
    public String getUnlockName() {
        return unlockName;
    }

    public int getUnlockStages() {
        return unlockStages;
    }

    public Map<Integer, ResearchStage> getStages() {
        return stages;
    }

    public ResearchStage getStage(int stageNumber) {
        return stages.get(stageNumber);
    }

    // Inner class representing a single stage
    public static class ResearchStage {
        private final int stageNumber;
        private final List<String> unlockItems;
        private final List<String> unlockedRecipes;

        public ResearchStage(int stageNumber, List<String> unlockItems, List<String> unlockedRecipes) {
            this.stageNumber = stageNumber;
            this.unlockItems = new ArrayList<>(unlockItems);
            this.unlockedRecipes = new ArrayList<>(unlockedRecipes);
        }

        public int getStageNumber() {
            return stageNumber;
        }

        public List<String> getUnlockItems() {
            return unlockItems;
        }

        public List<String> getUnlockedRecipes() {
            return unlockedRecipes;
        }
    }
}
