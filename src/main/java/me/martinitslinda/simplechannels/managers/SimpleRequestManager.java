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
import me.martinitslinda.simplechannels.channel.Channel;
import me.martinitslinda.simplechannels.request.Request;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SimpleRequestManager implements RequestManager {

    private List<Request> requests = new ArrayList<>();

    @Override
    public List<Request> getRequests() {
        return null;
    }

    @Override
    public boolean hasRequestTo(UUID uuid, Channel channel) {
        return false;
    }

    @Override
    public Request getRequestFrom(Channel sender, UUID recipient) {
        return null;
    }

    @Override
    public Request sendRequestTo(Player target, Channel sender) {
        Preconditions.checkNotNull(target, "target");
        Preconditions.checkNotNull(sender, "sender");

        if(hasRequestTo(target.getUniqueId(), sender))
            throw new IllegalArgumentException(target.getName()+" already has a request pending from " +
                    ""+sender.getName()+".");

        String message = ChatColor.translateAlternateColorCodes('&',
                "You have been invited to join {channel}.");
        message = message.replace("{channel}", sender.getName());
        target.sendMessage(message);

        Request request = new Request(sender, target.getUniqueId());
        requests.add(request);

        return request;
    }
}
