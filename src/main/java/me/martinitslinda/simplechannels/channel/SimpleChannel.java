/*
 * Copyright (C) 2016 MartinItsLinda.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.martinitslinda.simplechannels.channel;

import com.google.common.base.Preconditions;

import me.martinitslinda.simplechannels.SimpleChannels;
import me.martinitslinda.simplechannels.event.ChannelBroadcastEvent;
import me.martinitslinda.simplechannels.event.ChannelChatEvent;
import me.martinitslinda.simplechannels.event.ChannelPlayerInviteEvent;
import me.martinitslinda.simplechannels.managers.RequestManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class SimpleChannel implements Channel {

    private String id;
    private String name;
    private UUID creatorUUID;
    private Set<UUID> members;
    private String permission;

    public SimpleChannel(String id, String name, UUID creatorUUID, Set<UUID> members, String permission) {

        if (SimpleChannels.get().getChannelManager().getChannelById(id) != null) {
            throw new IllegalArgumentException("Cannot have duplicate channel id's.");
        }

        this.id = id;
        this.name = name;
        this.creatorUUID = creatorUUID;
        this.members = members;
        this.permission = permission;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getCreatorUUID() {
        return creatorUUID;
    }

    @Override
    public Set<UUID> getMembers() {
        return new HashSet<>(members);
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public void sendMessage(Player sender, String message) {

        Preconditions.checkNotNull(sender, "sender");
        Preconditions.checkNotNull(message, "message");
        Preconditions.checkArgument(!(message.isEmpty()), "message empty");

        message = ChatColor.stripColor(message);

        ChannelChatEvent event = new ChannelChatEvent(this, sender, message, getMembers());

        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) return;

        for (UUID recipient : event.getRecipients()) {
            Player player = Bukkit.getPlayer(recipient);

            if (player == null) continue;

            String response = ChatColor.translateAlternateColorCodes('&', "&6{player}: {message}");
            response = response.replace("{player}", sender.getName());
            response = response.replace("{message}", message);
            player.sendMessage(response);

        }

    }

    @Override
    public void broadcast(String message) {

        Preconditions.checkNotNull(message, "message");
        Preconditions.checkArgument(!(message.isEmpty()), "message empty");

        ChannelBroadcastEvent event = new ChannelBroadcastEvent(this, message);

        Bukkit.getPluginManager().callEvent(event);

        String broadcastMessage = ChatColor.translateAlternateColorCodes('&',
                "&e{channel} | {message}");
        broadcastMessage = broadcastMessage.replace("{channel}", getName());
        broadcastMessage = broadcastMessage.replace("{message}", message);

        for (UUID recipient : getMembers()) {
            Player player = Bukkit.getPlayer(recipient);

            if (player == null) continue;

            player.sendMessage(broadcastMessage);
        }

    }

    @Override
    public boolean isMember(Player player) {
        Preconditions.checkNotNull(player, "player");
        for (UUID uuid : members) {
            if (uuid.equals(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void add(UUID uuid) {
        if (!(members.contains(uuid)))
            members.add(uuid);
    }

    @Override
    public void remove(UUID uuid) {
        if (members.contains(uuid))
            members.remove(uuid);
    }

    @Override
    public boolean isAdministrator(Player player) {
        return player.getUniqueId().equals(creatorUUID);
    }

    @Override
    public void invite(Player sender, Player target) {

        Preconditions.checkNotNull(sender, "sender");
        Preconditions.checkNotNull(target, "target");

        if (!isAdministrator(sender)) {
            String reply = ChatColor.translateAlternateColorCodes('&', "&cYou must be at least an Administrator " +
                    "or above to invite people into this channel.");
            sender.sendMessage(reply);
            return;
        }

        if (isMember(target)) {
            String reply = ChatColor.translateAlternateColorCodes('&',
                    "&c{player} is already a member of this channel.");
            reply = reply.replace("{player}", target.getName());
            sender.sendMessage(reply);
            return;
        }

        RequestManager requestManager = SimpleChannels.get().getRequestManager();

        if (requestManager.hasRequestTo(target.getUniqueId(), this)) {
            String response = ChatColor.translateAlternateColorCodes('&',
                    "&c{player} already has a pending invite from {channel}");
            response = response.replace("{player}", target.getName());
            response = response.replace("{channel}", getName());
            sender.sendMessage(response);
            return;
        }

        ChannelPlayerInviteEvent event = new ChannelPlayerInviteEvent(sender, target, getMembers(), this);
        Bukkit.getServer().getPluginManager().callEvent(event);

        requestManager.sendRequestTo(target, this);

        for (UUID recipient : event.getRecipients()) {
            Player player = Bukkit.getPlayer(recipient);

            if (player == null) continue;

            String response = ChatColor.translateAlternateColorCodes('&', "&a{sender} has invited {target} to " +
                    "join {channel}");
            response = response.replace("{sender}", sender.getName());
            response = response.replace("{target}", target.getName());
            response = response.replace("{channel}", getName());

            player.sendMessage(response);

        }

    }

}
