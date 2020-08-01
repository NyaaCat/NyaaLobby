package cat.nyaa.lobby.lobby;

import cat.nyaa.lobby.util.Utils;
import cat.nyaa.nyaacore.configuration.ISerializable;
import org.bukkit.entity.Player;

public class Lobby implements ISerializable {
    @Serializable
    SerializedSpawnPoint spawnPoint;

    public Lobby(){}

    public Lobby(SerializedSpawnPoint spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    public void teleportPlayer(Player player) {
        Utils.safeTeleport(spawnPoint, player);
    }
}
