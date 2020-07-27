package cat.nyaa.lobby.teamsign;

import cat.nyaa.nyaacore.ILocalizer;
import cat.nyaa.nyaacore.cmdreceiver.Arguments;
import cat.nyaa.nyaacore.cmdreceiver.CommandReceiver;
import cat.nyaa.nyaacore.cmdreceiver.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class TeamCommands extends CommandReceiver {
    /**
     * @param plugin for logging purpose only
     * @param _i18n
     */
    public TeamCommands(Plugin plugin, ILocalizer _i18n) {
        super(plugin, _i18n);
    }

    @SubCommand(value = "setSpawn", permission = "nyaa.lobby.admin")
    public void onSetSpawn(CommandSender sender, Arguments arguments){
        String teamName = arguments.nextString();

    }

    @Override
    public String getHelpPrefix() {
        return "team";
    }
}
