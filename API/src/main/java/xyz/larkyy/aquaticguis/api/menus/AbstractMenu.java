package xyz.larkyy.aquaticguis.api.menus;

import org.bukkit.entity.Player;
import xyz.larkyy.aquaticguis.api.MenuItem;
import xyz.larkyy.aquaticguis.api.NMSHandler;
import xyz.larkyy.aquaticguis.api.menus.impl.Menu;
import xyz.larkyy.aquaticguis.api.sessions.AbstractSession;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractMenu {

    private final String title;
    private final NMSHandler nmsHandler;
    private Map<Integer, MenuItem> items = new HashMap<>();

    public AbstractMenu(String title, NMSHandler nmsHandler) {
        this.title = title;
        this.nmsHandler = nmsHandler;
    }

    public void addMenuItem(MenuItem menuItem) {
        for (Integer slot : menuItem.getSlots()) {
            items.put(slot, menuItem);
        }
    }

    public NMSHandler getNmsHandler() {
        return nmsHandler;
    }

    public Map<Integer, MenuItem> getItems() {
        return items;
    }

    public void setItems(Map<Integer, MenuItem> items) {
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public static String translateMenuType(Menu.Type type, int size) {
        switch (type) {
            case DISPENSER -> {
                return "generic_3x3";
            }
            default -> {
                switch (size) {
                    case 9 -> {
                        return "generic_9x1";
                    }
                    case 18 -> {
                        return "generic_9x2";
                    }
                    case 27 -> {
                        return "generic_9x3";
                    }
                    case 36 -> {
                        return "generic_9x4";
                    }
                    case 45 -> {
                        return "generic_9x5";
                    }
                    case 54 -> {
                        return "generic_9x6";
                    }
                }
            }
        }
        return "generic_9x1";
    }

    public abstract AbstractSession open(Player player);
    public AbstractSession open(Player player, Consumer<AbstractSession> factory) {
        var ms = open(player);
        factory.accept(ms);
        return ms;
    }

    public abstract Menu.Type getType();
}
