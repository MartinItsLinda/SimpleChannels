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

import me.martinitslinda.simplechannels.channel.Channel;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RequestManager{

    private static List<Request> requests=new ArrayList<>();

    public static List<Request> getRequests(){
        return requests;
    }

    public static Request getPendingRequest(Player target){
        if(hasPendingRequest(target)){
            for(Request request : getRequests()){
                if(!(request.getTarget().equals(target.getName()))){
                    continue;
                }
                return request;
            }
        }
        return null;
    }

    public static boolean hasPendingRequest(Player target){
        for(Request request : getRequests()){
            if(!(request.getTarget().equals(target.getName()))){
                continue;
            }

            if(request.getTime()-System.currentTimeMillis()<=0){
                requests.remove(request);
                return false;
            }

            return true;
        }
        return false;
    }

    public static Request.Result hasPendingRequestFrom(Channel sender, Player target){

        for(Request request : getRequests()){
            if(!(request.getSender().getId()==sender.getId())||
                    !(request.getTarget().equals(target.getName()))){
                continue;
            }

            if(request.getTime()-System.currentTimeMillis()<=0){
                requests.remove(request);
                return Request.Result.TIMED_OUT;
            }

            return Request.Result.FOUND;
        }

        return Request.Result.NOT_FOUND;
    }

    public static Request sendRequestTo(Channel sender, Player target){
        if(hasPendingRequest(target)){
            return getPendingRequest(target);
        }
        Request request=new Request(sender, target.getName());
        requests.add(request);
        return request;
    }

}
