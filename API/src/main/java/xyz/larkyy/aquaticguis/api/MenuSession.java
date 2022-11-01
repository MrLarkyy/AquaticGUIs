package xyz.larkyy.aquaticguis.api;

import org.bukkit.entity.Player;

import java.util.Map;

public class MenuSession {

    private final Player player;
    private final int inventoryId;
    private final Map<Integer, MenuItem> items;
    private int size;
    private Menu.Type type;
    private String title;
    private final NMSHandler nmsHandler;

    public MenuSession(Player player, Map<Integer,MenuItem> items, int size, String title, Menu.Type type, NMSHandler nmsHandler) {
        this.player = player;
        this.items = items;
        this.size = size;
        this.type = type;
        this.title = title;
        this.nmsHandler = nmsHandler;

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
            nmsHandler.setSlotPacket(player,inventoryId,slot,menuItem.getItemStack());
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
        nmsHandler.openScreen(player,inventoryId,Menu.translateMenuType(type,size),title);
        nmsHandler.emptyPlayerInventory(player,true);
        loadItems();
    }

    private void loadItems() {
        for (Map.Entry<Integer, MenuItem> entry : items.entrySet()) {
            Integer slot = entry.getKey();
            MenuItem menuItem = entry.getValue();
            int actualInventoryId = inventoryId;
            int actualSlot;

            if (slot > size + 35) {
                continue;
            }

            if (slot < size) {
                actualSlot = slot;
            } else {
                actualInventoryId = 0;
                actualSlot = size - (size - 9);
                if (actualSlot >= 36) {
                    actualSlot -= 36;
                }
            }

            nmsHandler.setSlotPacket(player, actualInventoryId, actualSlot, menuItem.getItemStack());
        }
    }

}
