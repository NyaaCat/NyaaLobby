package cat.nyaa.lobby.score.api;

import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Team;

public interface ScoreHolder {

    void addScore(OfflinePlayer player, double score);
    void addScore(Team team, double score);

    double getScore(OfflinePlayer player);
    /**
     * return total score of a team.
     * total score is the combination of all members and team score.
     * @param team team to query
     * @return the combination of all members and team score
     */
    double getScore(Team team);

    /**
     * return [team score], which is a part of total score
     * Team members' personal score is not included.
     * @param team team to query
     * @return score in the team. Team members' personal score is not included.
     */
    double getTeamScore(Team team);

    /**
     * return [member score], which is a part of total score
     * Team's score is not included.
     * @param team team to query
     * @return score of the combination of all members in the team. Team's score is not included.
     */
    double getMemberScore(Team team);
}
