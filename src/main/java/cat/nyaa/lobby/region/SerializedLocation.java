package cat.nyaa.lobby.region;

import cat.nyaa.nyaacore.configuration.ISerializable;
import org.bukkit.Location;
import org.bukkit.World;

public class SerializedLocation implements ISerializable {
    @Serializable
    String world = "";
    @Serializable
    double x = 0d;
    @Serializable
    double y = 0d;
    @Serializable
    double z = 0d;

    public SerializedLocation(World world, int x, int y, int z) {
        this.world = world.getName();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location getLocation() {
        return null;
    }
}
