package org.solocode.techwars;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RefreshTeamsCommand implements CommandExecutor {

    private final Techwars plugin;

    public RefreshTeamsCommand(Techwars plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("techwars.refresh")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("player") && sender instanceof Player) {
            // Refresh only the sender's team
            plugin.refreshPlayerTeam((Player) sender);
            sender.sendMessage(ChatColor.GREEN + "Your team has been refreshed from the database.");
            return true;
        }

        // Refresh all teams
        plugin.refreshTeams();
        sender.sendMessage(ChatColor.GREEN + "All teams have been refreshed from the database!");
        return true;
    }
}

