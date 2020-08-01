package cat.nyaa.lobby.team;

import cat.nyaa.lobby.lobby.LobbyManager;
import cat.nyaa.lobby.score.ScoreSession;
import cat.nyaa.lobby.score.api.ScoreAPI;
import cat.nyaa.lobby.team.api.TeamAPI;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TeamApiImpl implements TeamAPI {
    private static TeamApiImpl INSTANCE;

    private TeamApiImpl() {
    }

    public static TeamApiImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (TeamApiImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TeamApiImpl();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public List<TeamWrapper> getTeamsInLobby(String lobby) {
        Set<Team> teams = Bukkit.getScoreboardManager().getMainScoreboard().getTeams();
        TeamManager teamManager = TeamManager.getInstance();
        List<TeamWrapper> collect = teams.stream().map(teamManager::getWrappedTeam)
                .filter(teamWrapper -> teamWrapper.getLobby().getName().equals(lobby)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public ScoreSession getScoreSession(Team team) {
        TeamManager teamManager = TeamManager.getInstance();
        TeamWrapper wrappedTeam = teamManager.getWrappedTeam(team);
        return wrappedTeam.getLobby().getScoreSession();
    }
}
