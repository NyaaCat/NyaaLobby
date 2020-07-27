package cat.nyaa.lobby.teamsign;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

public class TeamManager {
    private static TeamManager INSTANCE;

    private TeamManager() {
    }

    public static TeamManager getInstance() {
        if (INSTANCE == null) {
            synchronized (TeamManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TeamManager();
                }
            }
        }
        return INSTANCE;
    }

    private Map<String, TeamWrapper> teamWrapperMap = new HashMap<>();

    public TeamWrapper getPlayerTeam(Player player){
        Team entryTeam = Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(player.getName());
        return getWrappedTeam(entryTeam);
    }

    public TeamWrapper getWrappedTeam(Team team){
        if (team == null) {
            return null;
        }
        String name = team.getName();
        return teamWrapperMap.computeIfAbsent(name, n -> new TeamWrapper(team));
    }

    public TeamWrapper getWrappedTeam(Team team, Player player){
        if (team == null) {
            return null;
        }
        String name = team.getName();
        return teamWrapperMap.computeIfAbsent(name, n -> new TeamWrapper(team, player));
    }

    public void leaveTeam(Player player){
        leaveTeam(player, false);
    }

    public void leaveTeam(Player player, boolean isQuit) {
        TeamWrapper playerTeam = getPlayerTeam(player);
        if (playerTeam != null) {
            playerTeam.playerLeave(player, isQuit);
        }
    }

    public TeamWrapper getPlayerLoggedOutTeam(Player player) {
        return teamWrapperMap.values().stream().filter(teamWrapper -> teamWrapper.isLogoutMember(player)).findFirst().orElse(null);
    }
}
