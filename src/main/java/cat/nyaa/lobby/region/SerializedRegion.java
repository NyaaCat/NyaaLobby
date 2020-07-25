package cat.nyaa.lobby.region;

import cat.nyaa.nyaacore.configuration.ISerializable;
import org.bukkit.World;

public class SerializedRegion implements ISerializable {
    @Serializable
    SerializedLocation location1;
    @Serializable
    SerializedLocation location2;

    public SerializedRegion(World world, int x, int x1, int y, int y1, int z, int z1) {
        location1 = new SerializedLocation(world, x, y, z);
        location2 = new SerializedLocation(world, x1, y1, z1);
    }
}
