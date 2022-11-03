package xyz.larkyy.aquaticguis.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.larkyy.aquaticguis.AquaticGUIs;
import xyz.larkyy.aquaticguis.menu.AquaticMenu;
import xyz.larkyy.aquaticguis.action.ActionList;
import xyz.larkyy.aquaticguis.api.Menu;
import xyz.larkyy.aquaticguis.condition.ConditionList;

import java.io.File;

public class MenuConfig extends Config {

    public MenuConfig(JavaPlugin main, File file) {
        super(main, file);
    }

    public AquaticMenu loadMenu() {
        FileConfiguration cfg = getConfiguration();

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
}
