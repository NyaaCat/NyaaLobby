package cat.nyaa.lobby.lobby;

import cat.nyaa.lobby.util.Utils;
import cat.nyaa.nyaacore.configuration.ISerializable;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public class Lobby implements ISerializable {
    @Serializable
    SerializedSpawnPoint spawnPoint;

    public Lobby(SerializedSpawnPoint spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    public void teleportPlayer(Player player) {
        Location location = spawnPoint.center.getLocation();
        double r = ThreadLocalRandom.current().nextDouble(spawnPoint.getRadius());
        double phi = ThreadLocalRandom.current().nextDouble(2*Math.PI);
        Vector yAxis = new Vector(0, 1, 0);
        Vector bias = new Vector(1, 0, 1).normalize().multiply(r).rotateAroundAxis(yAxis, phi);
        Location targetLoc = location.clone().add(bias);
        Location safePlace = Utils.findSafePlace(targetLoc);
        player.teleport(safePlace);
    }
}
