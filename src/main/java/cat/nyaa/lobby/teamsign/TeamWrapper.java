package cat.nyaa.lobby.teamsign;

import cat.nyaa.lobby.I18n;
import cat.nyaa.lobby.LobbyPlugin;
import cat.nyaa.nyaacore.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamWrapper {
    private Team team;
    private List<Player> members = new ArrayList<>();
    private Player leader;
    private int limit;

    public TeamWrapper(Team team, Player creator){
        this(team);
        this.members.add(creator);
        this.leader = creator;
    }


    public TeamWrapper(Team team) {
        this.team = team;
        this.limit = LobbyPlugin.plugin.config().teamLimit;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public Player getLeader() {
        return leader;
    }

    public void setLeader(Player leader) {
        this.leader = leader;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getMembersInTeam() {
        return members.size();
    }

    public boolean isFull() {
        return getMembersInTeam() >= limit;
    }

    public void playerJoin(Player player){
        TeamManager instance = TeamManager.getInstance();
        TeamWrapper playerTeam = instance.getPlayerTeam(player);
        if (playerTeam != null) {
            new Message(I18n.format("team.join.already_in_team")).send(player);
            return;
        }
        if (members.isEmpty()){
            leader = player;
        }
        members.add(player);
    }

    private Scoreboard getScoreBoard() {
        return Bukkit.getScoreboardManager().getMainScoreboard();
    }
}
