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
import me.martinitslinda.simplechannels.command.CommandRequest;
import me.martinitslinda.simplechannels.exception.CommandNotFoundException;
import me.martinitslinda.simplechannels.exception.CommandPermissionsException;
import me.martinitslinda.simplechannels.exception.CommandSenderException;
import me.martinitslinda.simplechannels.exception.CommandUsageException;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class CommandManager {

    private Set<ChannelCommand> commands = new HashSet<>();
    private SimpleChannels plugin = SimpleChannels.get();

    public void handle(CommandSender sender, String[] args) throws CommandPermissionsException, CommandUsageException, CommandNotFoundException, CommandSenderException {

        if (args.length < 1) throw new CommandUsageException(null, "sch <command>");

        String commandName = args[0];
        ChannelCommand command = getCommand(commandName);

        if (command == null) throw new CommandNotFoundException(commandName);

        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, newArgs.length);

        if (!(sender.hasPermission(command.getPermission())))
            throw new CommandPermissionsException(command, command.getPermission());

        CommandRequest request = new CommandRequest(sender, newArgs);

        command.execute(request);
    }

    public List<ChannelCommand> getCommands() {
        return new ArrayList<>(commands);
    }

    public ChannelCommand getCommand(String command) {
        for (ChannelCommand cmd : getCommands()) {
            if (cmd.getName().equalsIgnoreCase(command)) {
                return cmd;
            }
        }
        return null;
    }

    public boolean isRegistered(ChannelCommand command) {
        return getCommand(Preconditions.checkNotNull(command, "command").getName()) != null;
    }

    public void registerCommand(ChannelCommand command) {
        if (!(commands.contains(Preconditions.checkNotNull(command)))) {
            commands.add(command);
            plugin.getLogger().log(Level.INFO, "Command " + command.getName() + " has been registered successfully.");
        }
    }

    public void unregisterCommand(ChannelCommand command) {
        if (commands.contains(Preconditions.checkNotNull(command))) {
            commands.remove(command);
            plugin.getLogger().log(Level.INFO, "Command " + command.getName() + " has been unregistered successfully.");
        }
    }

}
