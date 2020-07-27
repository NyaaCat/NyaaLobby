package cat.nyaa.lobby;

import cat.nyaa.lobby.teamsign.TeamManager;
import cat.nyaa.lobby.teamsign.TeamWrapper;
import cat.nyaa.nyaacore.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        TeamWrapper playerLoggedOutTeam = TeamManager.getInstance().getPlayerLoggedOutTeam(player);

        if (playerLoggedOutTeam != null){
            if (playerLoggedOutTeam.isFull()){
                new Message(I18n.format("team.join.login.team_full", playerLoggedOutTeam.getTeam().getDisplayName())).send(player);
                return;
            }
            playerLoggedOutTeam.playerJoin(player);

        }


    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        TeamManager.getInstance().leaveTeam(player, true);
    }
}
