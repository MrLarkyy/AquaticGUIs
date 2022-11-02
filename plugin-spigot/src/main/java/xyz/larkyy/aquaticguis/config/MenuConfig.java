package xyz.larkyy.aquaticguis.config;

import cz.larkyy.shaded.colorutils.Colors;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.larkyy.aquaticguis.AquaticGUIs;
import xyz.larkyy.aquaticguis.menu.AquaticMenu;
import xyz.larkyy.aquaticguis.menu.AquaticMenuItem;
import xyz.larkyy.aquaticguis.action.ActionList;
import xyz.larkyy.aquaticguis.api.InventoryActionEvent;
import xyz.larkyy.aquaticguis.api.Menu;
import xyz.larkyy.aquaticguis.clickaction.ClickActions;
import xyz.larkyy.aquaticguis.condition.ConditionList;
import xyz.larkyy.aquaticguis.menu.title.MenuTitle;
import xyz.larkyy.itemlibrary.CustomItem;

import java.io.File;
import java.util.*;

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

        var menu = new AquaticMenu(identifier,
                loadMenuTitle(),
                size,
                type,
                AquaticGUIs.getInstance().getNmsHandler(),
                openConditions,
                openActions,
                closeAction
        );

        if (getConfiguration().contains("items")) {
            getConfiguration().getConfigurationSection("items").getKeys(false).forEach(key -> {
                menu.addMenuItem(loadMenuItem("items."+key));
            });
        }

        return menu;
    }

    private MenuTitle loadMenuTitle() {
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

    private AquaticMenuItem loadMenuItem(String path) {
        var is = CustomItem.loadFromYaml(getConfiguration(),path).getItem();
        List<Integer> slots = getConfiguration().getIntegerList(path+".slots");
        int priority = getConfiguration().getInt(path+".priority");
        var clickActions = loadClickActions(path);

        return new AquaticMenuItem(is,slots,priority,clickActions);
    }

    private ClickActions loadClickActions(String path) {
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
