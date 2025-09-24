package org.solocode.techwars.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.solocode.teams.Team;
import org.solocode.techwars.TechWars;

public class ChatListener implements Listener {
    
    @EventHandler
    public void onChat(AsyncChatEvent event) {
        var optTeam = TechWars.getInstance().getTeamManager().getPlayerTeam(event.getPlayer());
        if (optTeam.isPresent()) {
            Team team = optTeam.get();
            event.renderer((source, sourceDisplayName, message, viewer) -> 
                Component.text()
                    .append(Component.text("[" + team.getName() + "] ", TextColor.fromHexString(team.getColor())))
                    .append(sourceDisplayName)
                    .append(Component.text(": "))
                    .append(message)
                    .build()
            );
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var optTeam = TechWars.getInstance().getTeamManager().getPlayerTeam(event.getPlayer());
        if (optTeam.isPresent()) {
            Team team = optTeam.get();
            event.getPlayer().displayName(Component.text(event.getPlayer().getName(), 
                TextColor.fromHexString(team.getColor())));
            event.getPlayer().playerListName(Component.text(event.getPlayer().getName(), 
                TextColor.fromHexString(team.getColor())));
        }
    }
}