package xyz.larkyy.aquaticguis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.larkyy.aquaticguis.action.Actions;
import xyz.larkyy.aquaticguis.api.NMSHandler;
import xyz.larkyy.aquaticguis.condition.Conditions;
import xyz.larkyy.aquaticguis.nms.NMS_v1_19_R2;

public final class AquaticGUIs extends JavaPlugin {

    private static AquaticGUIs instance;
    private NMSHandler nmsHandler;
    private Conditions conditions;
    private Actions actions;
    private MenuRegistry menuRegistry;

    @Override
    public void onEnable() {
        instance = this;
        nmsHandler = new NMS_v1_19_R2(this);
        conditions = new Conditions();
        actions = new Actions();
        menuRegistry = new MenuRegistry();

        validatePlayers();

        getServer().getPluginManager().registerEvents(new ReaderListeners(),this);
        getServer().getPluginManager().registerEvents(new MenuListeners(),this);

        menuRegistry.load();

        Player p = Bukkit.getPlayer("MrLarkyy_");
        var menu = menuRegistry.getMenu("testmenu");
        if (menu == null) {
            p.sendMessage("Menu is null");
            return;
        }
        menu.open(p);

        /*Menu menu = new Menu("Test Menu",9, Menu.Type.CHEST,nmsHandler, m -> {
            m.addMenuItem(new MenuItem(new ItemStack(Material.STONE), Arrays.asList(e -> {

            }), Arrays.asList(0)));
        });

        MenuSession session = menu.open(p);
        new BukkitRunnable() {
            @Override
            public void run() {
                session.setTitle("Just a new title lol");
                session.setMenuItem(new MenuItem(new ItemStack(Material.DIAMOND),new ArrayList<>(Arrays.asList(e -> {
                p.sendMessage("This is a packet based Item! (Click type:"+e.getActionType()+")");
                })), Arrays.asList(1,40)));
                session.update();
            }
        }.runTaskLater(this,0);

         */

    }

    @Override
    public void onDisable() {
        invalidatePlayers();
    }

    private void validatePlayers() {
        if(!Bukkit.getOnlinePlayers().isEmpty()) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                nmsHandler.injectPlayer(p);
            }
        }
    }

    private void invalidatePlayers() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            nmsHandler.ejectPlayer(p);
        }
    }

    public static AquaticGUIs getInstance() {
        return instance;
    }

    public NMSHandler getNmsHandler() {
        return nmsHandler;
    }

    public Conditions getConditions() {
        return conditions;
    }

    public Actions getActions() {
        return actions;
    }

    public MenuRegistry getMenuRegistry() {
        return menuRegistry;
    }
}
