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

package me.martinitslinda.simplechannels.request;

import me.martinitslinda.simplechannels.channel.Channel;

import java.util.UUID;

public class Request {

    private Channel sender;
    private UUID recipient;
    private long requestTime;

    public Request(Channel sender, UUID recipient) {
        this.sender = sender;
        this.recipient = recipient;
        this.requestTime = System.currentTimeMillis();
    }

    public Channel getSender() {
        return sender;
    }

    public UUID getRecipient() {
        return recipient;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public boolean hasExpired() {
        return getRequestTime() + 30000L - System.currentTimeMillis() <= 0;
    }

    public enum Result {

        SUCCESS, FAILURE, PLAYER_HAS_REQUEST, NOT_FOUND, TIMED_OUT, FOUND

    }

}
