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

import me.martinitslinda.simplechannels.reqest.Request;
import me.martinitslinda.simplechannels.reqest.RequestManager;
import org.bukkit.entity.Player;

public class DeclineCommand extends Command{

    public DeclineCommand(){
        super("decline", "simplechannels.command.decline", null, "Decline a pending invite request.");
    }

    @Override
    public void execute(Player sender, String[] args){

        Request request=RequestManager.getPendingRequest(sender);
        if(request==null){
            error(sender, "You don't have any pending requests.");
            return;
        }

        error(sender, "Your request to join "+request.getSender().getName()+" has been declined.");
        RequestManager.terminate(request);

    }
}
