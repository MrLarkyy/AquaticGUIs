package xyz.larkyy.aquaticguis;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.aquaticguis.api.MenuCloseEvent;
import xyz.larkyy.aquaticguis.api.NMSHandler;
import xyz.larkyy.aquaticguis.menu.AquaticFakeMenu;
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
         int id = nmsHandler().getInventoryId(e.getPlayer());
         var session = nmsHandler().getOpenedMenus().getMenu(id);
         if (session != null) {
             e.setCancelled(true);
         }
    }

    @EventHandler
    public void onInvClose(MenuCloseEvent e) {
         var session = e.getSession();
         var player = e.getPlayer();

         if (session.getMenu() instanceof AquaticMenu menu) {
             menu.getCloseActions().run(player);
             MenuTitleSession titleSession = (MenuTitleSession) session.getData("title-session");
             titleSession.stop();
         }

        if (session.getMenu() instanceof AquaticFakeMenu menu) {
            menu.getCloseActions().run(player);
            MenuTitleSession titleSession = (MenuTitleSession) session.getData("title-session");
            titleSession.stop();
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

    private NMSHandler nmsHandler() {
         return AquaticGUIs.getInstance().getNmsHandler();
    }

}
