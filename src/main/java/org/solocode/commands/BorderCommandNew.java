package org.solocode.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.solocode.techwars.ParticleWall;

public class BorderCommandNew implements CommandExecutor {

    private ParticleWall pw = new ParticleWall();
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            Bukkit.getLogger().warning("Only in game can run this command!");
            return true;
        }

        Player player = (Player) sender;

        //Check if it is east west
        if (player.getYaw() >= 45 && player.getYaw() <= 145 ||
                player.getYaw() >= -145 && player.getYaw() <= -45) {

            pw.createWall(player.getLocation(), ParticleWall.Rotations.EASTWEST);
        } else {
            pw.createWall(player.getLocation(), ParticleWall.Rotations.NOTRHTSOUTH);
        }

        return true;
    }
}
