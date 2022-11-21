package xyz.larkyy.aquaticguis.commands.impl;

import org.bukkit.command.CommandSender;
import xyz.larkyy.aquaticguis.AquaticGUIs;
import xyz.larkyy.aquaticguis.commands.ICommand;

public class ListCommand implements ICommand {
    @Override
    public void run(CommandSender sender, String[] args) {
        if (!sender.hasPermission("aquaticguis.list")) {
            return;
        }
        sender.sendMessage("Menus:");
        AquaticGUIs.getInstance().getMenuRegistry().getMenus().values().forEach(menu -> {
            sender.sendMessage("- "+menu.getId());
        });
    }
}
