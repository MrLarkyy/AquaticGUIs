package xyz.larkyy.aquaticguis.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InventoryActionEvent extends Event implements Cancellable {

    public enum ActionType {
        LEFT,
        RIGHT,
        SHIFT_LEFT,
        SHIFT_RIGHT,
        THROW,
        SWAP,
        NUM_1,
        NUM_2,
        NUM_3,
        NUM_4,
        NUM_5,
        NUM_6,
        NUM_7,
        NUM_8,
        NUM_9
    }

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;
    private final Player player;
    private final int slot;
    private final MenuItem menuItem;
    private final ActionType actionType;

    public InventoryActionEvent(Player player, int slot, MenuItem menuItem, ActionType actionType) {
        this.player = player;
        this.slot = slot;
        this.menuItem = menuItem;
        this.actionType = actionType;
    }

    public MenuItem getClickedMenuItem() {
        return menuItem;
    }

    public Player getPlayer() {
        return player;
    }

    public int getClickedSlot() {
        return slot;
    }

    public ActionType getActionType() {
        return actionType;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}