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
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public abstract class Command{

    protected SimpleChannels plugin;
    private String name;
    private String permission;
    private String usage;
    private String description;

    public Command(String name, String permission, String usage, String description){
        this.name=name;
        this.permission=permission;
        this.usage=usage;
        this.description=description;
        this.plugin=SimpleChannels.get();
    }

    public String getName(){
        return name;
    }

    public String getPermission(){
        return permission;
    }

    public String getUsage(){
        return usage;
    }

    public String getDescription(){
        return description;
    }

    public void showHelp(Player player){
        StringBuilder builder=new StringBuilder();

        builder.append(SimpleChannels.PREFIX).append("&cCorrect Usage: &7/sch ")
                .append(getName())
                .append(" ")
                .append(getUsage());
        if(getDescription()!=null)
            builder.append(" &8-&7 ").append(getDescription());

        player.sendMessage(translateAlternateColorCodes('&', builder.toString()));
    }

    public void success(Player sender, String message){
        sender.sendMessage(SimpleChannels.PREFIX+"§a"+message);
    }

    public void error(Player sender, String message){
        sender.sendMessage(SimpleChannels.PREFIX+"§c"+message);
    }

    public abstract void execute(Player sender, String[] args);

}
