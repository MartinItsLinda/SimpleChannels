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

public class Request{

    private Channel sender;
    private String target;
    private long time;

    public Request(Channel sender, String target){
        this.sender=sender;
        this.target=target;
        this.time=System.currentTimeMillis()+10000;
    }

    public Channel getSender(){
        return sender;
    }

    public String getTarget(){
        return target;
    }

    public long getTime(){
        return time;
    }

    public enum Result{

        TIMED_OUT, FOUND, NOT_FOUND

    }

}
