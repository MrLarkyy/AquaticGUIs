package xyz.larkyy.aquaticguis.action;

import xyz.larkyy.aquaticguis.AquaticGUIs;
import xyz.larkyy.aquaticguis.action.impl.*;

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
        registerAction("closemenu", new CloseMenuAction());
        registerAction("consolecommand", new ConsoleCommandAction());
        registerAction("updatemenu", new UpdateMenuAction());
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

    public Map<String, Action> getActionTypes() {
        return actionTypes;
    }

    public static Actions inst() {
        return AquaticGUIs.getInstance().getActions();
    }
}
