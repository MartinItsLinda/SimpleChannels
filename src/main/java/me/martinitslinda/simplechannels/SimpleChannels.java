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

package me.martinitslinda.simplechannels;

import com.zaxxer.hikari.HikariDataSource;
import me.martinitslinda.simplechannels.channel.Role;
import me.martinitslinda.simplechannels.command.HelpCommand;
import me.martinitslinda.simplechannels.command.InviteCommand;
import me.martinitslinda.simplechannels.command.KickCommand;
import me.martinitslinda.simplechannels.exception.CommandNotFoundException;
import me.martinitslinda.simplechannels.exception.CommandPermissionsException;
import me.martinitslinda.simplechannels.exception.CommandSenderException;
import me.martinitslinda.simplechannels.exception.CommandUsageException;
import me.martinitslinda.simplechannels.listener.PlayerChatListener;
import me.martinitslinda.simplechannels.listener.PlayerJoinListener;
import me.martinitslinda.simplechannels.listener.PlayerQuitListener;
import me.martinitslinda.simplechannels.managers.*;
import me.martinitslinda.simplechannels.sql.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class SimpleChannels extends JavaPlugin {

    //Variable declaration
    public static final String PREFIX = "§7[§dSimpleChannels§7]";
    private static SimpleChannels instance;

    private ChannelManager channelManager;
    private RequestManager requestManager;
    private CommandManager commandManager;

    private HikariDataSource source;

    private boolean failure;

    public static SimpleChannels get() {
        return instance;
    }

    @Override
    public void onLoad() {

        instance = this;

        saveDefaultConfig();

        //Declare variables
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = MySQL.getConnection();

            DatabaseMetaData tableMeta = connection.getMetaData();

            String table = "sch_channels";

            if (tableMeta.getTables(null, null, table, null).next()) {
                instance.getLogger().log(Level.INFO, "Table `" + table + "` found!");
            } else {
                instance.getLogger().log(Level.INFO, "Table `" + table + "` not found, creating it...");

                String query = "CREATE TABLE `" + table + "` " +
                        "(`id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                        "`channel_id` VARCHAR(32) NOT NULL, " +
                        "`channelName` VARCHAR(16) NOT NULL, " +
                        "`creator` VARCHAR(36) NOT NULL);";

                statement = connection.prepareStatement(query);
                statement.executeUpdate();

                boolean success = tableMeta.getTables(null, null, table, null).next();

                if (success) {
                    instance.getLogger().log(Level.INFO, "Table `" + table + "` has been created!");
                } else {
                    instance.getLogger().log(Level.INFO, "An error occurred whilst attempting to create table `" + table + "`.");
                    instance.getLogger().log(Level.INFO, "Please execute the below query in your MySQL console.");
                    instance.getLogger().log(Level.INFO, query);

                    failure = true;
                }

            }

            table = "sch_channel_stats";

            if (tableMeta.getTables(null, null, table, null).next()) {
                instance.getLogger().log(Level.INFO, "Table `" + table + "` found!");
            } else {
                instance.getLogger().log(Level.INFO, "Table `" + table + "` not found, creating it...");

                String query = "CREATE TABLE `" + table + "` " +
                        "(`id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                        "`channel_id` VARCHAR(12) NOT NULL, " +
                        "`channel_name` " +
                        "`creator` VARCHAR(36) NOT NULL);";

                //Send query to MySQL
                statement = connection.prepareStatement(query);
                statement.executeUpdate();

                boolean success = tableMeta.getTables(null, null, table, null).next();

                if (success) {
                    instance.getLogger().log(Level.INFO, "Table `" + table + "` has been created!");
                } else {
                    instance.getLogger().log(Level.INFO, "An error occurred whilst attempting to create table `" + table + "`.");
                    instance.getLogger().log(Level.INFO, "Please execute the below query in your MySQL console.");
                    instance.getLogger().log(Level.INFO, query);

                    failure = true;
                }

            }

            table = "sch_channels_members";

            if (tableMeta.getTables(null, null, table, null).next()) {
                instance.getLogger().log(Level.INFO, "Table `" + table + "` found!");
            } else {
                instance.getLogger().log(Level.INFO, "Table `" + table + "` not found, creating it...");

                String query = "CREATE TABLE `" + table + "` " +
                        "(`id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                        "`channel_id` VARCHAR(12) NOT NULL, " +
                        "`uuid` VARCHAR(36) NOT NULL, " +
                        "`role` VARCHAR(30) NOT NULL DEFAULT '" + Role.USER + "')";

                statement = connection.prepareStatement(query);
                statement.executeUpdate();

                boolean success = tableMeta.getTables(null, null, table, null).next();

                if (success) {
                    instance.getLogger().log(Level.INFO, "Table `" + table + "` has been created!");
                } else {
                    instance.getLogger().log(Level.INFO, "An error occurred whilst attempting to create table `" + table + "`");
                    instance.getLogger().log(Level.INFO, "Please execute the below query in your MySQL console.");
                    instance.getLogger().log(Level.INFO, query);

                    failure = true;
                }

            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (statement != null) {
                try {
                    statement.close();
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

    @Override
    public void onEnable() {

        if (failure) {
            getLogger().log(Level.INFO, "An error occurred on startup, please check the logs for more details.");
            getPluginLoader().disablePlugin(this);
            return;
        }

        PluginManager manager = getServer().getPluginManager();

        manager.registerEvents(new PlayerJoinListener(), this);
        manager.registerEvents(new PlayerQuitListener(), this);
        manager.registerEvents(new PlayerChatListener(), this);
        manager.registerEvents(null, this);
        manager.registerEvents(null, this);

        new InviteCommand().register();
        new KickCommand().register();
        new HelpCommand().register();

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            getCommandManager().handle(sender, args);
        } catch (CommandPermissionsException e) {
            e.printStackTrace();
        } catch (CommandUsageException e) {
            e.printStackTrace();
        } catch (CommandNotFoundException e) {
            e.printStackTrace();
        } catch (CommandSenderException e) {
            e.printStackTrace();
        }
        return true;
    }

    public ChannelManager getChannelManager() {
        if (channelManager == null) {
            channelManager = new SimpleChannelManager();
        }
        return channelManager;
    }

    public RequestManager getRequestManager() {
        if (requestManager == null) {
            requestManager = new SimpleRequestManager();
        }
        return requestManager;
    }

    public CommandManager getCommandManager() {
        if (commandManager == null) {
            commandManager = new CommandManager();
        }
        return commandManager;
    }
}
