package org.solocode.techwars.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.solocode.techwars.TechWars;
import org.solocode.techwars.dev.DeveloperAPI;
import org.solocode.techwars.dev.DeveloperMenu;

public class DevCommand implements CommandExecutor {
    private final DeveloperAPI developerAPI;

    public DevCommand(TechWars plugin) {
        this.developerAPI = new DeveloperAPI(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        if (!developerAPI.isDeveloper(player)) {
            player.sendMessage("§cYou don't have permission to use developer tools!");
            return true;
        }

        new DeveloperMenu(player).open(player);
        return true;
    }
}