package cat.nyaa.lobby.score;

import cat.nyaa.lobby.score.api.ScoreHolder;
import cat.nyaa.lobby.teamsign.TeamManager;
import cat.nyaa.lobby.teamsign.TeamWrapper;
import cat.nyaa.nyaacore.configuration.ISerializable;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreSession implements ISerializable{
    @Serializable
    Map<String, Double> teamScore = new HashMap<>();
    @Serializable
    Map<UUID, Double> personalScore = new HashMap<>();

    public void addScore(OfflinePlayer player, double score) {
        Double sc = personalScore.computeIfAbsent(player.getUniqueId(), str -> 0d);
        sc += score;
        personalScore.put(player.getUniqueId(), sc);
    }

    public void addScore(Team team, double score) {
        Double sc = teamScore.computeIfAbsent(team.getName(), str -> 0d);
        sc += score;
        teamScore.put(team.getName(), sc);
    }

    public double getScore(OfflinePlayer player) {
        return personalScore.getOrDefault(player.getUniqueId(), 0d);
    }

    public double getTeamScore(Team team) {
        return teamScore.getOrDefault(team.getName(), 0d);
    }

    public double getMemberScore(Team team) {
        TeamWrapper wrappedTeam = TeamManager.getInstance().getWrappedTeam(team.getName());
        double sum = wrappedTeam.getMembers().stream().mapToDouble(uuid -> personalScore.getOrDefault(uuid, 0d)).sum();
        return sum;
    }
}
