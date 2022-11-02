package xyz.larkyy.aquaticguis.action.impl;

import org.bukkit.entity.Player;
import xyz.larkyy.aquaticguis.AquaticGUIs;
import xyz.larkyy.aquaticguis.action.Action;

public class OpenMenuAction extends Action {
    @Override
    public void run(Player player, String value) {
        var menu = AquaticGUIs.getInstance().getMenuRegistry().getMenu(value);
        if (menu == null) {
            return;
        }
        menu.open(player);
    }
}
