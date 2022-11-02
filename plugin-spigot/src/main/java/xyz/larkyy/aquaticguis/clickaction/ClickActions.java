package xyz.larkyy.aquaticguis.clickaction;

import org.bukkit.entity.Player;
import xyz.larkyy.aquaticguis.action.ActionList;
import xyz.larkyy.aquaticguis.api.InventoryActionEvent;

import java.util.HashMap;
import java.util.Map;

public class ClickActions {

    private final Map<InventoryActionEvent.ActionType, ActionList> clickActions;

    public ClickActions() {
        clickActions = new HashMap<>();
    }

    public ClickActions(Map<InventoryActionEvent.ActionType, ActionList> clickActions) {
        this.clickActions = clickActions;
    }

    public void run(InventoryActionEvent.ActionType clickType, Player player) {
        var action = clickActions.get(clickType);
        if (action == null) {
            return;
        }
        action.run(player);
    }

}
