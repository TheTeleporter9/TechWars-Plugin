package org.solocode.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SimpleBorderCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            Bukkit.getLogger().warning("Only in game can run this command!");
            return true;
        }

        Player player = (Player) sender;
        player.sendMessage("Border command executed!");
        return true;
    }
}
