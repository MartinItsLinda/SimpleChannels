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

import me.martinitslinda.simplechannels.SimpleChannels;
import me.martinitslinda.simplechannels.channel.Channel;
import me.martinitslinda.simplechannels.channel.ChannelManager;
import me.martinitslinda.simplechannels.reqest.Request;
import me.martinitslinda.simplechannels.reqest.RequestManager;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class InviteCommand extends Command{

    public InviteCommand(){
        super("invite", "simplechannels.command.invite", "<player> <channel>", "Invite a player to join your Channel.");
    }

    @Override
    public void execute(Player sender, String[] args){
        if(args.length<2){
            showHelp(sender);
            return;
        }

        List<Channel> channels=ChannelManager.getChannels(sender);
        if(channels.size()==0){
            error(sender, "You're not currently a part of any channels.");
            error(sender, "Use /channel create <name> to create a channel.");
            return;
        }

        Player target=Bukkit.getPlayer(args[0]);
        if(target==null){
            error(sender, "The specified player isn't currently online.");
            return;
        }

        Channel channel=null;
        for(Channel ch : channels){
            if(ch.getName().equalsIgnoreCase(args[1])){
                channel=ch;
            }
        }

        if(channel==null){
            error(sender, "You're not a member of any channel named "+args[1]+".");
            return;
        }

        if(!(channel.getCreator().equals(sender.getUniqueId()))){
            error(sender, "Only channel creators can invite players.");
            return;
        }

        Request.Result result=RequestManager.sendRequestTo(channel, target);
        switch(result){
            case PLAYER_ALREADY_HAS_PENDING_REQUEST:
                error(sender, target.getName()+" already has a pending request.");
                break;
            case SUCCESS:
                target.sendMessage(SimpleChannels.PREFIX+"§a"+sender.getName()+" has invited you to join "+channel.getName());
                new FancyMessage(SimpleChannels.PREFIX+"§2[§aAccept§2]")
                        .command("sch accept")
                        .send(target);
                new FancyMessage(SimpleChannels.PREFIX+"§4[§cDecline§4]")
                        .command("sch decline")
                        .send(target);
                success(sender, target.getName()+" has been sent a request to join "+channel.getName()+".");
        }

    }
}
