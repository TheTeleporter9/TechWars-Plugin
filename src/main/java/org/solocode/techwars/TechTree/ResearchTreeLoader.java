package org.solocode.techwars.TechTree;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads a research tree from the researchTree.yml file using ConfigReader.
 * All data is stored in memory for fast access.
 */
public class ResearchTreeLoader {

    private final Map<Integer, ResearchStage> stages = new HashMap<>();
    private String unlockName;
    private int unlockStages;

    public ResearchTreeLoader() {
        ConfigReader.setup();  // Ensure config is set up
        load();
    }

    /**
     * Load the research tree from the YAML file
     */
    public void load() {
        FileConfiguration config = ConfigReader.get();
        if (config == null) {
            throw new IllegalStateException("Research tree configuration could not be loaded!");
        }

        // Get general info
        unlockName = config.getString("unlockname", "DefaultUnlock");
        unlockStages = config.getInt("unlockstages", 0);

        stages.clear();

        // Load stages
        if (config.getConfigurationSection("stages") != null) {
            for (String key : config.getConfigurationSection("stages").getKeys(false)) {
                int stageNumber;
                try {
                    stageNumber = Integer.parseInt(key);
                } catch (NumberFormatException e) {
                    continue; // skip invalid keys
                }

                List<String> unlockItems = config.getStringList("stages." + key + ".UnlockItems");
                List<String> unlockedRecipes = config.getStringList("stages." + key + ".UnlockedRecipes");

                ResearchStage stage = new ResearchStage(stageNumber, unlockItems, unlockedRecipes);
                stages.put(stageNumber, stage);
            }
        }
    }

    // ------------------------------
    // In-Memory Access Methods
    // ------------------------------

    /** Get the name of the unlock */
    public String getUnlockName() {
        return unlockName;
    }

    /** Get the total number of stages */
    public int getUnlockStages() {
        return unlockStages;
    }

    /** Get all stages as a map */
    public Map<Integer, ResearchStage> getStages() {
        return stages;
    }

    /** Get a specific stage by number */
    public ResearchStage getStage(int stageNumber) {
        return stages.get(stageNumber);
    }

    /** Get all unlock items in memory (flattened) */
    public List<String> getAllUnlockItems() {
        List<String> allItems = new ArrayList<>();
        for (ResearchStage stage : stages.values()) {
            allItems.addAll(stage.getUnlockItems());
        }
        return allItems;
    }

    /** Get all unlocked recipes in memory (flattened) */
    public List<String> getAllUnlockedRecipes() {
        List<String> allRecipes = new ArrayList<>();
        for (ResearchStage stage : stages.values()) {
            allRecipes.addAll(stage.getUnlockedRecipes());
        }
        return allRecipes;
    }

    // ------------------------------
    // Inner class representing a single stage
    // ------------------------------

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
