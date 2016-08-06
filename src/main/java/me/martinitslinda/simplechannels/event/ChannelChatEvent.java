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

package me.martinitslinda.simplechannels.event;

import me.martinitslinda.simplechannels.channel.Channel;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Set;
import java.util.UUID;

public class ChannelChatEvent extends Event implements Cancellable{

    private static final HandlerList handlers=new HandlerList();
    private boolean isCancelled;
    private Channel channel;
    private Player sender;
    private Set<UUID> recipients;
    private String message;

    public ChannelChatEvent(Channel channel, Player sender, Set<UUID> recipients, String message){
        this.channel=channel;
        this.sender=sender;
        this.recipients=recipients;
        this.message=message;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    public Channel getChannel(){
        return channel;
    }

    public Player getSender(){
        return sender;
    }

    public Set<UUID> getRecipients(){
        return recipients;
    }

    public String getMessage(){
        return message;
    }

    @Override
    public HandlerList getHandlers(){
        return handlers;
    }

    @Override
    public boolean isCancelled(){
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled){
        this.isCancelled=isCancelled;
    }
}
