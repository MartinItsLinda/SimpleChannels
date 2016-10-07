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

import com.google.common.base.Preconditions;
import me.martinitslinda.simplechannels.SimpleChannels;
import me.martinitslinda.simplechannels.command.ChannelCommand;
import me.martinitslinda.simplechannels.command.CommandResult;
import me.martinitslinda.simplechannels.exception.CommandNotFoundException;
import me.martinitslinda.simplechannels.exception.CommandPermissionsException;
import me.martinitslinda.simplechannels.exception.CommandSenderException;
import me.martinitslinda.simplechannels.exception.CommandUsageException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class CommandManager{

    //Variable declaration
    private List<ChannelCommand> commands=new ArrayList<>();
    private SimpleChannels plugin=SimpleChannels.get();

    public CommandResult handle(CommandSender sender, String[] args) throws CommandPermissionsException, CommandUsageException, CommandNotFoundException, CommandSenderException{

        if(args.length<1) throw new CommandUsageException(null, "sch <command>");

        //Retrieve the command (the first arg)
        String cmd=args[0];

        //Get the command from command list
        ChannelCommand command=getCommand(cmd);

        //If command is null (i.e not found) throw exception
        if(command==null) throw new CommandNotFoundException(null, "Cannot find command "+cmd);

        //Create a new array with the length of the args they put (-1 for command)
        String[] newArgs=new String[args.length-1];

        //Copy the array to the new array
        System.arraycopy(args, 1, newArgs, 0, newArgs.length);

        //if the new args length is less than command min args, throw exception
        if(newArgs.length<command.getMinArgs())
            throw new CommandUsageException(command, "sch "+command.getUsage());

        //if sender doesn't have the commands permission, throw exception
        if(!(sender.hasPermission(command.getPermission())))
            throw new CommandPermissionsException(command, command.getPermission());

        //If command isn't usable on console and sender isn't player, throw exception
        if(!(command.isConsole())&&!(sender instanceof Player))
            throw new CommandSenderException(command, "The console cannot perform this command.");

        //execute command
        return command.execute(sender, newArgs);
    }

    public List<ChannelCommand> getCommands(){
        return new ArrayList<>(commands);
    }

    public ChannelCommand getCommand(String command){
        for(ChannelCommand cmd : getCommands()){
            if(cmd.getName().equalsIgnoreCase(command)){
                return cmd;
            }
        }
        return null;
    }

    public void registerCommand(ChannelCommand command){
        if(!(commands.contains(Preconditions.checkNotNull(command)))){
            commands.add(command);
            plugin.getLogger().log(Level.INFO, "Command "+command.getName()+" has been registered successfully.");
        }
    }

    public void unregisterCommand(ChannelCommand command){
        if(commands.contains(Preconditions.checkNotNull(command))){
            commands.remove(command);
            plugin.getLogger().log(Level.INFO, "Command "+command.getName()+" has been unregistered successfully.");
        }
    }

}
