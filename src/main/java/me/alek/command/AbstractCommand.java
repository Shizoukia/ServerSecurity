package me.alek.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommand implements CommandExecutor {

    ArrayList<SubCommand> subCommands = new ArrayList<SubCommand>();

    public void init() {
        registerSubCommands().forEach(subCommand -> {
            subCommands.add(subCommand);
        });
    }

    public AbstractCommand() {
        init();
    }

    public void sendHelpMessage(AbstractCommand abstractCommand, CommandSender sender) {
        sender.sendMessage("§8[§6AntiMalware§8] §7Sender kommandoer du kan bruge...");
        subCommands.forEach(subCommand -> {
            sender.sendMessage("§a" + subCommand.getUsage() + ": §7" + subCommand.getDescription());
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandResponse.Response response = CommandResponse.check(this, sender, args);
        if (response == CommandResponse.Response.PERFORM_SINGLE) {
            performSingle(sender, args);
            return true;
        }
        if (response == CommandResponse.Response.NOT_EXECUTABLE ) {
            sender.sendMessage("[AntiMalware] Denne kommando kan kun bruges af spillere.");
            return true;
        }
        if (response == CommandResponse.Response.NO_PERMISSION) {
            sender.sendMessage("§8[§6AntiMalware§8] §cDu har ikke adgang til denne kommando.");
            return true;
        }

        SubCommand sub = subCommands.stream().filter(subCommand -> {
            if (subCommand.getName().equalsIgnoreCase(args[0])) return true;
            return Arrays.stream(subCommand.getAliases()).anyMatch(alias -> alias.equalsIgnoreCase(args[0]));
        }).findAny().orElse(null);

        if (sub == null) {
            sendHelpMessage(this, sender);
            return true;
        }

        sub.perform(sender, args);
        return true;

    }

    public abstract void performSingle(CommandSender sender, String[] args);

    public abstract boolean executableAsConsole();

    public abstract String getPermission();

    public abstract List<SubCommand> registerSubCommands();

}
