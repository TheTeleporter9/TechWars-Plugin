package org.solocode.techwars.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.solocode.techwars.TechTree.TechTreeMenues;
import org.solocode.techwars.TechWars;
import org.solocode.techwars.teams.Team;

public class testGui implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage("Only ingame players can run this command!");
            return true;
        }

        // Get player's team
        Team playerTeam = TechWars.getInstance().getTeamManager().getPlayerTeam(player)
                .orElse(null);

        if (playerTeam == null) {
            player.sendMessage("§cYou are not in a team!");
            return true;
        }

        // Create a new TechTreeMenues instance for the team
        TechTreeMenues teamMenu = new TechTreeMenues();
        
        // Here you can load team-specific data for the menu
        // For example:
        // Object teamData = playerTeam.getData("techTree");
        // teamMenu.loadTeamData(teamData);

        teamMenu.open(player);

        return true;
    }
}
