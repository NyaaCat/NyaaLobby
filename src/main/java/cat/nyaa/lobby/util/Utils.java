package cat.nyaa.lobby.util;

import org.bukkit.ChatColor;

public class Utils {
    public static String colored(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
