package cat.nyaa.lobby.team.api;

import cat.nyaa.lobby.score.ScoreSession;
import cat.nyaa.lobby.team.TeamApiImpl;
import cat.nyaa.lobby.team.TeamWrapper;
import org.bukkit.scoreboard.Team;

import java.util.List;

public interface TeamAPI {
    static TeamAPI getImpl(){
        return TeamApiImpl.getInstance();
    }

    List<TeamWrapper> getTeamsInLobby(String lobby);
    ScoreSession getScoreSession(Team team);
}
