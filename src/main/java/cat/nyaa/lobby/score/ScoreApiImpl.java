package cat.nyaa.lobby.score;

import cat.nyaa.lobby.LobbyPlugin;
import cat.nyaa.lobby.score.api.ScoreAPI;
import cat.nyaa.nyaacore.configuration.FileConfigure;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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

    public void asyncSave() {
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
    protected String getFileName() {
        return "score.yml";
    }

    @Override
    protected JavaPlugin getPlugin() {
        return LobbyPlugin.plugin;
    }

    @Override
    public ScoreSession getOrCreateSession(String name) {
        return sessionMap.computeIfAbsent(name, str -> new ScoreSession());
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
