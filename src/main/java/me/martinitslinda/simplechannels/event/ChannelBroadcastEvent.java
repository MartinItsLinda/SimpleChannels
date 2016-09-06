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
import org.bukkit.event.Cancellable;

public class ChannelBroadcastEvent extends ChannelEvent implements Cancellable{

    private String message;
    private boolean isCancelled;

    public ChannelBroadcastEvent(Channel channel, String message){
        super(channel);
    }

    public String getMessage(){
        return message;
    }

    @Override
    public boolean isCancelled(){
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled){
        isCancelled=cancelled;
    }
}
