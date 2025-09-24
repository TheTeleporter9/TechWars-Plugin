package org.solocode.techwars.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.solocode.techwars.TechTree.TechTreeMenues;
import org.solocode.techwars.TechWars;
import org.solocode.teams.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the `/researchtree` command, which opens the research tree GUI for a player's team.
 */
public class ResearchTreeCommand implements CommandExecutor, TabCompleter {
    
    private final TechWars plugin;
    
    /**
     * Constructs a new ResearchTreeCommand.
     * @param plugin The instance of the TechWars plugin.
     */
    public ResearchTreeCommand(TechWars plugin) {
        this.plugin = plugin;
    }

    /**
     * Executes the research tree command.
     * @param sender The command sender.
     * @param command The executed command.
     * @param label The alias of the command used.
     * @param args The command arguments.
     * @return true if the command was handled successfully, false otherwise.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage("§cOnly in-game players can use this command!");
            return true;
        }

        // Get player's team
        Team playerTeam = plugin.getTeamManager().getPlayerTeam(player)
                .orElse(null);

        if (playerTeam == null) {
            player.sendMessage("§cYou must be in a team to use the research system!");
            return true;
        }

        // Create and open the research tree menu
        TechTreeMenues researchMenu = new TechTreeMenues(playerTeam);
        researchMenu.open(player);

        return true;
    }

    /**
     * Provides tab completions for the research tree command.
     * Currently, no specific tab completions are provided for this basic command.
     * @param sender The command sender.
     * @param command The executed command.
     * @param alias The alias of the command used.
     * @param args The command arguments.
     * @return An empty list of tab completions.
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>(); // No tab completions needed for basic command
    }
}
