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

package me.martinitslinda.simplechannels.reqest;

import com.google.common.base.Preconditions;
import me.martinitslinda.simplechannels.channel.Channel;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RequestManager{

    private static List<Request> requests=new ArrayList<>();

    public static List<Request> getRequests(){
        return requests;
    }

    public static Request getPendingRequest(Player target){
        Preconditions.checkNotNull(target, "target");
        for(Iterator<Request> it=requests.iterator(); it.hasNext(); ){
            Request request=it.next();
            if(!(request.getTarget().equals(target.getName()))){
                continue;
            }
            if(request.getTime()-System.currentTimeMillis()<=0){
                it.remove();
                return null;
            }
            return request;
        }
        return null;
    }

    public static boolean hasPendingRequest(Player target){
        Preconditions.checkNotNull(target, "target");
        for(Iterator<Request> it=requests.iterator(); it.hasNext(); ){
            Request request=it.next();
            if(!(request.getTarget().equals(target.getName()))){
                continue;
            }

            if(request.getTime()-System.currentTimeMillis()<=0){
                it.remove();
                return false;
            }

            return true;
        }
        return false;
    }

    public static Request.Result hasPendingRequestFrom(Channel sender, Player target){
        Preconditions.checkNotNull(sender, "channel");
        Preconditions.checkNotNull(target, "target");

        for(Iterator<Request> it=requests.iterator(); it.hasNext(); ){
            Request request=it.next();

            if(!(request.getSender().getId()==sender.getId())||
                    !(request.getTarget().equals(target.getName()))){
                continue;
            }

            if(request.getTime()-System.currentTimeMillis()<=0){
                it.remove();
                return Request.Result.NOT_FOUND;
            }

            return Request.Result.FOUND;
        }
        return Request.Result.NOT_FOUND;
    }

    public static Request.Result sendRequestTo(Channel sender, Player target){
        if(hasPendingRequest(Preconditions.checkNotNull(target, "target"))){
            return Request.Result.PLAYER_ALREADY_HAS_PENDING_REQUEST;
        }
        requests.add(new Request(Preconditions.checkNotNull(sender, "channel"), target.getName()));
        return Request.Result.SUCCESS;
    }

    public static boolean terminate(Request request){
        Preconditions.checkNotNull(request, "request");
        return requests.remove(request);
    }

}
