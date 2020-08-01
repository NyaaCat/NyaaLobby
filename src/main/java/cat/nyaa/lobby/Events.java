package cat.nyaa.lobby;

import cat.nyaa.lobby.lobby.Lobby;
import cat.nyaa.lobby.lobby.LobbyManager;
import cat.nyaa.lobby.teamsign.TeamManager;
import cat.nyaa.lobby.teamsign.TeamWrapper;
import cat.nyaa.nyaacore.Message;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Team;

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
            playerLoggedOutTeam.teleport(player);
            return;
        }
        LobbyManager.getInstance().getDefaultLobby().teleportPlayer(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        TeamManager.getInstance().leaveTeam(player, true);
    }

    @EventHandler
    public void onPlayerRightClickSign(PlayerInteractEvent event){
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null){
            return;
        }
        if (clickedBlock.getState() instanceof Sign){
            TeamManager.getInstance().onSignClicked(clickedBlock.getState(), event.getPlayer());
        }
    }
}
