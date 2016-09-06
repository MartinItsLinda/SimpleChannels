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
import me.martinitslinda.simplechannels.SimpleChannels;
import me.martinitslinda.simplechannels.exception.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class CommandManager{

    private List<ChannelCommand> commands=new ArrayList<>();
    private SimpleChannels plugin=SimpleChannels.get();

    public boolean handle(CommandSender sender, String[] args) throws CommandException{

        if(args.length<1) throw new CommandException(null, "Correct usage: /sch <command>");

        String cmd=args[0];
        ChannelCommand command=getCommand(cmd);

        if(command==null) throw new CommandNotFoundException(null, "Cannot find command "+cmd);

        String[] newArgs=new String[args.length-1];
        System.arraycopy(args, 1, newArgs, 0, newArgs.length);

        if(newArgs.length<command.getMinArgs())
            throw new CommandUsageException(command, "Correct usage: /sch "+command.getUsage());
        if(!(sender.hasPermission(command.getPermission())))
            throw new CommandPermissionsException(command, "You don't have permission to perform this command.");
        if(!(command.isConsole())&&!(sender instanceof Player))
            throw new CommandSenderException(command, "The console cannot perform this command.");

        command.execute(sender, newArgs);

        return true;
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
