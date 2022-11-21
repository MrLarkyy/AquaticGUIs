package xyz.larkyy.aquaticguis.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.larkyy.aquaticguis.commands.impl.ListCommand;
import xyz.larkyy.aquaticguis.commands.impl.OpenMenuCommand;
import xyz.larkyy.aquaticguis.commands.impl.ReloadCommand;

import java.util.HashMap;
import java.util.Map;

public class Commands implements CommandExecutor {

    private final Map<String, ICommand> availableCommands = new HashMap<>(){
        {
            put("open",new OpenMenuCommand());
            put("reload",new ReloadCommand());
            put("list",new ListCommand());
        }
    };

    @Override
    public boolean onCommand(CommandSender sender,  Command command,  String label, String[] args) {

        if (args.length < 1) {
            return false;
        }


        ICommand cmd = availableCommands.get(args[0]);
        if (cmd == null) {
            return false;
        }
        cmd.run(sender,args);
        return true;
    }
}
