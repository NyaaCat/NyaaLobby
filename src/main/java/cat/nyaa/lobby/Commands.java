package cat.nyaa.lobby;

import cat.nyaa.lobby.region.RegionCommands;
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
        regionCommands = new RegionCommands(plugin, _i18n);
    }

    @SubCommand(value = "region", permission = "nyaa.lobby.admin")
    public RegionCommands regionCommands;

    @Override
    public String getHelpPrefix() {
        return "";
    }

}
