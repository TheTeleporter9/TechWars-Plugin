package org.solocode.techwars.dev.menus;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.solocode.menu.BaseMenu;
import org.solocode.teams.Team;
import org.solocode.techwars.TechTree.TeamResearchData;
import org.solocode.techwars.TechWars;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ResearchTreeManagementMenu extends BaseMenu {
    
    private final Team team;
    private final Map<Integer, Team> teamSlots = new HashMap<>();
    
    public ResearchTreeManagementMenu(Player player, Team team) {
        super(Rows.SIX, "§6Research Tree Management");
        this.team = team;
        onSetItems();
    }
    
    @Override
    public void onSetItems() {
        teamSlots.clear();
        if (team == null) {
            showTeamSelection();
        } else {
            showTeamResearchManagement();
        }
    }

    @Override
    public void click(Player player, int slot) {
        if (team == null) {
            Team clickedTeam = teamSlots.get(slot);
            if (clickedTeam != null) {
                new ResearchTreeManagementMenu(player, clickedTeam).open(player);
            }
        } else {
            if (slot == 49) {
                new ResearchTreeManagementMenu(player, null).open(player);
                return;
            }
        }
    }
    
    private void showTeamSelection() {
        int slot = 0;
        for (Team availableTeam : TechWars.getInstance().getTeamManager().getAllTeams().values()) {
            if (slot >= 45) break;
            
            ItemStack teamItem = createTeamItem(availableTeam);
            setItem(slot, teamItem);
            teamSlots.put(slot, availableTeam);
            slot++;
        }
    }
    
    private void showTeamResearchManagement() {
        TeamResearchData researchData = new TeamResearchData(team);
        
        // Team Info
        setItem(4, createItem(Material.SHIELD, 
            "§6" + team.getName(), 
            "§7Current Stage: §e" + researchData.getCurrentStage(),
            "§7Unlocked Stages: §e" + researchData.getUnlockedStages().size()
        ));
        
        // Reset Progress
        setItem(19, createItem(Material.BARRIER,
            "§cReset Progress",
            "§7Click to reset all",
            "§7research progress"
        ), p -> {
            researchData.resetProgress();
            p.sendMessage("§aReset research progress for team " + team.getName());
            refresh();
        });
        
        // Unlock Next Stage
        setItem(21, createItem(Material.LIME_DYE,
            "§aUnlock Next Stage",
            "§7Click to unlock the",
            "§7next available stage"
        ), p -> {
            researchData.unlockStage(researchData.getCurrentStage());
            p.sendMessage("§aUnlocked stage " + researchData.getCurrentStage() + " for team " + team.getName());
            refresh();
        });
        
        // Test Requirements
        setItem(23, createItem(Material.HOPPER,
            "§eTest Requirements",
            "§7Click to test current",
            "§7stage requirements"
        ), this::testRequirements);
        
        // View All Stages
        setItem(25, createItem(Material.BOOK,
            "§bView All Stages",
            "§7Click to view all",
            "§7configured stages"
        ), this::showStagesInfo);
        
        // Back Button
        setItem(49, createItem(Material.ARROW,
            "§eBack",
            "§7Click to return to",
            "§7team selection"
        ), p -> new ResearchTreeManagementMenu(p, null).open(p));
    }
    
    private ItemStack createTeamItem(Team team) {
        TeamResearchData researchData = new TeamResearchData(team);
        return createItem(Material.SHIELD,
            "§6" + team.getName(),
            "§7Current Stage: §e" + researchData.getCurrentStage(),
            "§7Unlocked Stages: §e" + researchData.getUnlockedStages().size(),
            "",
            "§eClick to manage!"
        );
    }
    
    private void testRequirements(Player player) {
        TeamResearchData researchData = new TeamResearchData(team);
        int currentStage = researchData.getCurrentStage();
        
        ConfigurationSection stageConfig = TechWars.getInstance().getConfig()
            .getConfigurationSection("ResearchTree.stages." + currentStage);
            
        if (stageConfig == null) {
            player.sendMessage("§cNo configuration found for stage " + currentStage);
            return;
        }
        
        player.sendMessage("§6Requirements for stage " + currentStage + ":");
        ConfigurationSection reqs = stageConfig.getConfigurationSection("requirements");
        if (reqs != null) {
            for (String key : reqs.getKeys(false)) {
                String item = reqs.getString(key + ".item", "UNKNOWN");
                int amount = reqs.getInt(key + ".amount", 1);
                player.sendMessage("§7- " + amount + "x " + item);
            }
        }
    }
    
    private void showStagesInfo(Player player) {
        ConfigurationSection stages = TechWars.getInstance().getConfig()
            .getConfigurationSection("ResearchTree.stages");
            
        if (stages == null) {
            player.sendMessage("§cNo stages configured!");
            return;
        }
        
        player.sendMessage("§6Configured Research Stages:");
        for (String key : stages.getKeys(false)) {
            ConfigurationSection stage = stages.getConfigurationSection(key);
            if (stage != null) {
                player.sendMessage(
                    "\n§eStage " + key + ": §7" + stage.getString("name", "Unnamed Stage") +
                    "\n§7" + stage.getString("description", "No description")
                );
            }
        }
    }
    
    private void refresh() {
        onSetItems();
    }
}