package cat.nyaa.lobby.score;

import cat.nyaa.lobby.LobbyPlugin;
import cat.nyaa.lobby.score.api.ScoreAPI;
import cat.nyaa.nyaacore.configuration.FileConfigure;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

public class ScoreApiImpl extends FileConfigure implements ScoreAPI {
    @Serializable
    Map<String, ScoreSession> sessionMap = new HashMap<>();

    public static ScoreApiImpl INSTANCE;

    private ScoreApiImpl(){

    }

    public static ScoreApiImpl getInstance(){
        if (INSTANCE == null){
            synchronized (ScoreApiImpl.class){
                if (INSTANCE == null){
                    INSTANCE = new ScoreApiImpl();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void addScore(String session, OfflinePlayer player, double score) {
        ScoreSession session1 = getSession(session);
        session1.addScore(player, score);
    }

    private ScoreSession getSession(String session) {
        return sessionMap.get(session);
    }

    private void asyncSave() {
        new BukkitRunnable(){
            @Override
            public void run() {
                synchronized (ScoreApiImpl.class){
                    ScoreApiImpl.this.save();
                }
            }
        }.runTaskLaterAsynchronously(LobbyPlugin.plugin, 0);
    }

    @Override
    public void addScore(String session, Team team, double score) {
        ScoreSession session1 = getSession(session);
        session1.addScore(team, score);
        asyncSave();
    }

    @Override
    public double getScore(String session, OfflinePlayer player) {
        ScoreSession session1 = getSession(session);
        return session1.getScore(player);
    }

    @Override
    public double getScore(String session, Team team) {
        ScoreSession session1 = getSession(session);
        return session1.getTeamScore(team) + session1.getMemberScore(team);
    }

    @Override
    public double getTeamScore(String session, Team team) {
        ScoreSession session1 = getSession(session);
        return session1.getTeamScore(team);
    }

    @Override
    public double getMemberScore(String session, Team team) {
        ScoreSession session1 = getSession(session);
        return session1.getMemberScore(team);
    }

    @Override
    protected String getFileName() {
        return "score.yml";
    }

    @Override
    protected JavaPlugin getPlugin() {
        return LobbyPlugin.plugin;
    }

    @Override
    public void createSession(String name) {
        sessionMap.put(name, new ScoreSession());
    }

    @Override
    public void dumpSession(String name) {
        ScoreSession scoreSession = sessionMap.get(name);
        if (scoreSession != null) {
            new FileConfigure(){
                @Serializable
                public ScoreSession session = scoreSession;

                @Override
                protected String getFileName() {
                    return "score/dump/"+name+".yml";
                }

                @Override
                protected JavaPlugin getPlugin() {
                    return LobbyPlugin.plugin;
                }
            }.save();
        }
    }

    @Override
    public void removeSession(String name) {
        sessionMap.remove(name);
    }
}
