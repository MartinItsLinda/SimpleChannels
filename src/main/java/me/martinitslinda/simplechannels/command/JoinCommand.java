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

package me.martinitslinda.simplechannels.command;

import me.martinitslinda.simplechannels.channel.Channel;
import me.martinitslinda.simplechannels.channel.ChannelManager;
import me.martinitslinda.simplechannels.reqest.Request;
import me.martinitslinda.simplechannels.reqest.RequestManager;
import org.bukkit.entity.Player;

public class JoinCommand extends Command{

    public JoinCommand(){
        super("join", "simplechannels.command.join", "<channel_name>", "Join a channel.");
    }

    @Override
    public void execute(Player sender, String[] args){

        if(args.length<1){
            showHelp(sender);
            return;
        }

        Channel channel=ChannelManager.getChannel(args[0]);

        if(channel==null){
            error(sender, "Channel not found ("+args[0]+").");
            return;
        }

        Request.Result result=RequestManager.hasPendingRequestFrom(channel, sender);
        if(channel.isRequiresInvite()){
            if(result==Request.Result.TIMED_OUT){
                error(sender, "Your pending request from "+channel.getName()+" has timed out.");
            }else if(result==Request.Result.NOT_FOUND){
                error(sender, "You don't have any pending requests from "+channel.getName());
            }else{
                channel.add(sender);
                channel.broadcast("§a"+sender.getName()+" has joined.");
            }
            return;
        }

        if((channel.getJoinPermission()!=null)&&!(sender.hasPermission(channel.getJoinPermission()))){
            error(sender, "You don't have permission to join this channel.");
            return;
        }

        channel.add(sender);
        channel.broadcast("§a"+sender.getName()+" has joined.");

    }
}
