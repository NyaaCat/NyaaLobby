package cat.nyaa.lobby.teamsign;

import cat.nyaa.lobby.I18n;
import cat.nyaa.lobby.lobby.LobbyManager;
import cat.nyaa.lobby.util.Utils;
import cat.nyaa.nyaacore.ILocalizer;
import cat.nyaa.nyaacore.Message;
import cat.nyaa.nyaacore.cmdreceiver.Arguments;
import cat.nyaa.nyaacore.cmdreceiver.CommandReceiver;
import cat.nyaa.nyaacore.cmdreceiver.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        String teamName = arguments.nextString();
        double radius = Utils.getRadius(arguments);
        TeamWrapper wrappedTeam = TeamManager.getInstance().getWrappedTeam(teamName);
        if (wrappedTeam == null) {
            new Message(I18n.format("team.set_spawn.no_team", teamName)).send(sender);
        }
        Location location = player.getLocation();
        wrappedTeam.setSpawnPoint(location, radius);
        new Message(I18n.format("team.set_spawn.success", teamName)).send(sender);
    }

    @SubCommand(value = "createSign", permission = "nyaa.lobby.admin", tabCompleter = "createCompleter")
    public void onCreateSign(CommandSender sender, Arguments arguments){
        String teamName = arguments.nextString();
        Player player = asPlayer(sender);

        Block targetBlock = player.getTargetBlock(null, 10);
        if (!(targetBlock.getState() instanceof Sign)){
            new Message(I18n.format("sign.create.not_sign")).send(sender);
            return;
        }

        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
        if (team == null){
            new Message(I18n.format("sign.create.no_team", teamName)).send(sender);
            return;
        }
        TeamSign teamSign = new TeamSign(((Sign) targetBlock.getState()), team);
        TeamManager.getInstance().createSign(teamSign);
        teamSign.updateSign();
        new Message(I18n.format("sign.create.success", teamName)).send(sender);
    }

    @SubCommand(value = "leave", permission = "nyaa.lobby.admin", tabCompleter = "playerCompleter")
    public void onLeave(CommandSender sender, Arguments arguments){
        OfflinePlayer offlinePlayer = arguments.top() == null ? asPlayer(sender) : arguments.nextOfflinePlayer();
        TeamManager.getInstance().leaveTeam(offlinePlayer);
        LobbyManager.getInstance().getDefaultLobby().teleportPlayer(offlinePlayer.getPlayer());
        new Message(I18n.format("team.command.leave.success", offlinePlayer.getName())).send(sender);
    }

    @SubCommand(value = "join", permission = "nyaa.lobby.admin", tabCompleter = "namePlayerCompleter")
    public void onJoin(CommandSender sender, Arguments arguments){
        String teamName = arguments.nextString();
        OfflinePlayer offlinePlayer = arguments.top() == null ? asPlayer(sender) : arguments.nextOfflinePlayer();
        TeamWrapper wrappedTeam = TeamManager.getInstance().getWrappedTeam(teamName);
        if (wrappedTeam == null){
            new Message(I18n.format("team.command.join.no_team", teamName)).send(sender);
            return;
        }
        wrappedTeam.playerJoin(offlinePlayer);
        if (offlinePlayer.isOnline()) {
            wrappedTeam.teleport(offlinePlayer.getPlayer());
        }
        new Message(I18n.format("team.command.join.success", teamName, offlinePlayer.getName())).send(sender);
    }
    public List<String> playerCompleter(CommandSender sender, Arguments arguments){
        ArrayList<String> strs = new ArrayList<>();
        switch (arguments.remains()){
            case 1:
                strs.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
                break;
        }
        return Utils.filtered(arguments, strs);
    }

    public List<String> namePlayerCompleter(CommandSender sender, Arguments arguments){
        ArrayList<String> strs = new ArrayList<>();
        switch (arguments.remains()){
            case 1:
                strs.addAll(LobbyManager.getInstance().getLobbyNames());
                break;
            case 2:
                strs.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
                break;
        }
        return Utils.filtered(arguments, strs);
    }

    public List<String> createCompleter(CommandSender sender, Arguments arguments){
        List<String> strs = new ArrayList<>();
        return strs;
    }

    @Override
    public String getHelpPrefix() {
        return "team";
    }
}
