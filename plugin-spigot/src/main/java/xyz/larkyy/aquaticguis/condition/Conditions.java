package xyz.larkyy.aquaticguis.condition;

import xyz.larkyy.aquaticguis.AquaticGUIs;
import xyz.larkyy.aquaticguis.condition.impl.PermissionCondition;

import java.util.HashMap;
import java.util.Map;

public class Conditions {

    private final Map<String,Condition> conditionTypes;

    public Conditions() {
        conditionTypes = new HashMap<>();
        loadDefaultConditions();
    }

    private void loadDefaultConditions() {
        registerCondition("hasPermission", new PermissionCondition());
    }

    public void registerCondition(String id, Condition condition) {
        conditionTypes.put(id.toUpperCase(),condition);
    }

    public void unregisterCondition(String id) {
        conditionTypes.remove(id.toUpperCase());
    }

    public Condition getCondition(String id) {
        return conditionTypes.get(id.toUpperCase());
    }


    public static Conditions inst() {
        return AquaticGUIs.getInstance().getConditions();
    }
}
