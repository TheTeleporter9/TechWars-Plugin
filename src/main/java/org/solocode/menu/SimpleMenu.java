package org.solocode.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class SimpleMenu implements Menu{
    public final Map<Integer, Consumer<Player>> actions = new HashMap<>();
    private final Inventory inventory;

    public SimpleMenu(Rows rows, String title) {
        this.inventory = Bukkit.createInventory(this, rows.getSize(), title);
    }


    @Override
    public void click(Player player, int slot) {
        final Consumer<Player> action = this.actions.get(slot);

        if(action == null) return;

        action.accept(player);
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        setItem(slot, item, player -> {});
    }

    @Override
    public void setItem(int slot, ItemStack item, Consumer<Player> action) {
        this.actions.put(slot, action);
        getInventory().setItem(slot, item);
    }


    public abstract void onSetItems();

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public enum Rows {

        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6);
        private final int size;
        Rows(int rows) {
            this.size = rows * 9;
        }

        public int getSize() {
            return size;
        }
    }
}
