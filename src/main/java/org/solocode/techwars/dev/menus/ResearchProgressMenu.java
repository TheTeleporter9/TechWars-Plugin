package org.solocode.techwars.dev.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.solocode.menu.BaseMenu;
import org.solocode.techwars.TechTree.ResearchTreeLoader;
import org.solocode.teams.Team;

import java.util.HashMap;
import java.util.Map;

/**
 * A developer menu for viewing and managing a team's research progress.
 * This menu displays each research stage and allows developers to toggle their unlocked status for the selected team.
 */
public class ResearchProgressMenu extends BaseMenu {
    private final Team team;
    private ResearchTreeLoader treeLoader;
    private final Map<Integer, String> researchSlots = new HashMap<>();

    /**
     * Constructs a new ResearchProgressMenu.
     * @param viewer The player who is viewing this menu.
     * @param team The team whose research progress is to be managed.
     */
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

    /**
     * Sets up the items and their actions within the research progress menu.
     * Displays each research stage with its current unlocked status for the team.
     */
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

    /**
     * Handles click events within the research progress menu.
     * Allows toggling the unlocked status of research stages for the team.
     * @param player The player who clicked an item.
     * @param slot The slot that was clicked.
     */
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