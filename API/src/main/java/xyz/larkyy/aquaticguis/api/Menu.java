package xyz.larkyy.aquaticguis.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class Menu implements InventoryHolder {

    public enum Type {
        CHEST(InventoryType.CHEST),
        DISPENSER(InventoryType.DISPENSER),
        CRAFTING(InventoryType.CRAFTING);

        private final InventoryType invType;

        Type(InventoryType invType) {
            this.invType = invType;
        }

        public InventoryType getInvType() {
            return invType;
        }
    }

    private final Map<Integer,MenuItem> items = new HashMap<>();
    private final String title;
    private final int size;
    private Type type;
    private final Map<UUID,MenuSession> sessions = new HashMap<>();

    private Inventory inventory;
    private NMSHandler nmsHandler;

    public Menu(String title, int size, Type type, NMSHandler nmsHandler) {
        this.title = title;
        this.size = size;
        this.type = type;
        this. nmsHandler = nmsHandler;
        buildInventory();
    }

    public Menu(String title, int size, Type type, NMSHandler nmsHandler, Consumer<Menu> factory) {
        this.title = title;
        this.size = size;
        this.type = type;
        this.nmsHandler = nmsHandler;
        buildInventory();
        factory.accept(this);
    }

    private void buildInventory() {
        if (type.getInvType() == InventoryType.CHEST) {
            inventory = Bukkit.createInventory(this,size,title);
        } else {
            inventory = Bukkit.createInventory(this,type.getInvType(),title);
        }
    }

    public MenuSession open(Player player) {
        nmsHandler.emptyPlayerInventory(player,false);
        player.openInventory(inventory);
        MenuSession ms = new MenuSession(player,items,size,title,type,nmsHandler);
        sessions.put(player.getUniqueId(),ms);
        return ms;
    }

    public MenuSession open(Player player, Consumer<MenuSession> factory) {
        MenuSession ms = open(player);
        factory.accept(ms);
        return ms;
    }


    public void setTitle(Player player, String title) {
        MenuSession ms = sessions.get(player.getUniqueId());
        if (ms == null) {
            return;
        }
        ms.setTitle(title);
    }

    public void addMenuItem(MenuItem menuItem) {
        for (Integer slot : menuItem.getSlots()) {
            items.put(slot, menuItem);
        }
    }

    public void setMenuItem(Player player, MenuItem menuItem) {
        MenuSession ms = sessions.get(player.getUniqueId());
        if (ms == null) {
            return;
        }
        ms.setMenuItem(menuItem);
    }

    public MenuSession getSession(Player player) {
        return sessions.get(player.getUniqueId());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
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
}
