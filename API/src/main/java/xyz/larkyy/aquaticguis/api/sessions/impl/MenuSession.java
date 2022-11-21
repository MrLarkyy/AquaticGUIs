package xyz.larkyy.aquaticguis.api.sessions.impl;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.aquaticguis.api.MenuItem;
import xyz.larkyy.aquaticguis.api.menus.AbstractMenu;
import xyz.larkyy.aquaticguis.api.menus.impl.Menu;
import xyz.larkyy.aquaticguis.api.sessions.AbstractSession;

import java.util.Map;

public class MenuSession extends AbstractSession {

    public MenuSession(Player player, Menu menu, int size) {
        super(player, menu, size);
        update();
    }

    public void update() {
        new BukkitRunnable() {
            @Override
            public void run() {
                nmsHandler().emptyPlayerInventory(getPlayer(),true);
                updateTitle();
            }
        }.runTaskAsynchronously(nmsHandler().getPlugin());
    }

    public void updateTitle() {
        nmsHandler().openScreen(getPlayer(),getInventoryId(), AbstractMenu.translateMenuType(getMenu().getType(),getSize()),getTitle());
        loadItems();
    }

    public void loadItems() {
        for (Map.Entry<Integer, MenuItem> entry : getItems().entrySet()) {
            Integer slot = entry.getKey();
            MenuItem menuItem = entry.getValue();
            nmsHandler().setSlotPacket(getPlayer(), getInventoryId(), slot, menuItem.getItemStack());
        }
    }
}
