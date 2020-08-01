package cat.nyaa.lobby.team;

import cat.nyaa.lobby.I18n;
import cat.nyaa.lobby.LobbyPlugin;
import cat.nyaa.lobby.lobby.Lobby;
import cat.nyaa.lobby.lobby.LobbyManager;
import cat.nyaa.lobby.lobby.SerializedLocation;
import cat.nyaa.lobby.lobby.SerializedSpawnPoint;
import cat.nyaa.lobby.util.Utils;
import cat.nyaa.nyaacore.Message;
import cat.nyaa.nyaacore.configuration.FileConfigure;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamWrapper {
    private static final String DIR_TEAM = "teams";
    private Team team;
    private List<UUID> members = new ArrayList<>();
    private List<UUID> logoutMembers = new ArrayList<>();
    private Player leader;
    private int limit;
    private Lobby lobby;
    private SpawnConfig spawnConfig;

    public void load() {
        spawnConfig.load();
    }

    public boolean isMember(Player player) {
        return members.contains(player.getUniqueId());
    }

    public static class SpawnConfig extends FileConfigure{
        @Serializable
        String team;
        @Serializable
        SerializedSpawnPoint spawnPoint = null;

        private SpawnConfig(String team){
            this.team = team;
        }

        public SerializedSpawnPoint getSpawnPoint() {
            return spawnPoint;
        }

        public void setSpawnPoint(SerializedSpawnPoint spawnPoint) {
            this.spawnPoint = spawnPoint;
        }

        @Override
        protected String getFileName() {
            return DIR_TEAM +"/"+team+".yml";
        }

        @Override
        protected JavaPlugin getPlugin() {
            return LobbyPlugin.plugin;
        }
    }

    public TeamWrapper(Team team) {
        this.team = team;
        this.limit = LobbyPlugin.plugin.config().teamLimit;
        this.lobby = LobbyManager.getInstance().getDefaultLobby();
        spawnConfig = new SpawnConfig(team.getName());
        this.load();
    }

    public TeamWrapper(Team team, Player creator){
        this(team);
        this.members.add(creator.getUniqueId());
        this.leader = creator;
    }


    public void setTeam(Team team) {
        this.team = team;
        spawnConfig.team = team.getName();
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

    public List<UUID> getMembers(){
        return new ArrayList<>(members);
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public void playerJoin(OfflinePlayer player){
        TeamManager instance = TeamManager.getInstance();
        TeamWrapper playerTeam = instance.getPlayerTeam(player);
        if (playerTeam != null) {
            new Message(I18n.format("team.join.already_in_team")).send(player);
            return;
        }
        if (members.isEmpty() && player.isOnline()){
            leader = player.getPlayer();
        }
        String name = player.isOnline() ? player.getPlayer().getDisplayName() : player.getName();
        Message message = new Message(I18n.format("team.join.message.members", name));
        Message messageJoinedPlayer = new Message(I18n.format("team.join.message.joined_player", team.getDisplayName()));
        logoutMembers.remove(player.getUniqueId());
        members.stream().map(Bukkit::getOfflinePlayer).forEach(message::send);
        messageJoinedPlayer.send(player);
        team.addEntry(player.getName());
        members.add(player.getUniqueId());
    }

    public boolean isLogoutMember(Player player){
        return logoutMembers.contains(player.getUniqueId());
    }

    public void teleportToLobby(){
        members.stream().map(Bukkit::getOfflinePlayer).filter(OfflinePlayer::isOnline)
                .map(OfflinePlayer::getPlayer)
                .forEach(player -> {
                    lobby.teleportPlayer(player);
                });
    }

    private Scoreboard getScoreBoard() {
        return Bukkit.getScoreboardManager().getMainScoreboard();
    }

    public void playerLeave(OfflinePlayer player, boolean isLoggedOut) {
        if (player == null){
            return;
        }
        members.remove(player);
        if (isLoggedOut){
            logoutMembers.add(player.getUniqueId());
        }
        String name = player.isOnline() ? player.getPlayer().getDisplayName() : player.getName();
        Message message = new Message(I18n.format("team.leave.message.members", name));
        Message messageLeftPlayer = new Message(I18n.format("team.leave.message.left_player", team.getDisplayName()));
        if (!isLoggedOut){
            messageLeftPlayer.send(player);
        }
        members.stream().map(Bukkit::getOfflinePlayer).forEach(message::send);
        String name1 = player.getName();
        if (name1 == null)return;
        team.removeEntry(name1);
    }

    public void setSpawnPoint(Location location, double radius) {
        this.spawnConfig.setSpawnPoint(new SerializedSpawnPoint(new SerializedLocation(location), radius));
        spawnConfig.save();
    }

    public void teleport(Player player) {
        if (spawnConfig.spawnPoint == null){
            return;
        }
        Utils.safeTeleport(spawnConfig.getSpawnPoint(), player);
    }
}
