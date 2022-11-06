package xyz.larkyy.aquaticguis.api.sessions;

import org.bukkit.entity.Player;
import xyz.larkyy.aquaticguis.api.MenuActionEvent;
import xyz.larkyy.aquaticguis.api.MenuItem;
import xyz.larkyy.aquaticguis.api.NMSHandler;
import xyz.larkyy.aquaticguis.api.menus.AbstractMenu;
import xyz.larkyy.aquaticguis.api.menus.impl.Menu;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSession {

    private final Player player;
    private final Map<Integer, MenuItem> items;
    private int size;
    private String title;
    private final AbstractMenu menu;
    private final Map<String,Object> data;
    private final int inventoryId;

    public AbstractSession(Player player, AbstractMenu menu, int size) {
        this.player = player;
        this.menu = menu;
        this.items = new HashMap<>(menu.getItems());
        this.size = size;
        this.title = menu.getTitle();
        this.data = new HashMap<>();

        inventoryId = nmsHandler().getInventoryId(player);
        nmsHandler().getOpenedMenus().addMenu(this);
    }

    public NMSHandler nmsHandler() {
        return menu.getNmsHandler();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void activate(MenuActionEvent event) {
        var menuItem = items.get(event.getClickedSlot());
        if (menuItem == null) {
            return;
        }
        menuItem.activate(event);
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

    public AbstractMenu getMenu() {
        return menu;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public Map<Integer, MenuItem> getItems() {
        return items;
    }

    public int getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }
    public MenuItem getItem(int slot) {
        return items.get(slot);
    }

    public abstract void updateTitle();
}
