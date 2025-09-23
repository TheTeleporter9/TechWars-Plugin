package org.solocode.techwars.dev.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.solocode.menu.BaseMenu;
import org.solocode.menu.SimpleMenue.Rows;
import org.solocode.teams.Team;
import org.solocode.techwars.TechWars;

import java.util.ArrayList;
import java.util.List;

public class PlayerManagementMenu extends BaseMenu {
    public PlayerManagementMenu(Player viewer, String targetPlayerName, Team team) {
        super(Rows.SIX, "§6Player Management");
        onSetItems();
    }

    @Override
    public void onSetItems() {
        int slot = 10;
        // List all players with their heads
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (slot % 9 == 8) slot += 2; // Skip to next row if at edge
            
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwningPlayer(player);
            meta.setDisplayName("§e" + player.getName());
            
            // Add lores with player info
            List<String> lore = new ArrayList<>();
            lore.add("§7Click to manage player");
            Team playerTeam = TechWars.getInstance().getTeamManager().getPlayerTeam(player).orElse(null);
            if (playerTeam != null) {
                lore.add("§7Team: §a" + playerTeam.getName());
            } else {
                lore.add("§7Team: §cNone");
            }
            meta.setLore(lore);
            head.setItemMeta(meta);
            
            setItem(slot++, head);
        }

        // Back button
        setItem(49, createItem(Material.BARRIER, "§cBack",
            "§7Return to previous menu"));
            
        setItem(24, createItem(Material.CHEST, "§eManage Inventory",
            "§7View and modify inventory"));

        // Back Button
        setItem(49, createItem(Material.BARRIER, "§cBack",
            "§7Return to team view"));
    }

    @Override
    public void click(Player player, int slot) {
        if (slot == 49) {
            // Back button
            new DeveloperMenu(player).open(player);
            return;
        }
        
        // Check if clicked on a player head
        ItemStack clickedItem = getInventory().getItem(slot);
        if (clickedItem != null && clickedItem.getType() == Material.PLAYER_HEAD) {
            SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
            if (meta != null && meta.getOwningPlayer() != null) {
                Player targetPlayer = Bukkit.getPlayer(meta.getOwningPlayer().getUniqueId());
                if (targetPlayer != null) {
                    // Open player-specific management menu
                    Team playerTeam = TechWars.getInstance().getTeamManager()
                            .getPlayerTeam(targetPlayer).orElse(null);
                    new PlayerActionsMenu(player, targetPlayer.getName(), playerTeam).open(player);
                }
            }
        }
    }
}