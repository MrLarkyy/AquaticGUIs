package xyz.larkyy.aquaticguis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.aquaticguis.api.MenuActionEvent;
import xyz.larkyy.aquaticguis.api.MenuCloseEvent;
import xyz.larkyy.aquaticguis.api.NMSHandler;
import xyz.larkyy.aquaticguis.menu.AquaticFakeMenu;
import xyz.larkyy.aquaticguis.menu.AquaticMenu;
import xyz.larkyy.aquaticguis.menu.AquaticMenuItem;
import xyz.larkyy.aquaticguis.menu.session.AquaticMenuSession;
import xyz.larkyy.aquaticguis.menu.title.MenuTitleSession;

import java.util.ArrayList;
import java.util.List;

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

         if (session instanceof AquaticMenuSession ams) {
             ams.close();
             new BukkitRunnable() {
                 @Override
                 public void run() {
                     player.updateInventory();
                 }
             }.runTaskLater(AquaticGUIs.getInstance(),1);
         }

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

    @EventHandler
    public void onInvClick(MenuActionEvent e) {
         /*
        if (e.getMenuSession() instanceof AquaticMenuSession ams) {
            if (ams.updateMenuItemValue(e.getClickedSlot())) {
                if (e.getMenuItem() instanceof AquaticMenuItem ami) {
                    Bukkit.broadcastMessage("Placing new items!");
                    ami.place(ams.getInventoryId(),e.getPlayer());
                }
            }
        }

          */
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
