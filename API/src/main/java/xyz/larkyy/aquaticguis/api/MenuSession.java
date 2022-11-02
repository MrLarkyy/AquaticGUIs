package xyz.larkyy.aquaticguis.api;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class MenuSession {

    private final Player player;
    private final int inventoryId;
    private final Map<Integer, MenuItem> items;
    private int size;
    private Menu.Type type;
    private String title;
    private final NMSHandler nmsHandler;
    private final Map<String,Object> data;
    private final Menu menu;

    public MenuSession(Player player, Map<Integer,MenuItem> items, int size, String title, Menu.Type type, NMSHandler nmsHandler, Menu menu) {
        this.player = player;
        this.items = items;
        this.size = size;
        this.type = type;
        this.title = title;
        this.nmsHandler = nmsHandler;
        this.data = new HashMap<>();
        this.menu = menu;

        inventoryId = nmsHandler.getInventoryId(player);
        update();
    }

    public void activate(InventoryActionEvent event) {
        MenuItem mi = items.get(event.getClickedSlot());
        if (mi == null) {
            return;
        }
        mi.activate(event);
    }

    public void setMenuItem(MenuItem menuItem) {
        menuItem.getSlots().forEach(slot -> {
            items.put(slot,menuItem);
            //nmsHandler.setSlotPacket(player,inventoryId,slot,menuItem.getItemStack());
        });
    }

    public Map<Integer, MenuItem> getItems() {
        return items;
    }

    public MenuItem getItem(int slot) {
        return items.get(slot);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void update() {
        new BukkitRunnable() {
            @Override
            public void run() {
                nmsHandler.emptyPlayerInventory(player,true);
                updateTitle();
            }
        }.runTaskAsynchronously(nmsHandler.getPlugin());
    }

    public void updateTitle() {
        nmsHandler.openScreen(player,inventoryId,Menu.translateMenuType(type,size),title);
        loadItems();
    }

    private void loadItems() {
        for (Map.Entry<Integer, MenuItem> entry : items.entrySet()) {
            Integer slot = entry.getKey();
            MenuItem menuItem = entry.getValue();
            nmsHandler.setSlotPacket(player, inventoryId, slot, menuItem.getItemStack());
        }
    }

    public Object getData(String id) {
        return data.get(id);
    }

    public void addData(String id, Object data) {
        this.data.put(id,data);
    }

    public void removeData(String id) {
        this.data.remove(id);
    }

    public Player getPlayer() {
        return player;
    }

    public Menu getMenu() {
        return menu;
    }
}
