package me.martinitslinda.simplechannels.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRequest {

    private CommandSender sender;
    private String[] args;

    public CommandRequest(CommandSender sender, String[] args) {
        this.sender = sender;
        this.args = args;
    }

    public void reply(String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public CommandSender getSender() {
        return sender;
    }

    public boolean isPlayer() {
        return (getSender() instanceof Player);
    }

    public String[] getArgs() {
        return args;
    }
}
