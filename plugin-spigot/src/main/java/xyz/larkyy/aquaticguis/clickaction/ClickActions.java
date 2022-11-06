package xyz.larkyy.aquaticguis.clickaction;

import org.bukkit.entity.Player;
import xyz.larkyy.aquaticguis.action.ActionList;
import xyz.larkyy.aquaticguis.api.MenuActionEvent;

import java.util.HashMap;
import java.util.Map;

public class ClickActions {

    private final Map<MenuActionEvent.ActionType, ActionList> clickActions;

    public ClickActions() {
        clickActions = new HashMap<>();
    }

    public ClickActions(Map<MenuActionEvent.ActionType, ActionList> clickActions) {
        this.clickActions = clickActions;
    }

    public void run(MenuActionEvent.ActionType clickType, Player player) {
        var action = clickActions.get(clickType);
        if (action == null) {
            return;
        }
        action.run(player);
    }

}
