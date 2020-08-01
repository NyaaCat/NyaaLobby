package cat.nyaa.lobby;

import cat.nyaa.lobby.lobby.LobbyManager;
import cat.nyaa.lobby.teamsign.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyPlugin extends JavaPlugin {
    public static LobbyPlugin plugin;
    private Configuration configuration;
    private Commands commands;
    private I18n i18n;
    private Events events;

    @Override
    public void onEnable() {
        super.onEnable();
        plugin = this;
        reload();
        commands = new Commands(this, i18n);
        events = new Events();
        Bukkit.getPluginCommand("nlobby").setExecutor(commands);
        Bukkit.getPluginManager().registerEvents(events, this);
    }

    private void reload() {
        this.configuration = new Configuration();
        configuration.load();
        i18n = new I18n(this, configuration.language);
        LobbyManager.getInstance().load();
        TeamManager.getInstance().load();
    }

    public Configuration config() {
        return configuration;
    }
}
