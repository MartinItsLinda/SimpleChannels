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

package me.martinitslinda.simplechannels.sql;

import com.zaxxer.hikari.HikariDataSource;
import me.martinitslinda.simplechannels.SimpleChannels;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQL{

    private static HikariDataSource source;
    private static SimpleChannels plugin=SimpleChannels.get();

    public static Connection getConnection() throws SQLException{

        //If source isn't null and source isn't closed, return a connection from the pool
        if(source!=null&&!source.isClosed()) return source.getConnection();

        //otherwise create a new pool
        source=new HikariDataSource();
        source.setJdbcUrl("jdbc:mysql://"+plugin.getConfig().getString("mysql.host")+
                ":"+plugin.getConfig().getInt("mysql.port")+"/"+plugin.getConfig().getString("mysql.database"));
        source.setUsername(plugin.getConfig().getString("mysql.username"));
        source.setPassword(plugin.getConfig().getString("mysql.password"));
        source.setMaximumPoolSize(15);

        //return a connection from the pool
        return source.getConnection();
    }

    public static void close(){
        //if source isn't null and isn't closed
        if(source!=null&&!source.isClosed()){
            //close pool
            source.close();
        }
    }

}
