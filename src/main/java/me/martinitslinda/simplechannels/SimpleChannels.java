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
import me.martinitslinda.simplechannels.managers.*;
import me.martinitslinda.simplechannels.sql.MySQL;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class SimpleChannels extends JavaPlugin{

    //Variable declaration
    public static final String PREFIX="§7[§dSimpleChannels§7]";
    private static SimpleChannels instance;

    private ChannelManager channelManager;
    private RequestManager requestManager;
    private CommandManager commandManager;

    private HikariDataSource source;

    private boolean failure;

    public static SimpleChannels get(){
        return instance;
    }

    @Override
    public void onLoad(){

        instance=this;

        new Thread(){
            @Override
            public void run(){

                //Declare variables
                Connection connection=null;
                PreparedStatement statement=null;

                try{

                    //Try to connect to MySQL
                    connection=MySQL.getConnection();

                    //Retrieve DB meta data
                    DatabaseMetaData tableMeta=connection.getMetaData();

                    //To avoid typos
                    String table="sch_channels";

                    //Does the table exist?
                    if(tableMeta.getTables(null, null, table, null).next()){
                        //Table found, tell them everything's alright.
                        instance.getLogger().log(Level.INFO, "Table `"+table+"` found!");
                    }else{
                        //Oops, table not found. Lets create it
                        instance.getLogger().log(Level.INFO, "Table `"+table+"` not found, creating it...");

                        //Create statement to send to MySQL
                        String query="CREATE TABLE `"+table+"` "+
                                "(`id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY, "+
                                "`channel_id` VARCHAR(12) NOT NULL, "+
                                "`channel_name` "+
                                "`creator` VARCHAR(36) NOT NULL, "+
                                "`totalMessage` INT(11) NOT NULL DEFAULT '0', "+
                                "`dateCreated` BIGINT(32) NOT NULL DEFAULT '0', "+
                                "`isActive` BOOLEAN NOT NULL DEFAULT '1');";

                        //Send query to MySQL
                        statement=connection.prepareStatement(query);
                        statement.executeUpdate();

                        //Was it created or did something f*** up?
                        boolean success=tableMeta.getTables(null, null, table, null).next();

                        if(success){
                            //Yep, everything's fine.
                            instance.getLogger().log(Level.INFO, "Table `"+table+"` has been created!");
                        }else{
                            //Uh oh, something went wrong
                            instance.getLogger().log(Level.INFO, "An error occurred whilst attempting to create table `"+table+"`.");
                            instance.getLogger().log(Level.INFO, "Please execute the below query in your MySQL console.");
                            instance.getLogger().log(Level.INFO, query);

                            failure=true;
                        }

                    }

                    //Change table name
                    table="sch_channels_members";

                    //Does the table exist?
                    if(tableMeta.getTables(null, null, table, null).next()){
                        //Table found, lets continue on with our lives
                        instance.getLogger().log(Level.INFO, "Table `"+table+"` found!");
                    }else{
                        //Nope, lets create it.
                        instance.getLogger().log(Level.INFO, "Table `"+table+"` not found, creating it...");

                        //Create our statement we will be sending to MySQL
                        String query="CREATE TABLE `"+table+"` "+
                                "(`id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY, "+
                                "`channel_id` VARCHAR(12) NOT NULL, "+
                                "`uuid` VARCHAR(36) NOT NULL, "+
                                "`role` VARCHAR(30) NOT NULL DEFAULT '"+Role.USER+"')";

                        statement=connection.prepareStatement(query);
                        statement.executeUpdate();

                        boolean success=tableMeta.getTables(null, null, table, null).next();

                        if(success){
                            instance.getLogger().log(Level.INFO, "Table `"+table+"` has been created!");
                        }else{
                            instance.getLogger().log(Level.INFO, "An error occurred whilst attempting to create table `"+table+"`");
                            instance.getLogger().log(Level.INFO, "Please execute the below query in your MySQL console.");
                            instance.getLogger().log(Level.INFO, query);

                            failure=true;
                        }

                    }


                }
                catch(SQLException e){
                    e.printStackTrace();
                }

            }
        }.run();

    }

    @Override
    public void onEnable(){



    }


    public ChannelManager getChannelManager(){
        if(channelManager==null){
            channelManager=new SimpleChannelManager();
        }
        return channelManager;
    }

    public RequestManager getRequestManager(){
        if(requestManager==null){
            requestManager=new SimpleRequestManager();
        }
        return requestManager;
    }

    public CommandManager getCommandManager(){
        if(commandManager==null){
            commandManager=new CommandManager();
        }
        return commandManager;
    }
}
