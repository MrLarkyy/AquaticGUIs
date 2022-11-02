package xyz.larkyy.aquaticguis.condition;

import org.bukkit.entity.Player;
import xyz.larkyy.aquaticguis.action.ActionList;

import java.util.ArrayList;
import java.util.List;

public class ConditionList {

    private final List<ConfiguredCondition> conditions;
    private final ActionList failActions;
    private final ActionList successActions;

    public ConditionList() {
        failActions = null;
        successActions = null;
        conditions = new ArrayList<>();
    }
    public ConditionList(ActionList failActions, ActionList successActions) {
        this.failActions = failActions;
        this.successActions = successActions;
        conditions = new ArrayList<>();
    }
    public ConditionList(ActionList failActions, ActionList successActions,List<ConfiguredCondition> configuredConditions) {
        this.failActions = failActions;
        this.successActions = successActions;
        this.conditions = new ArrayList<>(configuredConditions);
    }

    public void addCondition(ConfiguredCondition condition) {
        conditions.add(condition);
    }

    public boolean areMet(Player player) {
        if (conditions.stream().allMatch(c -> c.isMet(player))) {
            if (successActions != null) {
                successActions.run(player);
            }
            return true;
        } else {
            if (failActions != null) {
                failActions.run(player);
            }
            return false;
        }
    }

}
