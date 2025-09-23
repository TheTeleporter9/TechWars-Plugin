package org.solocode.techwars.dev.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.solocode.menu.BaseMenu;
import org.solocode.menu.SimpleMenue.Rows;
import org.solocode.techwars.TechTree.ResearchTreeLoader;
import org.solocode.teams.Team;

import java.util.HashMap;
import java.util.Map;

public class ResearchProgressMenu extends BaseMenu {
    private final Team team;
    private ResearchTreeLoader treeLoader;
    private final Map<Integer, String> researchSlots = new HashMap<>();

    public ResearchProgressMenu(Player viewer, Team team) {
        super(Rows.SIX, "§6Research Progress: " + team.getName());
        this.team = team;
        try {
            this.treeLoader = new ResearchTreeLoader();
        } catch (Exception e) {
            this.treeLoader = null;
            viewer.sendMessage("§cError loading research tree: " + e.getMessage());
        }
        onSetItems();
    }

    @Override
    public void onSetItems() {
        if (treeLoader == null) {
            setItem(22, createItem(Material.BARRIER,
                "§cResearch Tree Error",
                "§7The research tree could not be loaded.",
                "§7Please check the console for errors."));
            return;
        }

        int slot = 10;
        Map<String, Boolean> research = team.getData("research");
        if (research == null) {
            research = new HashMap<>();
            team.setData("research", research);
        }

        // Display research stages in a centered grid
        for (ResearchTreeLoader.ResearchStage stage : treeLoader.getStages().values()) {
            if (slot % 9 == 8) slot += 2; // Skip to next row

            String stageId = String.valueOf(stage.getStageNumber());
            boolean unlocked = research.getOrDefault(stageId, false);
            researchSlots.put(slot, stageId);

            setItem(slot, createItem(
                unlocked ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK,
                "§eStage " + stageId,
                "§7Status: " + (unlocked ? "§aUnlocked" : "§cLocked"),
                "§7Click to toggle status"));

            slot++;
        }

        // Back button
        setItem(49, createItem(Material.BARRIER, "§cBack",
            "§7Return to team view"));
    }

    @Override
    public void click(Player player, int slot) {
        if (slot == 49) {
            new TeamEditMenu(player, team).open(player);
            return;
        }

        String techId = researchSlots.get(slot);
        if (techId != null) {
            Map<String, Boolean> research = team.getData("research");
            if (research == null) {
                research = new HashMap<>();
                team.setData("research", research);
            }

            // Toggle research status
            boolean currentStatus = research.getOrDefault(techId, false);
            research.put(techId, !currentStatus);
            
            // Update menu
            onSetItems();
            
            // Notify player
            player.sendMessage("§aTechnology " + techId + " is now " + 
                (!currentStatus ? "§aunlocked" : "§clocked"));
        }
    }
}