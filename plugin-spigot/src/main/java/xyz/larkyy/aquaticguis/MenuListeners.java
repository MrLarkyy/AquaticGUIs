package xyz.larkyy.aquaticguis;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MenuListeners implements Listener {

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof AquaticMenu menu) {
            menu.getCloseActions().run((Player) e.getPlayer());
        }
    }

}
