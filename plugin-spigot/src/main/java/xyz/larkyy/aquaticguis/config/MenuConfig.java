package xyz.larkyy.aquaticguis.config;

import cz.larkyy.shaded.colorutils.Colors;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.larkyy.aquaticguis.AquaticGUIs;
import xyz.larkyy.aquaticguis.AquaticMenu;
import xyz.larkyy.aquaticguis.action.ActionList;
import xyz.larkyy.aquaticguis.action.Actions;
import xyz.larkyy.aquaticguis.action.ConfiguredAction;
import xyz.larkyy.aquaticguis.api.Menu;
import xyz.larkyy.aquaticguis.condition.ConditionList;
import xyz.larkyy.aquaticguis.condition.Conditions;
import xyz.larkyy.aquaticguis.condition.ConfiguredCondition;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuConfig extends Config {

    public MenuConfig(JavaPlugin main, File file) {
        super(main, file);
    }

    public AquaticMenu loadMenu() {
        FileConfiguration cfg = getConfiguration();

        String title = cfg.getString(Colors.format("title"));
        Menu.Type type = Menu.Type.valueOf(cfg.getString("type","CHEST").toUpperCase());
        int size = cfg.getInt("size",9);

        ConditionList openConditions = loadConditionList("open-conditions");
        ActionList openActions = loadActionList("open-actions");
        ActionList closeAction = loadActionList("close-actions");

        String identifier = getFile().getName().substring(0,getFile().getName().length()-4);

        return new AquaticMenu(identifier,
                title,
                size,
                type,
                AquaticGUIs.getInstance().getNmsHandler(),
                openConditions,
                openActions,
                closeAction
        );
    }

    private ActionList loadActionList(String path) {
        if (!getConfiguration().contains(path)) {
            return new ActionList();
        }
        List<ConfiguredAction> actions = new ArrayList<>();
        Object obj = getConfiguration().get(path);
        if (obj instanceof List<?>) {
            List<String> list = (List<String>) obj;
            for (String str : list) {
                ConfiguredAction action = translateAction(str);
                if (action == null) {
                    continue;
                }
                actions.add(action);
            }
            return new ActionList(actions,new ConditionList());
        } else {
            for (String str : getConfiguration().getConfigurationSection(path).getKeys(false)) {
                ConfiguredAction action = loadAction(path+"."+str);
                if (action == null) {
                    continue;
                }
                actions.add(action);
            }

            ConditionList conditionList;
            if (getConfiguration().contains(path+".conditions")) {
                conditionList = loadConditionList(path+".conditions");
            } else {
                conditionList = new ConditionList();
            }
            return new ActionList(actions,conditionList);
        }
    }

    private ConfiguredAction loadAction(String path) {

        var action = translateAction(getConfiguration().getString(path+".value"));
        if (action == null) {
            return null;
        }
        if (getConfiguration().contains(path+".conditions")) {
            action.setConditions(loadConditionList(path+".conditions"));
        }
        return action;
    }

    private ConfiguredAction translateAction(String value) {
        String args;
        if (value.startsWith("[message]")) {
            args = value.substring(9).trim();
            return new ConfiguredAction(
                    new ConditionList(new ActionList(), new ActionList()),
                    Actions.inst().getAction("message"),args
            );
        }
        return null;
    }

    private ConditionList loadConditionList(String path) {
        if (!getConfiguration().contains(path)) {
            return new ConditionList();
        }
        List<ConfiguredCondition> conditions = new ArrayList<>();
        for (String str : getConfiguration().getConfigurationSection(path).getKeys(false)) {

            ConfiguredCondition condition = loadCondition(path+"."+str);
            if (condition != null) {
                conditions.add(condition);
                Bukkit.broadcastMessage("Adding");
            }
        }
        if (conditions.isEmpty()) {
            return new ConditionList(new ActionList(), new ActionList(),conditions);
        }
        ActionList successActions = loadActionList(path+".success-actions");
        ActionList failActions = loadActionList(path+".fail-actions");

        return new ConditionList(failActions,successActions,conditions);
    }

    private ConfiguredCondition loadCondition(String path) {
        final FileConfiguration cfg = getConfiguration();
        String type = cfg.getString(path+".type");
        if (type == null) {
            return null;
        }
        var condition = Conditions.inst().getCondition(type);
        if (condition == null) {
            Bukkit.broadcastMessage("Unknown condition");
            return null;
        }

        Map<String,String> filledArgs = new HashMap<>();
        for (String arg : condition.getArguments()) {
            filledArgs.put(arg,cfg.getString(path+"."+arg));
        }

        ActionList successActions = loadActionList(path+".success-actions");
        ActionList failActions = loadActionList(path+".fail-actions");

        return new ConfiguredCondition(condition,filledArgs,successActions,failActions);
    }
}
