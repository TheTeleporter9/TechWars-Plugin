package org.solocode.menu.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.solocode.menu.Menu;

public class InventoryListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        final Inventory clickedInventory = event.getClickedInventory();

        if(clickedInventory == null) return;

        if(!(clickedInventory.getHolder() instanceof final Menu menue)) {
            //Here if cliked inventory is not a menue
            return;
        }

        //Her if it is a custom inventory (GUI)
        event.setCancelled(true);
        menue.click((Player) event.getWhoClicked(), event.getSlot());


    }
}
