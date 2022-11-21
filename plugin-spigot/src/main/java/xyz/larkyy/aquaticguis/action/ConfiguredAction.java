package xyz.larkyy.aquaticguis.action;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.larkyy.aquaticguis.Utils;
import xyz.larkyy.aquaticguis.condition.ConditionList;

public class ConfiguredAction {

    private ConditionList conditions;
    private final Action action;
    private final String value;

    public ConfiguredAction(ConditionList conditions, Action action, String value) {
        this.conditions = conditions;
        this.action = action;
        this.value = value;
    }

    public boolean run(Player player) {
        if (conditions.areMet(player)) {
            action.run(player, Utils.format(value,player));
            return true;
        }
        return false;
    }

    public void setConditions(ConditionList conditions) {
        this.conditions = conditions;
    }
}
