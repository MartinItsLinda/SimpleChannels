package me.martinitslinda.simplechannels.event;

import me.martinitslinda.simplechannels.channel.Channel;

import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class ChannelPlayerInviteEvent extends ChannelEvent {

    private Player player;
    private Player target;
    private Set<UUID> recipients;

    public ChannelPlayerInviteEvent(Player player, Player target, Set<UUID> recipients, Channel channel) {
        super(channel);
        this.player = player;
        this.target = target;
        this.recipients = recipients;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getTarget() {
        return target;
    }

    public Set<UUID> getRecipients() {
        return recipients;
    }
}
