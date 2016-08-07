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
import com.google.common.collect.ImmutableSet;
import me.martinitslinda.simplechannels.event.ChannelChatEvent;
import me.martinitslinda.simplechannels.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Channel{

    private long id;
    private String name;
    private UUID creator;
    private boolean requiresInvite;
    private boolean requiresPermission;
    private Set<UUID> members;

    public Channel(int id, String name, UUID creator){

        Preconditions.checkArgument(ChannelManager.getChannel(id)==null, "duplicate channel id: "+id);
        Preconditions.checkArgument(ChannelManager.getChannel(name)==null, "duplicate channel name: "+name);

        this.id=id;
        this.name=name;
        this.creator=creator;
        this.members=new HashSet<>();
    }

    public long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=Preconditions.checkNotNull(name, "name");
    }

    public UUID getCreator(){
        return creator;
    }

    public Permission getJoinPermission(){
        return isRequiresJoinPermission() ? new Permission("simplechannels.join."+getName()) : null;
    }

    public boolean isRequiresJoinPermission(){
        return requiresPermission;
    }

    public void setRequiresJoinPermission(boolean requiresPermission){
        this.requiresPermission=requiresPermission;
    }

    public boolean isRequiresInvite(){
        return requiresInvite;
    }

    public void setRequiresInvite(boolean requiresInvite){
        this.requiresInvite=requiresInvite;
    }

    public ImmutableSet<UUID> getMembers(){
        return ImmutableSet.copyOf(members);
    }

    public Channel add(Player player){
        members.add(Preconditions.checkNotNull(player, "player").getUniqueId());
        return this;
    }

    public Channel remove(Player player){
        members.remove(Preconditions.checkNotNull(player, "player").getUniqueId());
        return this;
    }

    public boolean sendMessage(String message, Player sender){

        Preconditions.checkNotNull(message, "message");
        Preconditions.checkNotNull(sender, "sender");

        ChannelChatEvent event=new ChannelChatEvent(this, sender, getMembers(), message);
        Bukkit.getPluginManager().callEvent(event);

        if(event.isCancelled()){
            return false;
        }

        message=ChatColor.stripColor(message);

        for(UUID uuid : event.getRecipients()){
            Player player=Bukkit.getPlayer(uuid);
            if(player==null){
                continue;
            }

            player.sendMessage(MessageFormat
                    .format("§7{1} >> §e{2}: §f{3}", getName(), sender.getName(), message));
        }
        return true;
    }

    public boolean broadcast(String message){
        ChannelChatEvent event=new ChannelChatEvent(this, null, getMembers(), message);
        Bukkit.getPluginManager().callEvent(event);

        if(event.isCancelled()){
            return false;
        }

        for(UUID uuid : event.getRecipients()){
            Player player=Bukkit.getPlayer(uuid);
            if(player==null){
                continue;
            }

            player.sendMessage(MessageFormat
                    .format("§7{1} >> §f{2}", getName(), message));
        }
        return true;
    }

    @Override
    public boolean equals(Object o){
        if(this==o){
            return true;
        }
        if(!(o instanceof Channel)){
            return false;
        }

        Channel channel=(Channel) o;

        return id==channel.id&&
                requiresInvite==channel.requiresInvite&&
                requiresPermission==channel.requiresPermission&&
                name.equals(channel.name)&&creator.equals(channel.creator)&&
                members.equals(channel.members);

    }

    @Override
    public int hashCode(){
        int result=name!=null ? name.hashCode() : 0;
        result=31*result+(creator!=null ? creator.hashCode() : 0);
        result=31*result+(requiresInvite ? 1 : 0);
        result=31*result+(members!=null ? members.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return String.format("{name=%s,requiresJoinPermission=%s,joinPermission=%s,"+
                        "requiresInvite=%s,members=%s}",
                getName(), isRequiresJoinPermission(), getJoinPermission(),
                isRequiresInvite(), Util.getUuidsToNames(new ArrayList<>(getMembers())));
    }

}

