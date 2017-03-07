package me.martinitslinda.simplechannels.listener;

import me.martinitslinda.simplechannels.SimpleChannels;
import me.martinitslinda.simplechannels.channel.Channel;
import me.martinitslinda.simplechannels.managers.ChannelManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    private SimpleChannels plugin = SimpleChannels.get();
    private ChannelManager channelManager = plugin.getChannelManager();

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        Channel channel = channelManager.getActiveChannel(player);
        if (channel != null) {
            channel.sendMessage(player, event.getMessage());
            event.setCancelled(true);
        }

    }

}
