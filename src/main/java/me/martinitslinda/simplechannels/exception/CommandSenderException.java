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

package me.martinitslinda.simplechannels.exception;

import me.martinitslinda.simplechannels.command.ChannelCommand;

public class CommandSenderException extends Exception {

    private ChannelCommand command;
    private String message;

    public CommandSenderException(ChannelCommand command, String message) {
        this.command = command;
        this.message = message;
    }

    public ChannelCommand getCommand() {
        return command;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
