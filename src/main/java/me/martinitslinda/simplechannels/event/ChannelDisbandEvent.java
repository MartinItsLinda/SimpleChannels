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
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChannelDisbandEvent extends Event{

    private static final HandlerList handlers=new HandlerList();

    private Channel channel;
    private Player disbander;

    public ChannelDisbandEvent(Channel channel, Player disbander){
        this.channel=channel;
        this.disbander=disbander;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    public Channel getChannel(){
        return channel;
    }

    public Player getDisbander(){
        return disbander;
    }

    @Override
    public HandlerList getHandlers(){
        return handlers;
    }

}
