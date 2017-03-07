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
import me.martinitslinda.simplechannels.managers.ChannelManager;
import me.martinitslinda.simplechannels.managers.CommandManager;
import org.bukkit.permissions.Permission;

public abstract class ChannelCommand {

    private String name;
    private String usage;
    private Permission permission;
    private String description;
    private SimpleChannels plugin;
    private CommandManager commandManager;
    private ChannelManager channelManager;

    public ChannelCommand(String name, String usage, String description) {
        Preconditions.checkNotNull(name, "name");
        this.name = name;
        this.usage = usage;
        this.permission = new Permission("simplechannels.command."+name);
        this.description = description;
        this.plugin = SimpleChannels.get();
        this.commandManager = plugin.getCommandManager();
        this.channelManager = plugin.getChannelManager();
    }

    public String getName() {
        return name;
    }

    public String getUsage() {
        return usage;
    }

    public boolean hasUsageMessage() {
        return getUsage() != null && getUsage().length() > 0;
    }

    public Permission getPermission() {
        return permission;
    }

    public String getDescription() {
        return description;
    }

    public boolean hasDescription() {
        return getDescription() != null && getDescription().length() > 0;
    }

    public SimpleChannels getPlugin() {
        return plugin;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public ChannelManager getChannelManager() {
        return channelManager;
    }

    public void register() {
        getCommandManager().registerCommand(this);
    }

    public void unregister() {
        getCommandManager().unregisterCommand(this);
    }

    public abstract void execute(CommandRequest request);

}
