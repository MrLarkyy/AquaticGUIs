package xyz.larkyy.aquaticguis;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.larkyy.aquaticguis.action.ActionList;
import xyz.larkyy.aquaticguis.action.ConfiguredAction;
import xyz.larkyy.aquaticguis.action.impl.MessageAction;
import xyz.larkyy.aquaticguis.api.MenuItem;
import xyz.larkyy.aquaticguis.condition.ConditionList;
import xyz.larkyy.aquaticguis.config.MenuConfig;
import xyz.larkyy.aquaticguis.menu.AquaticFakeMenu;
import xyz.larkyy.aquaticguis.menu.AquaticMenu;
import xyz.larkyy.aquaticguis.menu.title.MenuTitle;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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

        var openActionList = new ActionList();
        openActionList.addAction(new ConfiguredAction(new ConditionList(),new MessageAction(),"Opened!"));
        var menu = new AquaticFakeMenu(
                "test",
                new MenuTitle(Arrays.asList("test"),-1),
                AquaticGUIs.getInstance().getNmsHandler(),
                openActionList,
                new ActionList()
        );
        menu.register();
        menu.addMenuItem(new MenuItem(
                new ItemStack(Material.STONE),
                new ArrayList<>(),
                Arrays.asList(1)
        ));

        fakeMenus.put("test",menu);
    }

    public AquaticMenu getMenu(String id) {
        return menus.get(id);
    }

}
