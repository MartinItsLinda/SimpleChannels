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

package me.martinitslinda.simplechannels.managers;

import me.martinitslinda.simplechannels.SimpleChannels;
import me.martinitslinda.simplechannels.channel.Channel;
import me.martinitslinda.simplechannels.channel.Role;
import me.martinitslinda.simplechannels.channel.SimpleChannel;
import me.martinitslinda.simplechannels.sql.MySQL;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.*;
import java.util.logging.Level;

public class SimpleChannelManager implements ChannelManager {

    private List<Channel> channels = new ArrayList<>();
    private Map<String, Channel> playerChannel = new HashMap<>();
    private SimpleChannels plugin = SimpleChannels.get();

    @Override
    public synchronized void downloadChannels() {

        channels.clear();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {

                Connection connection = null;
                PreparedStatement stmt = null;
                ResultSet set = null;

                PreparedStatement memberStmt = null;
                ResultSet memberSet = null;

                try {

                    connection = MySQL.getConnection();
                    stmt = connection.prepareStatement("SELECT * FROM `sch_channels`;");
                    set = stmt.executeQuery();

                    int rows = connection.prepareStatement("SELECT COUNT(*) FROM `sch_channels`;").executeQuery().getInt("count");

                    plugin.getLogger().log(Level.INFO, "Downloading data of " + rows + " channels...");

                    long startTime = System.currentTimeMillis();

                    while (set.next()) {
                        String id = set.getString("channel_id");
                        String channelName = set.getString("channelName");
                        UUID creatorUUID = UUID.fromString(set.getString("creator"));

                        plugin.getLogger().log(Level.INFO, "Downloading membership data for channel id '" + id + "'("+channelName+")...");
                        Set<UUID> members = new HashSet<>();

                        memberStmt = connection.prepareStatement("SELECT * FROM `sch_channels_members` WHERE `channel_id`=?");
                        memberStmt.setString(1, id);
                        memberSet = memberStmt.executeQuery();

                        while (memberSet.next()) members.add(UUID.fromString(set.getString("uuid")));

                        plugin.getLogger().log(Level.INFO, "Downloaded " + members.size() + " users.");

                        SimpleChannel channel = new SimpleChannel(id, channelName, creatorUUID, members,
                                set.getString("permission"));

                        plugin.getLogger().log(Level.INFO, "Registering channel \""+channelName+"\"...");

                        channels.add(channel);

                        plugin.getLogger().log(Level.INFO, channelName + " has been registered.");
                    }

                    long endTime = System.currentTimeMillis();
                    long totalTime = (endTime - startTime);

                    plugin.getLogger().log(Level.INFO, "Download complete. Took " + totalTime + "ms.");

                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {

                    if (set != null) {
                        try {
                            set.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    if (memberSet != null) {
                        try {
                            memberSet.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    if (stmt != null) {
                        try {
                            stmt.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    if (memberStmt != null) {
                        try {
                            memberStmt.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        });

    }

    @Override
    public List<Channel> getChannels() {
        return new ArrayList<>(channels);
    }

    @Override
    public List<Channel> getOwnedChannels(Player player) {

        List<Channel> channels = new ArrayList<>();

        for(Channel channel : getChannels(player)) {
            if(channel.getCreatorUUID().equals(player.getUniqueId())) channels.add(channel);
        }

        return channels;
    }

    @Override
    public List<Channel> getChannels(Player player) {

        List<Channel> channels = new ArrayList<>();

        for (Channel channel : getChannels()) {
            if (channel.getMembers().contains(player.getUniqueId())) {
                channels.add(channel);
            }
        }

        return channels;
    }

    @Override
    public Channel getChannelByName(String name) {
        for (Channel channel : getChannels()) {
            if (channel.getName().equalsIgnoreCase(name)) {
                return channel;
            }
        }
        return null;
    }

    @Override
    public Channel getChannelById(String id) {
        for (Channel channel : getChannels()) {
            if (channel.getId().equals(id)) {
                return channel;
            }
        }
        return null;
    }

    @Override
    public Channel getActiveChannel(Player player) {
        return playerChannel.get(player.getName());
    }

    @Override
    public Channel setActiveChannel(Player player, Channel channel) {
        return playerChannel.put(player.getName(), channel);
    }

}
