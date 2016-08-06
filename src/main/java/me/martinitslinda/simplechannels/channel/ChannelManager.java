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
import me.martinitslinda.simplechannels.SimpleChannels;
import me.martinitslinda.simplechannels.exception.ChannelException;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ChannelManager{

    private static Set<Channel> channels=new HashSet<>();
    private static Map<String, Channel> playerChannel=new HashMap<>();
    private static SimpleChannels plugin=SimpleChannels.get();

    public static void downloadChannelData() throws SQLException{

        Connection connection=plugin.getConnection();
        PreparedStatement statement=connection.prepareStatement("SELECT * FROM channel_information WHERE disbanded <> 1;");
        ResultSet set=statement.executeQuery();

    }

    public static Set<Channel> getChannels(){
        return ImmutableSet.copyOf(channels);
    }

    public static Channel getChannel(long id){
        for(Channel channel:getChannels()){
            if(channel.getId()==id){
                return channel;
            }
        }
        return null;
    }

    public static Channel getChannel(String name){
        for(Channel channel:getChannels()){
            if(channel.getName().equalsIgnoreCase(name)){
                return channel;
            }
        }
        return null;
    }

    public static List<Channel> getChannels(Player player){
        List<Channel> channels=new ArrayList<>();

        for(Channel channel:getChannels()){
            if(channel.getMembers().contains(player.getUniqueId())){
                channels.add(channel);
            }
        }

        return channels;
    }

    public static Channel getActiveChannel(Player player){
        return playerChannel.get(Preconditions.checkNotNull(player, "player").getName());
    }

    public static Channel setActiveChannel(Player player, Channel channel){
        Preconditions.checkNotNull(player, "player");
        Preconditions.checkNotNull(channel, "channel");

        Channel oldChannel=getActiveChannel(player);
        if(oldChannel!=null){
            oldChannel.remove(player);
        }

        channel.add(player);
        playerChannel.put(player.getName(), channel);

        return channel;
    }

    public static Channel create(Player creator, String name) throws ChannelException{

        if(getChannel(name) != null)throw new ChannelException("A channel already exists with this name.");

        return null;
    }

    public static boolean disband(Player disbander, Channel channel){

        Preconditions.checkNotNull(disbander, "disbander");
        Preconditions.checkNotNull(channel, "channel");

        return true;
    }

}
