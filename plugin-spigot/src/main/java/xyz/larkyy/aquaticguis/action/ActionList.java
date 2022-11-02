package xyz.larkyy.aquaticguis.action;

import org.bukkit.entity.Player;
import xyz.larkyy.aquaticguis.condition.ConditionList;

import java.util.ArrayList;
import java.util.List;

public class ActionList {

    private final List<ConfiguredAction> actions;
    private final ConditionList conditions;

    public ActionList() {
        actions = new ArrayList<>();
        this.conditions = new ConditionList();
    }
    public ActionList(List<ConfiguredAction> actions,ConditionList conditions) {
        this.actions = actions;
        this.conditions = conditions;
    }

    public void addAction(ConfiguredAction action) {
        actions.add(action);
    }

    public boolean run(Player player) {
        if (conditions.areMet(player)) {
            actions.forEach(a -> {
                a.run(player);
            });
            return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return actions.isEmpty();
    }
}
