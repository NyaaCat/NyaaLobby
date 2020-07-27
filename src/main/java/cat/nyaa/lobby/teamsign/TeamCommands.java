package cat.nyaa.lobby.teamsign;

import cat.nyaa.lobby.I18n;
import cat.nyaa.lobby.util.Utils;
import cat.nyaa.nyaacore.ILocalizer;
import cat.nyaa.nyaacore.Message;
import cat.nyaa.nyaacore.cmdreceiver.Arguments;
import cat.nyaa.nyaacore.cmdreceiver.CommandReceiver;
import cat.nyaa.nyaacore.cmdreceiver.SubCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
        Player player = asPlayer(sender);
        double radius = Utils.getRadius(arguments);
        String teamName = arguments.nextString();
        TeamWrapper wrappedTeam = TeamManager.getInstance().getWrappedTeam(teamName);
        if (wrappedTeam == null) {
            new Message(I18n.format("team.set_spawn.no_team", teamName)).send(sender);
        }
        Location location = player.getLocation();
        wrappedTeam.setSpawnPoint(location, radius);
    }

    @Override
    public String getHelpPrefix() {
        return "team";
    }
}
