package org.solocode.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.solocode.menu.SimpleMenu;
import org.solocode.techwars.TechTree.TestMenu;

public class TestMenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof final Player player)) {
            Bukkit.getLogger().severe("Only ingame player can run this command");
            return true;
        }

        TestMenu menu = new TestMenu(SimpleMenu.Rows.THREE, "IT has two ways to die!");
        menu.open(player);
        return true;
    }
}
