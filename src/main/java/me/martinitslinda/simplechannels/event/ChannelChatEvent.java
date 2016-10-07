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
import me.martinitslinda.simplechannels.channel.Role;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;

import java.util.Map;
import java.util.UUID;

public class ChannelChatEvent extends ChannelEvent implements Cancellable{

    private CommandSender sender;
    private String message;
    private Map<UUID, Role> recipients;
    private boolean cancelled;

    public ChannelChatEvent(Channel channel, CommandSender sender, String message, Map<UUID, Role> recipients){
        super(channel);
        this.sender=sender;
        this.message=message;
        this.recipients=recipients;
    }

    public CommandSender getSender(){
        return sender;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message=message;
    }

    public Map<UUID, Role> getRecipients(){
        return recipients;
    }

    @Override
    public boolean isCancelled(){
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled){
        this.cancelled=cancelled;
    }
}
