package cat.nyaa.lobby.teamsign;

import cat.nyaa.lobby.I18n;
import cat.nyaa.lobby.region.SerializedLocation;
import cat.nyaa.lobby.util.Utils;
import cat.nyaa.nyaacore.Message;
import cat.nyaa.nyaacore.configuration.ISerializable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;

public class TeamSign implements ISerializable {
    Sign sign;
    private boolean valid = false;

    @Serializable
    SerializedLocation location;
    @Serializable
    String team;

    @Override
    public void deserialize(ConfigurationSection config) {
        ISerializable.deserialize(config, this);
        rebuildSign();
    }

    private void rebuildSign(){
        Location l1 = location.getLocation();
        Block block = l1.getBlock();
        if (!(block.getState() instanceof Sign)) {
            return;
        }
        this.sign = (Sign) block.getState();
        updateSign();
        valid = true;
    }

    public void onClickSign(Player player){
        Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team entryTeam = mainScoreboard.getEntryTeam(player.getName());
        if (entryTeam != null){
            new Message(I18n.format("sign.click.already_in_team", entryTeam.getDisplayName())).send(player);
            return;
        }
        Team team = mainScoreboard.getTeam(this.team);
        if (team == null){
            new Message(I18n.format("sign.click.invalid_team", this.team)).send(player);
            return;
        }
        TeamWrapper wrappedTeam = TeamManager.getInstance().getWrappedTeam(team, player);
        if (wrappedTeam.isFull()) {
            new Message(I18n.format("sign.click.team_full", this.team)).send(player);
            return;
        }

    }

    public void updateSign(){
        Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = mainScoreboard.getTeam(this.team);
        TeamWrapper wrappedTeam = TeamManager.getInstance().getWrappedTeam(team);
        ChatColor color = ChatColor.WHITE;
        if (team!=null){
            color = team.getColor();
        }

        String title = Utils.colored(String.format("[&" + color.getChar() + "TEAM %s&r]", team));
        String leader = Utils.colored(I18n.format("sign.leader.no_leader"));
        String members;
        String hint = Utils.colored(I18n.format("sign.hint.click_to_join"));
        if (wrappedTeam != null && wrappedTeam.getLeader() != null) {
            String displayName = wrappedTeam.getLeader().getName();
            leader = Utils.colored(I18n.format("sign.leader.leader", displayName));
        }

        if (wrappedTeam == null){
            members = Utils.colored(I18n.format("sign.members", 0, 0));
        }else {
            int membersInTeam = wrappedTeam.getMembersInTeam();
            int limit = wrappedTeam.getLimit();
            members = Utils.colored(I18n.format("sign.members", membersInTeam, limit));
        }
        if (wrappedTeam != null && wrappedTeam.isFull()){
            hint = Utils.colored(I18n.format("sign.hint.full"));
        }

        sign.setLine(0, title);
        sign.setLine(1, leader);
        sign.setLine(2, members);
        sign.setLine(3, leader);
    }

    public boolean isValid() {
        return valid;
    }
}
