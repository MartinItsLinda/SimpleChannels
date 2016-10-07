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

import com.google.common.base.Preconditions;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public abstract class ChannelCommand{

    private String name;
    private Permission permission;
    private String usage;
    private String description;
    private int minArgs;
    private boolean isConsole;

    public ChannelCommand(String name, String permission){
        this(name, permission, null, null, 0, false);
    }

    public ChannelCommand(String name, String permission, String usage){
        this(name, permission, usage, null, 0, false);
    }

    public ChannelCommand(String name, String permission, String usage, String description){
        this(name, permission, usage, description, 0, false);
    }

    public ChannelCommand(String name, String permission, String usage, String description, int minArgs){
        this(name, permission, usage, description, minArgs, false);
    }

    public ChannelCommand(String name, String permission, String usage, String description, int minArgs, boolean isConsole){

        Preconditions.checkNotNull(name, "name");
        Preconditions.checkNotNull(permission, "permission");

        this.name=name;
        this.permission=new Permission(permission);
        this.usage=usage;
        this.description=description;
        this.minArgs=minArgs;
        this.isConsole=isConsole;
    }

    public String getName(){
        return name;
    }

    public Permission getPermission(){
        return permission;
    }

    public String getUsage(){
        return usage;
    }

    public String getDescription(){
        return description;
    }

    public int getMinArgs(){
        return minArgs;
    }

    public boolean isConsole(){
        return isConsole;
    }

    public abstract CommandResult execute(CommandSender sender, String[] args);

}
