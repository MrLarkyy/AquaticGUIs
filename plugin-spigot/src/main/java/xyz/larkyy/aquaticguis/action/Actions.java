package xyz.larkyy.aquaticguis.action;

import xyz.larkyy.aquaticguis.AquaticGUIs;
import xyz.larkyy.aquaticguis.action.impl.MessageAction;
import xyz.larkyy.aquaticguis.action.impl.OpenMenuAction;

import java.util.HashMap;
import java.util.Map;

public class Actions {

    private final Map<String, Action> actionTypes;

    public Actions() {
        actionTypes = new HashMap<>();
        loadDefaultActions();
    }

    private void loadDefaultActions() {
        registerAction("message", new MessageAction());
        registerAction("openmenu", new OpenMenuAction());
    }

    public void registerAction(String id, Action action) {
        actionTypes.put(id.toUpperCase(),action);
    }

    public void unregisterAction(String id) {
        actionTypes.remove(id.toUpperCase());
    }

    public Action getAction(String id) {
        return actionTypes.get(id.toUpperCase());
    }


    public static Actions inst() {
        return AquaticGUIs.getInstance().getActions();
    }
}
