package org.solocode.techwars.TechTree;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.solocode.menu.BaseMenu;
import org.solocode.teams.Team;
import org.solocode.techwars.TechWars;

import java.util.*;

public class TechTreeMenues extends BaseMenu {

    private final Team team;
    private final ResearchTreeLoader treeLoader;
    private final Map<Integer, Integer> slotToStage = new HashMap<>();
    private final int[] snakePattern;

    public TechTreeMenues(Team team) {
        super(Rows.SIX, "§6Research Tree");
        this.team = team;
        this.treeLoader = new ResearchTreeLoader();
        this.snakePattern = generateSnakePattern();
        onSetItems();
    }

    private int[] generateSnakePattern() {
        List<Integer> pattern = new ArrayList<>();
        boolean goingUp = true;
        
        for (int col = 0; col < 9; col++) {
            if (goingUp) {
                for (int row = 5; row >= 0; row--) {
                    pattern.add(row * 9 + col);
                }
            } else {
                for (int row = 0; row < 6; row++) {
                    pattern.add(row * 9 + col);
                }
            }
            goingUp = !goingUp;
        }
        
        return pattern.stream().mapToInt(Integer::intValue).toArray();
    }

    @Override
    public void onSetItems() {
        ConfigurationSection stages = TechWars.getInstance().getConfig().getConfigurationSection("ResearchTree.stages");
        if (stages == null) return;

        Set<String> unlockedStages = getTeamUnlockedStages();
        int currentStage = getTeamCurrentStage();
        
        int stageNumber = 1;
        for (int slot : snakePattern) {
            if (stageNumber > stages.getKeys(false).size()) break;
            
            slotToStage.put(slot, stageNumber);
            
            if (unlockedStages.contains(String.valueOf(stageNumber))) {
                setUnlockedStage(slot, stageNumber);
            } else if (stageNumber == currentStage) {
                setCurrentStage(slot, stageNumber);
            } else {
                setLockedStage(slot, stageNumber);
            }
            
            stageNumber++;
        }
    }

    private Set<String> getTeamUnlockedStages() {
        TeamResearchData researchData = new TeamResearchData(team);
        return new HashSet<>(researchData.getUnlockedStages());
    }

    private int getTeamCurrentStage() {
        TeamResearchData researchData = new TeamResearchData(team);
        return researchData.getCurrentStage();
    }

    private void setUnlockedStage(int slot, int stage) {
        ItemStack item = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(net.kyori.adventure.text.Component.text("§bUnlocked Stage " + stage));
            List<String> lore = getUnlockedStageLore(stage);
            meta.lore(lore.stream().map(text -> 
                net.kyori.adventure.text.Component.text(text)
            ).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        setItem(slot, item);
    }

    private void setCurrentStage(int slot, int stage) {
        ItemStack item = new ItemStack(Material.CHAIN_COMMAND_BLOCK);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(net.kyori.adventure.text.Component.text("§eStage " + stage + " - Available!"));
            List<String> lore = getCurrentStageLore(stage);
            meta.lore(lore.stream().map(text -> 
                net.kyori.adventure.text.Component.text(text)
            ).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        setItem(slot, item, player -> openUpgradeMenu(player, stage));
    }

    private void setLockedStage(int slot, int stage) {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(net.kyori.adventure.text.Component.text("§cLocked Stage " + stage));
            List<String> lore = getLockedStageLore(stage);
            meta.lore(lore.stream().map(text -> 
                net.kyori.adventure.text.Component.text(text)
            ).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        setItem(slot, item);
    }

    private List<String> getUnlockedStageLore(int stage) {
        ConfigurationSection stageConfig = TechWars.getInstance().getConfig()
                .getConfigurationSection("ResearchTree.stages." + stage);
        List<String> lore = new ArrayList<>();
        
        if (stageConfig != null) {
            lore.add("§7" + stageConfig.getString("name", "Stage " + stage));
            lore.add("§7" + stageConfig.getString("description", "No description"));
            lore.add("");
            lore.add("§aUnlocked!");
        }
        
        return lore;
    }

    private List<String> getCurrentStageLore(int stage) {
        ConfigurationSection stageConfig = TechWars.getInstance().getConfig()
                .getConfigurationSection("ResearchTree.stages." + stage);
        List<String> lore = new ArrayList<>();
        
        if (stageConfig != null) {
            lore.add("§7" + stageConfig.getString("name", "Stage " + stage));
            lore.add("§7" + stageConfig.getString("description", "No description"));
            lore.add("");
            lore.add("§eRequirements:");
            
            ConfigurationSection reqs = stageConfig.getConfigurationSection("requirements");
            if (reqs != null) {
                for (String key : reqs.getKeys(false)) {
                    String item = reqs.getString(key + ".item", "UNKNOWN");
                    int amount = reqs.getInt(key + ".amount", 1);
                    lore.add("§7- " + amount + "x " + item);
                }
            }
            
            lore.add("");
            lore.add("§eClick to view upgrade menu!");
        }
        
        return lore;
    }

    private List<String> getLockedStageLore(int stage) {
        ConfigurationSection stageConfig = TechWars.getInstance().getConfig()
                .getConfigurationSection("ResearchTree.stages." + stage);
        List<String> lore = new ArrayList<>();
        
        if (stageConfig != null) {
            lore.add("§7" + stageConfig.getString("name", "Stage " + stage));
            lore.add("§7" + stageConfig.getString("description", "No description"));
            lore.add("");
            lore.add("§cLocked - Complete previous stages first!");
        }
        
        return lore;
    }

    private void openUpgradeMenu(Player player, int stage) {
        new UpgradeMenu(player, team, stage).open(player);
    }

    @Override
    public void click(Player player, int slot) {
        Integer clickedStage = slotToStage.get(slot);
        if (clickedStage != null && clickedStage == getTeamCurrentStage()) {
            openUpgradeMenu(player, clickedStage);
        }
        // No action for unlocked (blue glass) or locked (red glass) stages
    }
}
