package org.solocode.techwars.dev.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.solocode.menu.BaseMenu;
import org.solocode.menu.SimpleMenue.Rows;
import net.kyori.adventure.text.Component;

public class PlayerInventoryMenu extends BaseMenu {
    private final Player targetPlayer;
    private final PlayerInventory targetInventory;
    private final int[] armorSlots = {5, 14, 23, 32}; // Head to Boots

    public PlayerInventoryMenu(Player viewer, Player targetPlayer) {
        super(Rows.SIX, "§6Inventory: " + targetPlayer.getName());
        this.targetPlayer = targetPlayer;
        this.targetInventory = targetPlayer.getInventory();
        onSetItems();
    }

    @Override
    public void onSetItems() {
        // Display armor slots (right side)
        ItemStack[] armor = targetInventory.getArmorContents();
        for (int i = 0; i < armor.length; i++) {
            if (armor[i] != null) {
                setItem(armorSlots[i], armor[i]);
            }
        }

        // Display inventory contents (centered)
        int slot = 0;
        for (ItemStack item : targetInventory.getContents()) {
            if (slot >= 36) break; // Only show main inventory
            if (item != null) {
                int menuSlot = getMenuSlot(slot);
                if (menuSlot != -1) {
                    setItem(menuSlot, item);
                }
            }
            slot++;
        }

        // Action buttons
        setItem(48, createItem(Material.BARRIER, "§cBack",
            "§7Return to player management"));
        
        setItem(50, createItem(Material.HOPPER, "§eClear Inventory",
            "§7Remove all items",
            "§cThis action cannot be undone!"));
    }

    private int getMenuSlot(int inventorySlot) {
        // Convert player inventory slot to menu slot
        // This centers the inventory contents in the menu
        int row = inventorySlot / 9;
        int column = inventorySlot % 9;
        
        // Start at row 1 (slot 9) to center vertically
        return (row + 1) * 9 + column;
    }

    @Override
    public void click(Player player, int slot) {
        if (slot == 48) { // Back
            new PlayerManagementMenu(player, targetPlayer.getName(), null).open(player);
            return;
        }

        if (slot == 50) { // Clear Inventory
            targetInventory.clear();
            player.sendMessage("§aInventory cleared!");
            onSetItems(); // Refresh the menu
            return;
        }

        // Handle armor and inventory slots
        if (isArmorSlot(slot)) {
            // Handle armor modification
            int armorIndex = getArmorIndex(slot);
            if (armorIndex != -1) {
                ItemStack currentItem = player.getItemInHand();
                ItemStack oldArmor = targetInventory.getArmorContents()[armorIndex];
                
                targetInventory.setArmorContents(new ItemStack[4]); // Clear armor first
                if (currentItem != null && currentItem.getType() != Material.AIR) {
                    targetInventory.setItem(armorIndex, currentItem);
                    player.setItemInHand(oldArmor);
                }
            }
        } else {
            // Handle regular inventory slots
            int inventorySlot = getInventorySlot(slot);
            if (inventorySlot != -1) {
                ItemStack currentItem = player.getItemInHand();
                ItemStack targetItem = targetInventory.getItem(inventorySlot);
                
                targetInventory.setItem(inventorySlot, currentItem);
                player.setItemInHand(targetItem);
            }
        }
        
        onSetItems(); // Refresh the menu
    }

    private boolean isArmorSlot(int slot) {
        for (int armorSlot : armorSlots) {
            if (slot == armorSlot) return true;
        }
        return false;
    }

    private int getArmorIndex(int menuSlot) {
        for (int i = 0; i < armorSlots.length; i++) {
            if (armorSlots[i] == menuSlot) return i;
        }
        return -1;
    }

    private int getInventorySlot(int menuSlot) {
        // Convert menu slot back to inventory slot
        int row = menuSlot / 9 - 1;
        int column = menuSlot % 9;
        
        if (row >= 0 && row < 4) {
            return row * 9 + column;
        }
        return -1;
    }
}