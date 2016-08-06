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
import me.martinitslinda.simplechannels.command.CommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

public class SimpleChannels extends JavaPlugin{

    public static final String PREFIX="§7SimpleChannels >> §f";
    private static SimpleChannels instance;

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

    public Connection getConnection() throws SQLException{

        if(source!=null&&!source.isClosed()){
            return source.getConnection();
        }

        source=new HikariDataSource();
        source.setJdbcUrl("jdbc:mysql://"+getConfig().getString("mysql.host")+
                ":"+getConfig().getInt("mysql.port")+"/"+getConfig().getString("mysql.database"));
        source.setUsername(getConfig().getString("mysql.username"));
        source.setPassword(getConfig().getString("mysql.password"));
        source.setMaximumPoolSize(15);
        source.setLeakDetectionThreshold(5000);

        return source.getConnection();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        return command.getName().equalsIgnoreCase("simplechannels")&&CommandHandler.handle(sender, args);
    }
}
