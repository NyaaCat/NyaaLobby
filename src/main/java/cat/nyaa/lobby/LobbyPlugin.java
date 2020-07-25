package cat.nyaa.lobby;

import org.bukkit.plugin.java.JavaPlugin;

public class LobbyPlugin extends JavaPlugin {
    public static LobbyPlugin plugin;
    private Configuration configuration;

    @Override
    public void onEnable() {
        super.onEnable();
        plugin = this;

    }

    public Configuration config() {
        return configuration;
    }
}
