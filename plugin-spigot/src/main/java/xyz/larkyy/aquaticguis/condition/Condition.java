package xyz.larkyy.aquaticguis.condition;

import org.bukkit.entity.Player;
import xyz.larkyy.aquaticguis.action.ActionList;

import java.util.List;
import java.util.Map;

public abstract class Condition {

    private final List<String> arguments;

    public Condition(List<String> arguments) {
        this.arguments = arguments;
    }

    public abstract boolean isMet(Player player, Map<String,String> filledArguments);

    public List<String> getArguments() {
        return arguments;
    }
}
