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

package me.martinitslinda.simplechannels.util;

import me.martinitslinda.simplechannels.SimpleChannels;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class Config{

    public static void load(SimpleChannels plugin){

        FileConfiguration config=plugin.getConfig();
        Map<String, Object> cfg=new HashMap<>();

        cfg.put("mysql.username", "username");
        cfg.put("mysql.password", "password");
        cfg.put("mysql.host", "localhost");
        cfg.put("mysql.database", "Minecraft");
        cfg.put("mysql.port", 3306);

        for(Map.Entry<String, Object> e : cfg.entrySet()){
            if(!(config.contains(e.getKey()))){
                config.set(e.getKey(), e.getValue());
            }
        }

    }

}
