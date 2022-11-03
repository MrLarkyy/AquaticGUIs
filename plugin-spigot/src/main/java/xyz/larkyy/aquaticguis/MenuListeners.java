package xyz.larkyy.aquaticguis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.aquaticguis.api.Menu;
import xyz.larkyy.aquaticguis.menu.AquaticMenu;
import xyz.larkyy.aquaticguis.menu.title.MenuTitleSession;

public class MenuListeners implements Listener {

     @EventHandler
    public void rpStatus(PlayerResourcePackStatusEvent e) {
        if (e.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
            if (e.getPlayer() != null && e.getPlayer().isOnline()) {
                AquaticGUIs.getInstance().getManager().getResourcePackLoadActions().run(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e) {
         if (e.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof Menu menu) {
             e.setCancelled(true);
         }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof AquaticMenu menu) {
            menu.getCloseActions().run((Player) e.getPlayer());
            var ms = menu.getSession(((Player) e.getPlayer()));
            if (ms != null) {
                MenuTitleSession titleSession = (MenuTitleSession) ms.getData("title-session");
                titleSession.stop();
                menu.removeSession((Player) e.getPlayer());
            }
        }

        if (e.getInventory().getHolder() instanceof AquaticMenu menu) {
            menu.removeSession((Player) e.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (e.getPlayer() != null && e.getPlayer().isOnline()) {
                    if (!e.getPlayer().hasPlayedBefore()) {
                        AquaticGUIs.getInstance().getManager().getFirstJoinActions().run(e.getPlayer());
                    }
                    AquaticGUIs.getInstance().getManager().getJoinActions().run(e.getPlayer());
                }
            }
        }.runTaskLater(AquaticGUIs.getInstance(),1);

    }

}
