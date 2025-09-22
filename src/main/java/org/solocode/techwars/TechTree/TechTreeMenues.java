package org.solocode.techwars.TechTree;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.solocode.menu.SimpleMenue;

import java.util.List;

public class TechTreeMenues extends SimpleMenue {

    private ResearchTreeLoader treeLoader;

    public TechTreeMenues() {
        super(Rows.THREE, "TechTree");
        setupResearchTreeConfig();
        treeLoader = new ResearchTreeLoader(); // Load the research tree
    }

    void setupResearchTreeConfig() {
        // Setup config if not exists
        ConfigReader.setup();
        ConfigReader.get().addDefault("unlockname", "Minecraft");
        ConfigReader.get().options().copyDefaults(true);
        ConfigReader.save();
    }

    @Override
    public void onSetItems() {
        String unlockName = treeLoader.getUnlockName();
        int totalStages = treeLoader.getUnlockStages();

        for (int i= 1; i <= totalStages; i++) {
            ResearchTreeLoader.ResearchStage stage = treeLoader.getStage(i);
            if(stage != null) {
                Bukkit.getLogger().info("Stage " + i + " unlocks items: " + stage.getUnlockItems());
                Bukkit.getLogger().info("Stage " + i + " unlocks recipes: " + stage.getUnlockedRecipes());
            }
    }

     enum IconType {
        Unlock,
        Locked,
        UnlockedMilestone,
        LockedMilestone;

        public ItemStack getItemType(IconType type) {
            ItemStack itemType = new ItemStack(Material.STONE);
                switch (type) {
                    case Unlock -> itemType.setType(Material.BLUE_STAINED_GLASS_PANE);
                    case Locked -> itemType.setType(Material.RED_STAINED_GLASS_PANE);
                    case UnlockedMilestone -> itemType.setType(Material.LIME_SHULKER_BOX);
                    case LockedMilestone -> itemType.setType(Material.COMMAND_BLOCK);
                }
                return itemType;
            }
        }
    }
}
