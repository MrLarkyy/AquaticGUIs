package xyz.larkyy.aquaticguis.api;


import org.bukkit.inventory.ItemStack;
import xyz.larkyy.aquaticguis.api.InventoryActionEvent;

import java.util.List;
import java.util.function.Consumer;

public class MenuItem {

    private ItemStack itemStack;
    private List<Consumer<InventoryActionEvent>> actions;
    private final List<Integer> slots;

    public MenuItem(ItemStack is, List<Consumer<InventoryActionEvent>> actions, List<Integer> slots) {
        this.itemStack = is;
        this.actions = actions;
        this.slots = slots;
    }

    public List<Integer> getSlots() {
        return slots;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void activate(InventoryActionEvent event) {
        for (Consumer<InventoryActionEvent> a : actions) {
            a.accept(event);
        }
    }
}
