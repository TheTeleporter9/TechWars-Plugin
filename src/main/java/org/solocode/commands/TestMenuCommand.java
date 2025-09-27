package org.solocode.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.solocode.techwars.TechTree.MyPagedMenu;

public class TestMenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof final Player player)) {
            Bukkit.getLogger().severe("Only ingame player can run this command");
            return true;
        }

        MyPagedMenu menu = new MyPagedMenu();

        // Debug: Check if pages exist
        System.out.println("Current page: " + menu.getCurrentPage());
        System.out.println("Page count: " + menu.getPageCount());

        menu.open(player);
        return true;
    }
}
