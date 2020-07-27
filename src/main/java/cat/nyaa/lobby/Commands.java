package cat.nyaa.lobby;

import cat.nyaa.lobby.lobby.LobbyCommands;
import cat.nyaa.lobby.teamsign.TeamCommands;
import cat.nyaa.nyaacore.ILocalizer;
import cat.nyaa.nyaacore.cmdreceiver.CommandReceiver;
import cat.nyaa.nyaacore.cmdreceiver.SubCommand;
import org.bukkit.plugin.Plugin;

public class Commands extends CommandReceiver {
    /**
     * @param plugin for logging purpose only
     * @param _i18n
     */
    public Commands(Plugin plugin, ILocalizer _i18n) {
        super(plugin, _i18n);
        lobbyCommands = new LobbyCommands(plugin, _i18n);
        teamCommands = new TeamCommands(plugin, _i18n);
    }

    @SubCommand(value = "lobby", permission = "nyaa.lobby.admin")
    public LobbyCommands lobbyCommands;

    @SubCommand(value = "team", permission = "nyaa.lobby.team")
    public TeamCommands teamCommands;

    @Override
    public String getHelpPrefix() {
        return "nlobby";
    }

}
