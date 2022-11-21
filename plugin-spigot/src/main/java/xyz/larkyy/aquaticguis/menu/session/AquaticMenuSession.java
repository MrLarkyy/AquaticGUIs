package xyz.larkyy.aquaticguis.menu.session;

import org.bukkit.entity.Player;
import xyz.larkyy.aquaticguis.api.MenuItem;
import xyz.larkyy.aquaticguis.api.sessions.impl.MenuSession;
import xyz.larkyy.aquaticguis.menu.AquaticMenu;
import xyz.larkyy.aquaticguis.menu.AquaticMenuItem;
import xyz.larkyy.aquaticguis.menu.MenuUpdator;
import xyz.larkyy.aquaticguis.menu.Updatable;

import java.util.HashMap;
import java.util.Map;

public class AquaticMenuSession extends MenuSession {
    private boolean readyToUpdate = false;
    private final MenuUpdator updator;

    public AquaticMenuSession(Player player, AquaticMenu menu, int size) {
        super(player, menu, size);
        updator = new MenuUpdator(this);
        loadUpdatables();
        updator.start();
        updateMenuItemValues();
        update();
    }

    private void loadUpdatables() {
        getItems().values().forEach(item -> {
            if (item instanceof Updatable updatable) {
                updator.addUpdatable(updatable);
            }
        });
    }

    public void updateMenuItemValues() {
        Map<Integer, MenuItem> updatedItems = new HashMap<>();
        for (Map.Entry<Integer, MenuItem> entry : getItems().entrySet()) {
            Integer slot = entry.getKey();
            MenuItem item = entry.getValue();

            if (updatedItems.containsKey(slot)) {
                continue;
            }

            if (item instanceof AquaticMenuItem ami) {
                var menuItem = ami.getMenuItem(getPlayer());
                for (int i : menuItem.getSlots()) {
                    updatedItems.put(i, menuItem);
                }
            } else {
                for (int i : item.getSlots()) {
                    updatedItems.put(i, item);
                }
            }
        }
        setItems(updatedItems);
        readyToUpdate = true;
    }

    public boolean updateMenuItemValue(int slot) {
        var item = getItem(slot);
        if (item == null) {
            return false;
        }
        if (item instanceof AquaticMenuItem ami) {
            var changedAmi = ami.getMenuItem(getPlayer());
            if (changedAmi == ami) {
                return false;
            }
            changedAmi.getSlots().forEach(i -> {
                getItems().put(i,changedAmi);
            });
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        if (readyToUpdate) {
            super.update();
        }
    }


    @Override
    public void loadItems() {
        if (readyToUpdate) {
            super.loadItems();
        }
    }

    public void close() {
        updator.end();
    }
}
