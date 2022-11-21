package xyz.larkyy.aquaticguis.api.sessions.impl;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.aquaticguis.api.MenuItem;
import xyz.larkyy.aquaticguis.api.menus.AbstractMenu;
import xyz.larkyy.aquaticguis.api.sessions.AbstractSession;

import java.util.Arrays;
import java.util.Map;

public class FakeMenuSession extends AbstractSession {

    public FakeMenuSession(Player player, AbstractMenu menu, int size) {
        super(player, menu, size);
        update();
        loadItems();
    }

    public void update() {
        new BukkitRunnable() {
            @Override
            public void run() {
                //nmsHandler().emptyPlayerInventory(getPlayer(),true);
                updateTitle();
            }
        }.runTaskAsynchronously(nmsHandler().getPlugin());
    }

    public void updateTitle() {
        nmsHandler().openScreen(getPlayer(),getInventoryId(), AbstractMenu.translateMenuType(getMenu().getType(),getSize()),getTitle());
        loadItems();
    }

    private void loadItems() {
        nmsHandler().setContentPacket(
                getPlayer(),
                getInventoryId(),
                Arrays.stream(getPlayer().getOpenInventory().getTopInventory().getContents()).toList(),
                true
        );


        for (Map.Entry<Integer, MenuItem> entry : getItems().entrySet()) {
            Integer slot = entry.getKey();
            MenuItem menuItem = entry.getValue();
            nmsHandler().setSlotPacket(getPlayer(), getInventoryId(), slot, menuItem.getItemStack());
        }
    }
}
