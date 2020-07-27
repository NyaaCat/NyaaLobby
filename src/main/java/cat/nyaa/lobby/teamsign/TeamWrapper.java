package cat.nyaa.lobby.teamsign;

import cat.nyaa.lobby.I18n;
import cat.nyaa.lobby.LobbyPlugin;
import cat.nyaa.lobby.lobby.Lobby;
import cat.nyaa.nyaacore.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamWrapper {
    private Team team;
    private List<Player> members = new ArrayList<>();
    private List<UUID> logoutMembers = new ArrayList<>();
    private Player leader;
    private int limit;
    private Lobby lobby;

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

    public List<Player> getMembers(){
        return new ArrayList<>(members);
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
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
        Message message = new Message(I18n.format("team.join.message.members", player.getDisplayName()));
        Message messageJoinedPlayer = new Message(I18n.format("team.join.message.joined_player", team.getDisplayName()));
        logoutMembers.remove(player.getUniqueId());
        members.forEach(message::send);
        messageJoinedPlayer.send(player);
        members.add(player);
    }

    public boolean isLogoutMember(Player player){
        return logoutMembers.contains(player.getUniqueId());
    }

    private Scoreboard getScoreBoard() {
        return Bukkit.getScoreboardManager().getMainScoreboard();
    }

    public void playerLeave(Player player, boolean isLoggedOut) {
        members.remove(player);
        if (isLoggedOut){
            logoutMembers.add(player.getUniqueId());
        }
        Message message = new Message(I18n.format("team.leave.message.members", player.getDisplayName()));
        Message messageLeftPlayer = new Message(I18n.format("team.leave.message.left_player", team.getDisplayName()));
        if (!isLoggedOut){
            messageLeftPlayer.send(player);
        }
        members.forEach(message::send);
    }
}
