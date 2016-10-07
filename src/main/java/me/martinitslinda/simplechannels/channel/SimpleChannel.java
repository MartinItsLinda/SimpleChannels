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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class SimpleChannel implements Channel{

    private String id;
    private String name;
    private String format;
    private String broadcastFormat;
    private UUID creator;
    private Map<UUID, Role> members;
    private String permission;

    public SimpleChannel(String id, String name, String format, String broadcastFormat,
                         UUID creator, Map<UUID, Role> members, String permission){

        if(SimpleChannels.get().getChannelManager().getChannelById(id)!=null){
            throw new IllegalArgumentException("Cannot have duplicate channel id's.");
        }

        this.id=id;
        this.name=name;
        this.format=format;
        this.broadcastFormat=broadcastFormat;
        this.creator=creator;
        this.members=members;
        this.permission=permission;
    }

    @Override
    public String getId(){
        return id;
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public UUID getCreator(){
        return creator;
    }

    @Override
    public Map<UUID, Role> getMembers(){
        return new HashMap<>(members);
    }

    @Override
    public String getFormat(){
        return format;
    }

    @Override
    public void setFormat(String format){
        this.format=format;
    }

    @Override
    public String getBroadcastFormat(){
        return broadcastFormat;
    }

    @Override
    public void setBroadcastFormat(String broadcastFormat){
        this.broadcastFormat=broadcastFormat;
    }

    @Override
    public String getPermission(){
        return permission;
    }

    @Override
    public void sendMessage(CommandSender sender, String message){

        Preconditions.checkNotNull(sender, "sender");
        Preconditions.checkNotNull(message, "message");
        Preconditions.checkArgument(!(message.isEmpty()), "message empty");

        message=ChatColor.stripColor(message);

        ChannelChatEvent event=new ChannelChatEvent(this, sender, message, getMembers());

        Bukkit.getPluginManager().callEvent(event);

        if(event.isCancelled()) return;

        for(UUID recipient : event.getRecipients().keySet()){
            Player player=Bukkit.getPlayer(recipient);

            if(player==null) continue;

            player.sendMessage(translateAlternateColorCodes('&',
                    MessageFormat.format(event.getMessage(), getFormat(), getName(), event.getSender())));

        }

    }

    @Override
    public void broadcast(String message){

        Preconditions.checkNotNull(message, "message");
        Preconditions.checkArgument(!(message.isEmpty()), "message empty");

        ChannelBroadcastEvent event=new ChannelBroadcastEvent(this, message);

        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled()) return;

        for(UUID recipient : getMembers().keySet()){
            Player player=Bukkit.getPlayer(recipient);

            if(player==null) continue;

            player.sendMessage(translateAlternateColorCodes('&',
                    MessageFormat.format(event.getMessage(), getFormat(), getName())));
        }

    }

    @Override
    public void add(Player player){
        if(!(members.containsKey(Preconditions.checkNotNull(player).getUniqueId())))
            members.put(player.getUniqueId(), Role.USER);
    }

    @Override
    public void remove(Player player){
        if(members.containsKey(Preconditions.checkNotNull(player).getUniqueId())) members.remove(player.getUniqueId());
    }

    @Override
    public String toString(){
        return "SimpleChannel{"+
                "id='"+id+'\''+
                ", name='"+name+'\''+
                ", format='"+format+'\''+
                ", broadcastFormat='"+broadcastFormat+'\''+
                ", creator="+creator+
                ", members="+members+
                ", permission='"+permission+'\''+
                '}';
    }
}
