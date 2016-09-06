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
import me.martinitslinda.simplechannels.command.CommandManager;
import me.martinitslinda.simplechannels.managers.ChannelManager;
import me.martinitslinda.simplechannels.managers.RequestManager;
import me.martinitslinda.simplechannels.managers.SimpleChannelManager;
import me.martinitslinda.simplechannels.managers.SimpleRequestManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleChannels extends JavaPlugin{

    public static final String PREFIX="§7[§dSimpleChannels§7]";
    private static SimpleChannels instance;

    private ChannelManager channelManager;
    private RequestManager requestManager;
    private CommandManager commandManager;

    private HikariDataSource source;

    public static SimpleChannels get(){
        return instance;
    }

    @Override
    public void onLoad(){



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
