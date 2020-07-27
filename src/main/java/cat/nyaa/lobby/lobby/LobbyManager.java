package cat.nyaa.lobby.lobby;

import cat.nyaa.lobby.LobbyPlugin;
import cat.nyaa.nyaacore.configuration.FileConfigure;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LobbyManager {
    private static LobbyManager INSTANCE;

    private LobbyManager() {
        lobbyHolder = new LobbyHolder();
        lobbyHolder.load();
    }

    public static LobbyManager getInstance() {
        if (INSTANCE == null) {
            synchronized (LobbyManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LobbyManager();
                }
            }
        }
        return INSTANCE;
    }

    private LobbyHolder lobbyHolder;
    private Map<UUID, Lobby> lobbyMap = new HashMap<>();

    public void setTeamSpawnPoint(Team team, SerializedSpawnPoint region){
        lobbyHolder.setTeamSpawnPoint(team, region);
        lobbyHolder.save();
    }

    public void setLobby(String lobbyName, SerializedSpawnPoint region) {
        lobbyHolder.setLobby(lobbyName, region);
        if ("".equals(lobbyHolder.defaultLobby)){
            lobbyHolder.setDefaultLobby(lobbyName);
        }
        lobbyHolder.save();
    }

    public boolean hasLobby(String def) {
        return lobbyHolder.lobby.containsKey(def);
    }

    public boolean removeLobby(String lobbyName) {
        return lobbyHolder.removeLobby(lobbyName);
    }

    public void setDefaultLobby(String def) {

    }

    public Lobby getLobby(String lobby) {
        return lobbyHolder.getLobby(lobby);
    }

    public static class LobbyHolder extends FileConfigure {
        @Serializable
        String defaultLobby = "";
        @Serializable
        Map<String, Lobby> lobby = new HashMap<>();
        @Serializable
        Map<String, Lobby> teamSpawnPoint = new HashMap<>();


        @Override
        protected String getFileName() {
            return "regions.yml";
        }

        @Override
        protected JavaPlugin getPlugin() {
            return LobbyPlugin.plugin;
        }

        public void setLobby(String name, SerializedSpawnPoint spawnPoint) {
            this.lobby.put(name, new Lobby(spawnPoint));
        }

        public boolean removeLobby(String name){
            return this.lobby.remove(name) != null;
        }

        public void setTeamSpawnPoint(Team team, SerializedSpawnPoint spawnPoint) {
            String name = team.getName();
            teamSpawnPoint.put(name, new Lobby(spawnPoint));
        }

        public boolean removeTeamSpawnRegion(Team team){
            return teamSpawnPoint.remove(team.getName()) != null;
        }

        public void setDefaultLobby(String lobbyName) {
            this.defaultLobby = lobbyName;
        }

        public Lobby getLobby(String lobby) {
            return this.lobby.get(lobby);
        }
    }
}
