package xyz.larkyy.aquaticguis.action.impl;

import org.bukkit.entity.Player;
import xyz.larkyy.aquaticguis.action.Action;

public class CloseMenuAction extends Action {
    @Override
    public void run(Player player, String value) {
        player.closeInventory();
    }
}
