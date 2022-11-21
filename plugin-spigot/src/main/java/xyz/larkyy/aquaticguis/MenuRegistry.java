package xyz.larkyy.aquaticguis;
import xyz.larkyy.aquaticguis.config.MenuConfig;
import xyz.larkyy.aquaticguis.menu.AquaticFakeMenu;
import xyz.larkyy.aquaticguis.menu.AquaticMenu;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MenuRegistry {
    private final Map<String, AquaticMenu> menus = new HashMap<>();
    private final Map<String, AquaticFakeMenu> fakeMenus = new HashMap<>();

    public void load() {
        menus.clear();

        File menusFolder = new File(AquaticGUIs.getInstance().getDataFolder(),"menus/");
        menusFolder.mkdirs();
        File fakeMenusFolder = new File(AquaticGUIs.getInstance().getDataFolder(),"fakemenus/");
        fakeMenusFolder.mkdirs();

        for (File file : menusFolder.listFiles()) {
            AquaticMenu menu = new MenuConfig(AquaticGUIs.getInstance(),file).loadMenu();
            menus.put(menu.getId(),menu);
        }

        for (File file : fakeMenusFolder.listFiles()) {
            AquaticFakeMenu menu = new MenuConfig(AquaticGUIs.getInstance(),file).loadFakeMenu();
            fakeMenus.put(menu.getId(),menu);
            menu.register();
        }
    }

    public AquaticMenu getMenu(String id) {
        return menus.get(id);
    }

    public Map<String, AquaticMenu> getMenus() {
        return menus;
    }
}
