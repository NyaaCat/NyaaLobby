package cat.nyaa.lobby.lobby;

import cat.nyaa.lobby.score.ScoreSession;
import cat.nyaa.lobby.score.api.ScoreAPI;
import cat.nyaa.lobby.util.Utils;
import cat.nyaa.nyaacore.configuration.ISerializable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Lobby implements ISerializable {
    @Serializable
    public String name = "";
    @Serializable
    SerializedSpawnPoint spawnPoint;

    private ScoreSession scoreSession;

    public Lobby(){}

    public Lobby(String name, SerializedSpawnPoint spawnPoint) {
        this.name = name;
        this.spawnPoint = spawnPoint;
        scoreSession = ScoreAPI.getImpl().getOrCreateSession(name);
    }

    @Override
    public void deserialize(ConfigurationSection config) {
        ISerializable.deserialize(config, this);
        scoreSession = ScoreAPI.getImpl().getOrCreateSession(name);
    }

    public ScoreSession getScoreSession() {
        return scoreSession;
    }

    public void teleportPlayer(Player player) {
        Utils.safeTeleport(spawnPoint, player);
    }

    public String getName() {
        return name;
    }
}
