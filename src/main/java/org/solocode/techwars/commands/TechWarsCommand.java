package org.solocode.techwars.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.solocode.techwars.TechWars;

/**
 * Handles the main `/techwars` command for administrative tasks.
 * Currently, it supports reloading the plugin configuration.
 */
public class TechWarsCommand implements CommandExecutor {
    
    private final TechWars plugin;
    
    /**
     * Constructs a new TechWarsCommand.
     * @param plugin The instance of the TechWars plugin.
     */
    public TechWarsCommand(TechWars plugin) {
        this.plugin = plugin;
    }

    /**
     * Executes the techwars command.
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

        if (args.length < 1) {
            sendUsage(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "reload":
                plugin.reloadConfig();
                sender.sendMessage("§aConfiguration reloaded successfully!");
                break;
            default:
                sendUsage(sender);
                break;
        }

        return true;
    }

    /**
     * Sends the usage message for the techwars command to the sender.
     * @param sender The command sender.
     */
    private void sendUsage(CommandSender sender) {
        sender.sendMessage("§6TechWars Commands:");
        sender.sendMessage("§e/techwars reload §7- Reload configuration");
    }
}