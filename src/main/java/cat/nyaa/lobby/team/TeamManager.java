package cat.nyaa.lobby.team;

import cat.nyaa.lobby.LobbyPlugin;
import cat.nyaa.nyaacore.configuration.FileConfigure;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import java.util.*;

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

    private Map<String, TeamWrapper> teamWrapperMap = new WeakHashMap<>();
    private TeamSignManager teamSignManager = new TeamSignManager();

    public void createSign(TeamSign teamSign) {
        teamSignManager.signs.put(teamSign.team, teamSign);
        teamSignManager.save();
    }

    public void onSignClicked(BlockState state, Player player) {
        Location location = state.getLocation();
        teamSignManager.signs.values().stream()
                .filter(teamSign -> teamSign.sign.getBlock().getLocation().equals(location))
                .limit(1)
                .forEach(teamSign -> teamSign.onClickSign(player));
    }

    public static class TeamSignManager extends FileConfigure {
        @Serializable
        Map<String, TeamSign> signs = new HashMap<>();

        public TeamSignManager(){}

        @Override
        protected String getFileName() {
            return "sign.yml";
        }

        @Override
        protected JavaPlugin getPlugin() {
            return LobbyPlugin.plugin;
        }
    }

    public TeamWrapper getPlayerTeam(OfflinePlayer player){
        String name = player.getName();
        if (name == null)return null;
        Team entryTeam = Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(name);
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

    public void leaveTeam(OfflinePlayer player){
        leaveTeam(player, false);
    }

    public void leaveTeam(OfflinePlayer player, boolean isQuit) {
        TeamWrapper playerTeam = getPlayerTeam(player);
        if (playerTeam != null) {
            playerTeam.playerLeave(player, isQuit);
        }
    }

    public TeamWrapper getPlayerLoggedOutTeam(Player player) {
        return teamWrapperMap.values().stream().filter(teamWrapper -> teamWrapper.isMember(player) || teamWrapper.isLogoutMember(player)).findFirst().orElse(null);
    }

    public TeamWrapper getWrappedTeam(String teamName) {
        Team entryTeam = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
        return getWrappedTeam(entryTeam);
    }

    public void load() {
        teamWrapperMap.values().forEach(teamWrapper -> teamWrapper.load());
        teamSignManager.load();
    }
}
