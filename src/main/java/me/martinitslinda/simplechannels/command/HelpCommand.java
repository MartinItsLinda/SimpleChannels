package me.martinitslinda.simplechannels.command;

import me.martinitslinda.simplechannels.managers.CommandManager;

public class HelpCommand extends ChannelCommand {

    public HelpCommand() {
        super("help", "[page]", "Show all available commands.");
    }

    @Override
    public void execute(CommandRequest request) {

        String[] args = request.getArgs();
        CommandManager commandManager = getCommandManager();

        int startPage = 1; //Do pages later? idk

        if (args.length > 0) {
            try {
                startPage = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
                String reply = "&cPlease enter a valid integer.";
                request.reply(reply);
                return;
            }
        }

        for (ChannelCommand command : commandManager.getCommands()) {
            if (!request.getSender().hasPermission(command.getPermission())) continue;

            StringBuilder builder = new StringBuilder();

            builder.append("&7/").append(command.getName());
            if (command.hasUsageMessage()) builder.append(" ").append(command.getUsage());
            if (command.hasDescription()) builder.append(" &8- &7 ").append(command.getDescription());

            request.reply(builder.toString());
        }

    }
}
