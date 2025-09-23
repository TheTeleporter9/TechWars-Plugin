package org.solocode.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Base class for all menus providing common functionality
 */
public abstract class BaseMenu extends SimpleMenue {
    protected BaseMenu(Rows rows, String title) {
        super(rows, title);
    }

    /**
     * Creates an ItemStack with the given material, name and lore
     * @param material the material
     * @param name the name
     * @param lore the lore lines
     * @return the created ItemStack
     */
    protected ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(net.kyori.adventure.text.Component.text(name));
            meta.lore(Arrays.stream(lore)
                    .map(line -> net.kyori.adventure.text.Component.text(line))
                    .toList());
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Override this method to handle click events
     * @param player the player who clicked
     * @param slot the slot that was clicked
     */
    @Override
    public abstract void click(Player player, int slot);

    /**
     * Override this method to set up the menu items
     */
    @Override
    public abstract void onSetItems();
}