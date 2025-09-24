package org.solocode.techwars.TechTree;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.solocode.menu.BaseMenu;
import org.solocode.teams.Team;
import org.solocode.techwars.TechWars;

import java.util.ArrayList;
import java.util.List;

public class UpgradeMenu extends BaseMenu {

    private final Team team;
    private final int stage;
    private final int[] resourceSlots = {2, 3, 4}; // Top row, left-center
    private final int[] submitSlots = {8, 17, 26}; // Far right, last three slots

    public UpgradeMenu(Player player, Team team, int stage) {
        super(Rows.THREE, "§6Research Upgrade - Stage " + stage);
        this.team = team;
        this.stage = stage;
        onSetItems();
    }

    @Override
    public void onSetItems() {
        // Back button
        ItemStack backButton = new ItemStack(Material.SPECTRAL_ARROW);
        ItemMeta backMeta = backButton.getItemMeta();
        if (backMeta != null) {
            backMeta.displayName(net.kyori.adventure.text.Component.text("§eBack to Research Tree"));
            backButton.setItemMeta(backMeta);
        }
        setItem(0, backButton, p -> new TechTreeMenues(team).open(p));

        // Fill background with grey stained glass panes, excluding interactive slots
        ItemStack greyGlassPane = createItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < getInventory().getSize(); i++) {
            // Skip the back button, resource slots, and submit slots
            boolean isInteractiveSlot = (i == 0) ||
                                        (i >= 2 && i <= 4) ||
                                        (i == 8 || i == 17 || i == 26);
            if (!isInteractiveSlot) {
                setItem(i, greyGlassPane);
            }
        }

        // Add hoppers for resource input slots
        ItemStack hopper = createItem(Material.HOPPER, "§ePlace Resources Here");
        for (int slot : resourceSlots) {
            setItem(slot, hopper);
        }

        // Add cauldrons below resource input
        setItem(11, createItem(Material.CAULDRON, "§7Required Items:"));
        setItem(15, createItem(Material.CAULDRON, "§7Your Inventory:"));

        // Add pistons for output at the bottom
        setItem(20, createItem(Material.PISTON, "§bOutput Slot 1"));
        setItem(21, createItem(Material.PISTON, "§bOutput Slot 2"));
        setItem(22, createItem(Material.PISTON, "§bOutput Slot 3"));
        setItem(23, createItem(Material.PISTON, "§bOutput Slot 4"));
        setItem(24, createItem(Material.PISTON, "§bOutput Slot 5"));
        
        // Submit buttons
        ItemStack submitButton = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta submitMeta = submitButton.getItemMeta();
        if (submitMeta != null) {
            submitMeta.displayName(net.kyori.adventure.text.Component.text("§aSubmit Resources"));
            List<net.kyori.adventure.text.Component> submitLore = new ArrayList<>();
            submitLore.add(net.kyori.adventure.text.Component.text("§7Click to submit resources"));
            submitLore.add(net.kyori.adventure.text.Component.text("§7and unlock this stage!"));
            submitMeta.lore(submitLore);
            submitButton.setItemMeta(submitMeta);
        }

        for (int slot : submitSlots) {
            setItem(slot, submitButton.clone()); // Removed action here, will be handled in click method
        }
    }

    @Override
    public void click(Player player, int slot) {
        if (slot == 0) {
            new TechTreeMenues(team).open(player);
            return;
        }

        for (int submitSlot : submitSlots) {
            if (slot == submitSlot) {
                handleSubmit(player);
                return;
            }
        }

        // Optionally handle other clicks or do nothing for non-interactive slots
    }

    private void handleSubmit(Player player) {
        ConfigurationSection stageConfig = TechWars.getInstance().getConfig()
                .getConfigurationSection("ResearchTree.stages." + stage);
        
        if (stageConfig == null) {
            player.sendMessage("§cError: Stage configuration not found!");
            return;
        }

        // Check requirements
        ConfigurationSection requirements = stageConfig.getConfigurationSection("requirements");
        if (requirements == null) {
            player.sendMessage("§cError: Stage requirements not found!");
            return;
        }

        // Get items from resource slots
        List<ItemStack> providedItems = new ArrayList<>();
        for (int slot : resourceSlots) {
            ItemStack item = getInventory().getItem(slot);
            if (item != null && !item.getType().isAir()) {
                providedItems.add(item);
            }
        }

        // Validate requirements
        if (validateRequirements(providedItems, requirements)) {
            // Update team progress
            TeamResearchData researchData = new TeamResearchData(team);
            researchData.unlockStage(stage);

            // Clear resource slots
            for (int slot : resourceSlots) {
                getInventory().setItem(slot, null);
            }

            // Apply unlocks
            applyUnlocks(player, stageConfig);

            player.sendMessage("§aSuccessfully unlocked stage " + stage + "!");
            new TechTreeMenues(team).open(player);
        } else {
            player.sendMessage("§cIncorrect resources! Check the requirements and try again.");
        }
    }

    private boolean validateRequirements(List<ItemStack> provided, ConfigurationSection requirements) {
        for (String key : requirements.getKeys(false)) {
            String requiredItem = requirements.getString(key + ".item");
            int requiredAmount = requirements.getInt(key + ".amount");

            boolean found = false;
            for (ItemStack item : provided) {
                if (item.getType().name().equals(requiredItem) && item.getAmount() >= requiredAmount) {
                    found = true;
                    break;
                }
            }

            if (!found) return false;
        }
        return true;
    }

    private void applyUnlocks(Player player, ConfigurationSection stageConfig) {
        ConfigurationSection unlocks = stageConfig.getConfigurationSection("unlocks");
        if (unlocks == null) return;

        // Apply recipes
        List<String> recipes = unlocks.getStringList("recipes");
        for (String recipe : recipes) {
            // TODO: Implement recipe unlocking
            player.sendMessage("§7Unlocked recipe: " + recipe);
        }

        // Apply permissions
        List<String> permissions = unlocks.getStringList("permissions");
        for (String permission : permissions) {
            // TODO: Implement permission granting
            player.sendMessage("§7Granted permission: " + permission);
        }
    }
}