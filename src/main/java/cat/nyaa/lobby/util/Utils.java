package cat.nyaa.lobby.util;

import cat.nyaa.lobby.LobbyPlugin;
import cat.nyaa.nyaacore.cmdreceiver.Arguments;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

public class Utils {
    public static String colored(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static double getRadius(Arguments arguments) {
        return arguments.top() == null ? LobbyPlugin.plugin.config().defaultRadius : arguments.nextDouble();
    }

    public static Location findSafePlace(Location targetLoc) {
        Location solidBlockDown = findSolidBlockDown(targetLoc);
        Location airUp = findSpaceUp(solidBlockDown, 2);
        airUp.setY(airUp.getBlockY());
        return airUp;
    }


    /**
     * find a n-block high stand space to put a player in it
     * @param from from which block to search
     * @return bottom location that have n-1 stand space above.
     */
    private static Location findSpaceUp(Location from, int height) {
        Location loc = from.clone().add(0, 1, 0);

        while (loc.getY() < 256 && !isNBlockSpace(loc, height)){
            loc.add(0,1,0);
        }
        return loc;
    }

    private static boolean isNBlockSpace(Location loc, int height) {
        for (int i = 0; i < height; i++) {
            Material type = loc.clone().add(0, i, 0).getBlock().getType();
            boolean valid = !type.isSolid();
            if (!valid){
                return false;
            }
        }
        return true;
    }

    private static Location findSolidBlockDown(Location from) {
        Location loc = from.clone();

        while (loc.getY() > 0 && !loc.getBlock().getType().isSolid()){
            loc.add(0,-1,0);
        }
        return loc;
    }
}
