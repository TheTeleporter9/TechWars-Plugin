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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.List;

/**
 * A menu for developers to manage players. This menu displays a list of online players
 * and allows for various management actions such as viewing their inventory, kicking them,
 * or assigning them to teams. It can be opened from the DeveloperMenu or TeamEditMenu.
 */
public class PlayerManagementMenu extends BaseMenu {
    private final String targetPlayerName;
    private final Team currentTeam;

    /**
     * Constructs a new PlayerManagementMenu.
     * @param viewer The player who is viewing this menu.
     * @param targetPlayerName The name of the player whose actions are being managed. Can be null if showing all players.
     * @param team The current team context, or null if managing players globally.
     */
    public PlayerManagementMenu(Player viewer, String targetPlayerName, Team team) {
        super(Rows.SIX, "§6Player Management");
        this.targetPlayerName = targetPlayerName;
        this.currentTeam = team;
        onSetItems();
    }

    /**
     * Sets up the items and their actions within the player management menu.
     * Displays online players as player heads and provides action buttons.
     */
    @Override
    public void onSetItems() {
        int slot = 10;
        // List all players with their heads
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (slot % 9 == 8) slot += 2; // Skip to next row if at edge
            
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwningPlayer(player);
            meta.displayName(Component.text(player.getName()).color(NamedTextColor.YELLOW));
            
            // Add lores with player info
            List<Component> lore = new ArrayList<>();
            Team playerTeam = TechWars.getInstance().getTeamManager().getPlayerTeam(player).orElse(null);
            if (playerTeam != null) {
                lore.add(Component.text("Team: ").color(NamedTextColor.GRAY)
                    .append(Component.text(playerTeam.getName())
                    .color(TextColor.fromHexString(playerTeam.getColor()))));
            } else {
                lore.add(Component.text("Team: None").color(NamedTextColor.RED));
            }
            lore.add(Component.empty());
            lore.add(Component.text("Left-click to manage player").color(NamedTextColor.GRAY));
            lore.add(Component.text("Right-click to manage team").color(NamedTextColor.GRAY));
            meta.lore(lore);
            head.setItemMeta(meta);
            
            setItem(slot++, head);
        }

        // Action buttons
        setItem(24, createItem(Material.CHEST, "§eManage Inventory",
            "§7View and modify inventory"));

        // Back Button
        setItem(49, createItem(Material.BARRIER, "§cBack",
            currentTeam != null ? "§7Return to team view" : "§7Return to developer menu"));
    }

    /**
     * Handles click events within the player management menu.
     * Processes player selection for further actions or navigates back.
     * @param player The player who clicked an item.
     * @param slot The slot that was clicked.
     */
    @Override
    public void click(Player player, int slot) {
        if (slot == 49) {
            // Back button
            if (currentTeam != null) {
                new TeamEditMenu(player, currentTeam).open(player);
            } else {
                new DeveloperMenu(player).open(player);
            }
            return;
        }
        
        // Check if clicked on a player head
        ItemStack clickedItem = getInventory().getItem(slot);
        if (clickedItem != null && clickedItem.getType() == Material.PLAYER_HEAD) {
            SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
            if (meta != null && meta.getOwningPlayer() != null) {
                Player targetPlayer = Bukkit.getPlayer(meta.getOwningPlayer().getUniqueId());
                if (targetPlayer != null) {
                    Team playerTeam = TechWars.getInstance().getTeamManager()
                            .getPlayerTeam(targetPlayer).orElse(null);
                    
                    // Check if this is the currently selected player
                    if (targetPlayer.getName().equals(targetPlayerName)) {
                        playerTeam = currentTeam;
                    }
                    
                    boolean isRightClick = player.getOpenInventory().getTopInventory().getItem(slot) != null &&
                        player.getOpenInventory().getTopInventory().getItem(slot).equals(clickedItem);
                    
                    if (isRightClick) {
                        // Open team management
                        new TeamAssignMenu(player, targetPlayer.getName(), playerTeam).open(player);
                    } else {
                        // Open player management
                        new PlayerActionsMenu(player, targetPlayer.getName(), playerTeam).open(player);
                    }
                }
            }
        }
    }
}