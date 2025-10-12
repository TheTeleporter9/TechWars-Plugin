package org.solocode.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.solocode.techwars.ParticleWall;
import org.solocode.util.ChunkUtils;

public class BorderCommand implements CommandExecutor {

    private final ParticleWall pw;

    public BorderCommand(ParticleWall pw) {
        this.pw = pw;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getLogger().warning("Only in-game players can run this command!");
            return true;
        }

        Player player = (Player) sender;

        Location loc = ChunkUtils.getChunkCenter(player.getLocation());


        while (loc.getBlock().getType() == Material.AIR && loc.getY() > 0) {
            loc = loc.clone().add(0, -1, 0);
        }

        pw.wallCenterLocations.add(loc);


        player.sendMessage(Component.text("Wall added at chunk center: " + loc));


        pw.createWall(pw.wallCenterLocations);


        return true;
    }
}
