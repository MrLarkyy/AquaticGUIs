package xyz.larkyy.aquaticguis.api;

import xyz.larkyy.aquaticguis.api.sessions.AbstractSession;

import java.util.HashMap;
import java.util.Map;

public class OpenedMenus {

    private final Map<Integer, AbstractSession> inventories = new HashMap<>();

    public void addMenu(AbstractSession session) {
        this.inventories.put(session.getInventoryId(),session);
    }

    public AbstractSession getMenu(int inventoryId) {
        return inventories.get(inventoryId);
    }

    public void removeMenu(int inventoryId) {
        this.inventories.remove(inventoryId);
    }

}
