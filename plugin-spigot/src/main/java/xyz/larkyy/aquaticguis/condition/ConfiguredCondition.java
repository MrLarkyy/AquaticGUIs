package xyz.larkyy.aquaticguis.condition;

import org.bukkit.entity.Player;
import xyz.larkyy.aquaticguis.action.ActionList;

import java.util.Map;
import java.util.function.Consumer;

public class ConfiguredCondition {

    private final Condition condition;
    private final ActionList failActions;
    private final ActionList successActions;
    private final Map<String,String> filledArgs;

    public ConfiguredCondition(Condition condition, Map<String,String> filledArgs,
                               ActionList failActions, ActionList successActions) {
        this.condition = condition;
        this.filledArgs = filledArgs;
        this.failActions = failActions;
        this.successActions = successActions;
    }

    public boolean isMet(Player player) {
        if (condition.isMet(player,filledArgs)) {
            successActions.run(player);
            return true;
        }
        failActions.run(player);
        return false;
    }

}
