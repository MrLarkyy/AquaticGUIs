package xyz.larkyy.aquaticguis.api.menus.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import xyz.larkyy.aquaticguis.api.NMSHandler;
import xyz.larkyy.aquaticguis.api.menus.AbstractMenu;
import xyz.larkyy.aquaticguis.api.sessions.AbstractSession;
import xyz.larkyy.aquaticguis.api.sessions.impl.MenuSession;

public class Menu extends AbstractMenu implements InventoryHolder {

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
    @Override
    public AbstractSession open(Player player) {
        getNmsHandler().emptyPlayerInventory(player,false);
        player.openInventory(inventory);
        MenuSession ms = new MenuSession(player,this,size);
        getNmsHandler().getOpenedMenus().addMenu(ms);
        return ms;
    }
    private final Type type;
    private Inventory inventory;
    private final int size;

    public Menu(String title, NMSHandler nmsHandler, Type type, int size) {
        super(title, nmsHandler);
        this.type = type;
        this.size = size;
        buildInventory();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public Type getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    private void buildInventory() {
        if (type.getInvType() == InventoryType.CHEST) {
            inventory = Bukkit.createInventory(this,size,getTitle());
        } else {
            inventory = Bukkit.createInventory(this,type.getInvType(),getTitle());
        }
    }
}
