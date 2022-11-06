package xyz.larkyy.aquaticguis.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MenuActionEvent extends Event {

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
    private final Player player;
    private final int slot;
    private final MenuItem menuItem;
    private final ActionType actionType;

    public MenuActionEvent(Player player, int slot, MenuItem menuItem, ActionType actionType) {
        this.player = player;
        this.slot = slot;
        this.menuItem = menuItem;
        this.actionType = actionType;
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

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
