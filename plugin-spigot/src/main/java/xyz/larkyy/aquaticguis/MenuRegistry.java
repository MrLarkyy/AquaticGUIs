package xyz.larkyy.aquaticguis;

import xyz.larkyy.aquaticguis.config.MenuConfig;
import xyz.larkyy.aquaticguis.menu.AquaticMenu;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MenuRegistry {
    private final Map<String, AquaticMenu> menus = new HashMap<>();

    public void load() {
        menus.clear();

        File cratesFolder = new File(AquaticGUIs.getInstance().getDataFolder(),"menus/");
        cratesFolder.mkdirs();

        for (File file : cratesFolder.listFiles()) {
            AquaticMenu menu = new MenuConfig(AquaticGUIs.getInstance(),file).loadMenu();
            menus.put(menu.getId(),menu);
        }
    }

    public AquaticMenu getMenu(String id) {
        return menus.get(id);
    }

}
