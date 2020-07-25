package cat.nyaa.lobby.region;

import cat.nyaa.nyaacore.cmdreceiver.BadCommandException;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class RegionHandler {
    private static RegionHandler INSTANCE;

    private RegionHandler() {
    }

    public static RegionHandler getInstance() {
        if (INSTANCE == null) {
            synchronized (RegionHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RegionHandler();
                }
            }
        }
        return INSTANCE;
    }

    public SerializedRegion getRegion(Player sender){
        LocalSession session = WorldEdit.getInstance().getSessionManager().get(new BukkitPlayer(sender));
        World world = sender.getWorld();
        Region selection = null;
        try {
            selection = session.getSelection(new BukkitWorld(world));
            BlockVector3 minimumPoint = selection.getMinimumPoint();
            BlockVector3 maximumPoint = selection.getMaximumPoint();
            SerializedRegion region = new SerializedRegion(world,
                    minimumPoint.getX(), maximumPoint.getX(),
                    minimumPoint.getY(), maximumPoint.getY(),
                    minimumPoint.getZ(), maximumPoint.getZ());
            return region;
        } catch (IncompleteRegionException e) {
            throw new BadCommandException("exception getting region",e);
        }
    }
}
