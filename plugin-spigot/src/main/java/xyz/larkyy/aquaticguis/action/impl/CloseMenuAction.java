package xyz.larkyy.aquaticguis.action.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.larkyy.aquaticguis.AquaticGUIs;
import xyz.larkyy.aquaticguis.action.Action;

public class CloseMenuAction extends Action {
    @Override
    public void run(Player player, String value) {
        AquaticGUIs.getInstance().getNmsHandler().closeContainer(player,-1);
        player.closeInventory();
    }
}
