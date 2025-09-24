package org.solocode.techwars.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.solocode.teams.Team;
import org.solocode.techwars.TechTree.TeamResearchData;
import org.solocode.techwars.TechWars;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles administrative commands related to the research tree.
 * Allows developers to unlock stages, reset progress, copy progress between teams, and test stage requirements.
 */
public class ResearchAdminCommand implements CommandExecutor, TabCompleter {
    
    private final TechWars plugin;
    
    /**
     * Constructs a new ResearchAdminCommand.
     * @param plugin The instance of the TechWars plugin.
     */
    public ResearchAdminCommand(TechWars plugin) {
        this.plugin = plugin;
    }

    /**
     * Executes the research admin command.
     * @param sender The command sender.
     * @param command The executed command.
     * @param label The alias of the command used.
     * @param args The command arguments.
     * @return true if the command was handled successfully, false otherwise.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("techwars.admin")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        if (args.length < 2) {
            sendUsage(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();
        String teamName = args[1];
        Team team = plugin.getTeamManager().getTeam(teamName).orElse(null);

        if (team == null) {
            sender.sendMessage("§cTeam not found: " + teamName);
            return true;
        }

        TeamResearchData researchData = new TeamResearchData(team);

        switch (subCommand) {
            case "unlock":
                if (args.length < 3) {
                    sender.sendMessage("§cUsage: /researchadmin unlock <team> <stage>");
                    return true;
                }
                try {
                    int stage = Integer.parseInt(args[2]);
                    researchData.unlockStage(stage);
                    sender.sendMessage("§aUnlocked stage " + stage + " for team " + teamName);
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cInvalid stage number: " + args[2]);
                }
                break;

            case "reset":
                researchData.resetProgress();
                sender.sendMessage("§aReset research progress for team " + teamName);
                break;

            case "copy":
                if (args.length < 3) {
                    sender.sendMessage("§cUsage: /researchadmin copy <fromTeam> <toTeam>");
                    return true;
                }
                Team toTeam = plugin.getTeamManager().getTeam(args[2]).orElse(null);
                if (toTeam == null) {
                    sender.sendMessage("§cTarget team not found: " + args[2]);
                    return true;
                }
                copyTeamProgress(team, toTeam);
                sender.sendMessage("§aCopied research progress from " + teamName + " to " + args[2]);
                break;

            case "test":
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("§cThis command can only be used by players!");
                    return true;
                }
                testRequirements(player, team);
                break;

            default:
                sendUsage(sender);
                break;
        }

        return true;
    }

    /**
     * Sends the usage message for the research admin command to the sender.
     * @param sender The command sender.
     */
    private void sendUsage(CommandSender sender) {
        sender.sendMessage("§6Research Admin Commands:");
        sender.sendMessage("§e/researchadmin unlock <team> <stage> §7- Force unlock a stage");
        sender.sendMessage("§e/researchadmin reset <team> §7- Reset team progress");
        sender.sendMessage("§e/researchadmin copy <fromTeam> <toTeam> §7- Copy progress");
        sender.sendMessage("§e/researchadmin test <team> §7- Test requirements");
    }

    /**
     * Copies the research progress from one team to another.
     * @param from The source team.
     * @param to The target team.
     */
    private void copyTeamProgress(Team from, Team to) {
        TeamResearchData fromData = new TeamResearchData(from);
        TeamResearchData toData = new TeamResearchData(to);

        // Reset target team's progress
        toData.resetProgress();

        // Copy stages one by one
        for (String stage : fromData.getUnlockedStages()) {
            toData.unlockStage(Integer.parseInt(stage));
        }
    }

    /**
     * Tests and displays the requirements for a team's current research stage.
     * @param player The player requesting the test.
     * @param team The team whose research requirements are to be tested.
     */
    private void testRequirements(Player player, Team team) {
        TeamResearchData researchData = new TeamResearchData(team);
        int currentStage = researchData.getCurrentStage();
        
        // Check if stage exists in config
        var stageConfig = plugin.getConfig().getConfigurationSection("ResearchTree.stages." + currentStage);
        if (stageConfig == null) {
            player.sendMessage("§cNo configuration found for stage " + currentStage);
            return;
        }

        player.sendMessage("§6Testing requirements for stage " + currentStage + ":");
        var requirements = stageConfig.getConfigurationSection("requirements");
        if (requirements == null) {
            player.sendMessage("§eNo requirements defined for this stage!");
            return;
        }

        requirements.getKeys(false).forEach(key -> {
            String item = requirements.getString(key + ".item");
            int amount = requirements.getInt(key + ".amount");
            player.sendMessage("§7- " + amount + "x " + item);
        });
    }

    /**
     * Provides tab completions for the research admin command.
     * @param sender The command sender.
     * @param command The executed command.
     * @param alias The alias of the command used.
     * @param args The command arguments.
     * @return A list of possible tab completions.
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("techwars.admin")) {
            return new ArrayList<>();
        }

        if (args.length == 1) {
            return List.of("unlock", "reset", "copy", "test").stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2) {
            return plugin.getTeamManager().getAllTeams().keySet().stream()
                    .filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("unlock")) {
            List<String> stages = new ArrayList<>();
            for (int i = 1; i <= 10; i++) { // Assuming max 10 stages
                stages.add(String.valueOf(i));
            }
            return stages.stream()
                    .filter(s -> s.startsWith(args[2]))
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}