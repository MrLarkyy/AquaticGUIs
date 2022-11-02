package xyz.larkyy.aquaticguis.commands.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.larkyy.aquaticguis.AquaticGUIs;
import xyz.larkyy.aquaticguis.commands.ICommand;

public class OpenMenuCommand implements ICommand {
    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player p) {
            if (!p.hasPermission("aquaticguis.open")) {
                return;
            }
            if (args.length == 1) {
                return;
            }
            var menu = AquaticGUIs.getInstance().getMenuRegistry().getMenu(args[1]);
            if (menu == null) {
                return;
            }
            menu.open(p);
        }
    }
}
