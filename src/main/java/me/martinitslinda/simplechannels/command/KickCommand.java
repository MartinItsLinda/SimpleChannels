package me.martinitslinda.simplechannels.command;

import me.martinitslinda.simplechannels.channel.Channel;
import me.martinitslinda.simplechannels.managers.ChannelManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class KickCommand extends ChannelCommand {

    public KickCommand() {
        super("kick", "<player> [channel]", "Kick a player from a channel.");
    }

    @Override
    public void execute(CommandRequest request) {

        if (!request.isPlayer()) {
            String reply = "&cYou must be a player to use this command!";
            request.reply(reply);
            return;
        }

        Player player = (Player) request.getSender();

        String[] args = request.getArgs();
        if (args.length < 2) {
            String reply = "&cCorrect Usage: &7/{command} {usage}";
            reply = reply.replace("{command}", getName());
            reply = reply.replace("{usage}", getUsage());
            request.reply(reply);
            return;
        }

        ChannelManager channelManager = getChannelManager();

        List<Channel> ownedChannels = channelManager.getOwnedChannels(player);
        if (ownedChannels.size() > 1) {
            String reply = "&cAs you're the Owner (or Administrator) of more than 1 channel, " +
                    "you must specify a channel to remove the player from.";
            request.reply(reply);
            return;
        }

        if (ownedChannels.size() == 0) {
            String reply = "&cYou don't own any channels to invite anyone too.";
            request.reply(reply);
            return;
        }

        String channelName = args[1];
        Channel channel = channelManager.getChannelByName(channelName);
        if (channel == null) {
            String reply = "&cUnknown channel \"{channel}\"";
            reply = reply.replace("{channel}", channelName);
            request.reply(reply);
            return;
        }

        if (!channel.isMember(player)) {
            String reply = "&cYou're not a member of {channel}";
            reply = reply.replace("{channel}", channelName);
            request.reply(reply);
            return;
        }

        if (!channel.isAdministrator(player)) {
            String reply = "&cYou must be at least an Administrator " +
                    "or above to kick people from this channel.";
            request.reply(reply);
            return;
        }

        String playerName = args[0];
        OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
        if (target == null) {
            String reply = "&c\"{player}\" isn't a member of {channel}.";
            reply = reply.replace("{player}", playerName);
            reply = reply.replace("{channel}", channel.getName());
            request.reply(reply);
            return;
        }

        channel.remove(target.getUniqueId());

        String broadcast = ChatColor.translateAlternateColorCodes('&',
                "&c{player} has been kicked from {channel}");
        broadcast = broadcast.replace("{player}", target.getName());
        broadcast = broadcast.replace("{channel}", channel.getName());

        channel.broadcast(broadcast);

    }

}
