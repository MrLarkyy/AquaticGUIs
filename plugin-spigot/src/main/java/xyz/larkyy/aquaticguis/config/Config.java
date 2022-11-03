package xyz.larkyy.aquaticguis.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.larkyy.aquaticguis.action.ActionList;
import xyz.larkyy.aquaticguis.action.Actions;
import xyz.larkyy.aquaticguis.action.ConfiguredAction;
import xyz.larkyy.aquaticguis.api.InventoryActionEvent;
import xyz.larkyy.aquaticguis.clickaction.ClickActions;
import xyz.larkyy.aquaticguis.condition.ConditionList;
import xyz.larkyy.aquaticguis.condition.Conditions;
import xyz.larkyy.aquaticguis.condition.ConfiguredCondition;
import xyz.larkyy.aquaticguis.menu.AquaticMenuItem;
import xyz.larkyy.aquaticguis.menu.title.MenuTitle;
import xyz.larkyy.itemlibrary.CustomItem;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Config {

    private final File file;
    private FileConfiguration config;
    private final JavaPlugin main;

    public Config(JavaPlugin main, String path) {
        this.main = main;
        this.file = new File(main.getDataFolder(), path);
    }

    public Config(JavaPlugin main,File file) {
        this.main = main;
        this.file = file;
    }

    public void load() {
        if (!this.file.exists()) {
            try {
                this.main.saveResource(this.file.getName(), false);
            } catch (IllegalArgumentException var4) {
                try {
                    this.file.createNewFile();
                } catch (IOException var3) {
                    var3.printStackTrace();
                }
            }
        }

        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public FileConfiguration getConfiguration() {
        if (this.config == null) {
            this.load();
        }
        return this.config;
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }

    public ActionList loadActionList(String path) {
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

    public ConfiguredAction loadAction(String path) {

        var action = translateAction(getConfiguration().getString(path+".value"));
        if (action == null) {
            return null;
        }
        if (getConfiguration().contains(path+".conditions")) {
            action.setConditions(loadConditionList(path+".conditions"));
        }
        return action;
    }

    public ConfiguredAction translateAction(String value) {
        String args;
        if (value.startsWith("[message]")) {
            args = value.substring(9).trim();
            return new ConfiguredAction(
                    new ConditionList(new ActionList(), new ActionList()),
                    Actions.inst().getAction("message"),args
            );
        }
        if (value.startsWith("[openmenu]")) {
            args = value.substring(10).trim();
            return new ConfiguredAction(
                    new ConditionList(new ActionList(), new ActionList()),
                    Actions.inst().getAction("openmenu"),args
            );
        }
        if (value.startsWith("[closemenu]")) {
            args = value.substring(11).trim();
            return new ConfiguredAction(
                    new ConditionList(new ActionList(), new ActionList()),
                    Actions.inst().getAction("closemenu"),args
            );
        }
        return null;
    }

    public ConditionList loadConditionList(String path) {
        if (!getConfiguration().contains(path)) {
            return new ConditionList();
        }
        List<ConfiguredCondition> conditions = new ArrayList<>();
        for (String str : getConfiguration().getConfigurationSection(path).getKeys(false)) {

            ConfiguredCondition condition = loadCondition(path+"."+str);
            if (condition != null) {
                conditions.add(condition);
            }
        }
        if (conditions.isEmpty()) {
            return new ConditionList(new ActionList(), new ActionList(),conditions);
        }
        ActionList successActions = loadActionList(path+".success-actions");
        ActionList failActions = loadActionList(path+".fail-actions");

        return new ConditionList(failActions,successActions,conditions);
    }

    public ConfiguredCondition loadCondition(String path) {
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

    public MenuTitle loadMenuTitle() {
        List<String> frames;
        Object cfgVal = getConfiguration().get("title");
        if (cfgVal == null) {
            return new MenuTitle(new ArrayList<>(Arrays.asList("")),-1);
        }
        if (cfgVal instanceof List<?>) {
            frames = (List<String>)cfgVal;
        } else {
            frames = new ArrayList<>();
            frames.add(cfgVal.toString());
        }
        int update = getConfiguration().getInt("title-update",-1);
        return new MenuTitle(frames,update);
    }

    public AquaticMenuItem loadMenuItem(String path) {
        var is = CustomItem.loadFromYaml(getConfiguration(),path).getItem();
        List<Integer> slots = getConfiguration().getIntegerList(path+".slots");
        int priority = getConfiguration().getInt(path+".priority");
        var clickActions = loadClickActions(path);

        return new AquaticMenuItem(is,slots,priority,clickActions);
    }

    public ClickActions loadClickActions(String path) {
        Map<InventoryActionEvent.ActionType,ActionList> clickActions = new HashMap<>();

        ActionList actionList;

        actionList = loadActionList(path+".left-click-actions");
        if (!actionList.isEmpty()) {
            clickActions.put(InventoryActionEvent.ActionType.LEFT,actionList);
        }
        actionList = loadActionList(path+".shift-left-click-actions");
        if (!actionList.isEmpty()) {
            clickActions.put(InventoryActionEvent.ActionType.SHIFT_LEFT,actionList);
        }
        actionList = loadActionList(path+".right-click-actions");
        if (!actionList.isEmpty()) {
            clickActions.put(InventoryActionEvent.ActionType.RIGHT,actionList);
        }
        actionList = loadActionList(path+".shift-right-click-actions");
        if (!actionList.isEmpty()) {
            clickActions.put(InventoryActionEvent.ActionType.SHIFT_RIGHT,actionList);
        }
        actionList = loadActionList(path+".drop-actions");
        if (!actionList.isEmpty()) {
            clickActions.put(InventoryActionEvent.ActionType.THROW,actionList);
        }
        actionList = loadActionList(path+".num-1-actions");
        if (!actionList.isEmpty()) {
            clickActions.put(InventoryActionEvent.ActionType.NUM_1,actionList);
        }
        actionList = loadActionList(path+".num-2-actions");
        if (!actionList.isEmpty()) {
            clickActions.put(InventoryActionEvent.ActionType.NUM_2,actionList);
        }
        actionList = loadActionList(path+".num-3-actions");
        if (!actionList.isEmpty()) {
            clickActions.put(InventoryActionEvent.ActionType.NUM_3,actionList);
        }
        actionList = loadActionList(path+".num-4-actions");
        if (!actionList.isEmpty()) {
            clickActions.put(InventoryActionEvent.ActionType.NUM_4,actionList);
        }
        actionList = loadActionList(path+".num-5-actions");
        if (!actionList.isEmpty()) {
            clickActions.put(InventoryActionEvent.ActionType.NUM_5,actionList);
        }
        actionList = loadActionList(path+".num-6-actions");
        if (!actionList.isEmpty()) {
            clickActions.put(InventoryActionEvent.ActionType.NUM_6,actionList);
        }
        actionList = loadActionList(path+".num-7-actions");
        if (!actionList.isEmpty()) {
            clickActions.put(InventoryActionEvent.ActionType.NUM_7,actionList);
        }
        actionList = loadActionList(path+".num-8-actions");
        if (!actionList.isEmpty()) {
            clickActions.put(InventoryActionEvent.ActionType.NUM_8,actionList);
        }
        actionList = loadActionList(path+".num-9-actions");
        if (!actionList.isEmpty()) {
            clickActions.put(InventoryActionEvent.ActionType.NUM_9,actionList);
        }
        actionList = loadActionList(path+".swap-actions");
        if (!actionList.isEmpty()) {
            clickActions.put(InventoryActionEvent.ActionType.SWAP,actionList);
        }

        return new ClickActions(clickActions);
    }

}
