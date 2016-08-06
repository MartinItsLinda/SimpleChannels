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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class CommandHandler{

    private static Set<Command> commands=new HashSet<>();

    public static Set<Command> getCommands(){
        return commands;
    }

    public static Command getCommand(String name){
        for(Command command : getCommands()){
            if(command.getName().equalsIgnoreCase(name)){
                return command;
            }
        }
        return null;
    }

    public static boolean handle(CommandSender sender, String[] args){

        if(!(sender instanceof Player)){
            sender.sendMessage(SimpleChannels.PREFIX+"&cThis command cannot be performed by the console.");
            return true;
        }

        Player player=(Player) sender;

        if(args.length<1){
            showHelp(player);
            return false;
        }

        Command command=getCommand(args[0]);

        if(command==null){
            player.sendMessage(translateAlternateColorCodes('&', SimpleChannels.PREFIX+"&cUnknown command &4(&c" + args[0] + "&4)&c."));
        }else if(!(player.hasPermission(command.getPermission()))){
            player.sendMessage(translateAlternateColorCodes('&', SimpleChannels.PREFIX+"&cYou don't have permission to perform that action."));
        }else{

            String[] newArgs=new String[args.length-1];
            System.arraycopy(args, 1, newArgs, 0, args.length-1);
            command.execute(player, newArgs);

        }
        return true;
    }

    public static void showHelp(Player player){

        player.sendMessage(translateAlternateColorCodes('&', "&8[&7======== &cSimpleChannels v"+SimpleChannels.get().getDescription().getVersion()+" Help =======&8]"));

        for(Command command : getCommands()){
            if(player.hasPermission(command.getPermission())){
                String message="&7/sch "+command.getName()+
                        " "+command.getUsage()+" - "+command.getDescription();

                player.sendMessage(translateAlternateColorCodes('&', message));
            }
        }

    }

}
