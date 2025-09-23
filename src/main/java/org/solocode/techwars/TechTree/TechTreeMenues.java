package org.solocode.techwars.TechTree;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.solocode.menu.SimpleMenue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TechTreeMenues extends SimpleMenue {

    private final ResearchTreeLoader treeLoader;

    private ArrayList<Integer> inventorySlots = new ArrayList<>(List.of(
            0, 1, 2, 4, 5, 6, 8
    )); // Adding the heads of the "snake"

    public TechTreeMenues() {
        super(Rows.FIVE, "TechTree");
        setupResearchTreeConfig();
        treeLoader = new ResearchTreeLoader(); // Load the research tree
    }

    void setupResearchTreeConfig() {
        // Setup config if not exists
        ConfigReader.setup();
        ConfigReader.get().addDefault("unlockname", "Minecraft");
        ConfigReader.get().options().copyDefaults(true);
        ConfigReader.save();

        //Setup inventory slots list:
        for (int i = 9; i <= 44; i +=2) {
            if(i == 19 || i == 28 || i == 37) i -= 1;
            inventorySlots.add(i);
        }
    }



    @Override
    public void onSetItems() {
        int totalStages = treeLoader.getUnlockStages();
        String unlockName = treeLoader.getUnlockName();

        for (int currentStage = 1; currentStage <= totalStages; currentStage++) {

            int smallestInvSlot = Collections.min(inventorySlots);
            setItem(smallestInvSlot, IconType.Locked.getItemType(IconType.Locked), this::itemClicked);
            inventorySlots.remove(Integer.valueOf(smallestInvSlot));
        }

    }


    public void itemClicked(Player player) {
        Bukkit.getLogger().info("yay it is working!");
    }

    enum IconType {
        Unlock,
        Locked;

        public ItemStack getItemType(IconType type) {
            ItemStack itemType = new ItemStack(Material.STONE);
                switch (type) {
                    case Unlock -> itemType.setType(Material.BLUE_STAINED_GLASS_PANE);
                    case Locked -> itemType.setType(Material.RED_STAINED_GLASS_PANE);
                }
                return itemType;
            }
        }
    }
