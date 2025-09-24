package org.solocode.techwars.TechTree;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.solocode.menu.PagedMenu;
import org.solocode.menu.SimpleMenu;

//AI GENERATED
public class TestMenu extends PagedMenu {
    public TestMenu(Rows rows, String title) {
        super(Rows.THREE, "IT has two ways to die!", 21);

        final ItemStack killItem = new ItemStack(Material.NETHERITE_SWORD);
        final ItemMeta killItemMeta = killItem.getItemMeta();
        killItemMeta.displayName(Component.text("Kill your self!", NamedTextColor.RED));

        final ItemStack killItem2 = new ItemStack(Material.NETHERITE_SWORD);
        final ItemMeta killItemMeta2 = killItem2.getItemMeta();

        if (killItemMeta2 != null) {
            killItemMeta2.displayName(Component.text("Kill yourself!", NamedTextColor.RED));
            killItem2.setItemMeta(killItemMeta2); // <- this is required
        }

        setItem(0, 13, killItem2, player -> {
            player.sendMessage(Component.text("You selected slow Death!", NamedTextColor.RED));
            player.setVelocity(new Vector(0, 10, 0));
        });
    }
    @Override
    protected ItemStack createNextItem() {
        return new ItemStack(Material.ARROW);
    }

    @Override
    protected ItemStack createPrevItem() {
        return new ItemStack(Material.ARROW);
    }
}
