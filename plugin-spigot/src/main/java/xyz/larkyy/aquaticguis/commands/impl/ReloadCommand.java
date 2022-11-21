package xyz.larkyy.aquaticguis.commands.impl;

import org.bukkit.command.CommandSender;
import xyz.larkyy.aquaticguis.AquaticGUIs;
import xyz.larkyy.aquaticguis.commands.ICommand;

public class ReloadCommand implements ICommand {
    @Override
    public void run(CommandSender sender, String[] args) {
        if (!sender.hasPermission("aquaticguis.reload")) {
            return;
        }

        AquaticGUIs.getInstance().reload();
        sender.sendMessage("Plugin reloaded!");
    }
}
