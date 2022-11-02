package xyz.larkyy.aquaticguis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.larkyy.aquaticguis.action.Actions;
import xyz.larkyy.aquaticguis.api.NMSHandler;
import xyz.larkyy.aquaticguis.commands.Commands;
import xyz.larkyy.aquaticguis.condition.Conditions;
import xyz.larkyy.aquaticguis.nms.NMS_v1_19_R2;

public final class AquaticGUIs extends JavaPlugin {

    private static AquaticGUIs instance;
    private NMSHandler nmsHandler;
    private Conditions conditions;
    private Actions actions;
    private Manager manager;
    private MenuRegistry menuRegistry;

    @Override
    public void onEnable() {
        instance = this;
        nmsHandler = new NMS_v1_19_R2(this);
        conditions = new Conditions();
        actions = new Actions();
        menuRegistry = new MenuRegistry();
        manager = new Manager();

        validatePlayers();

        getServer().getPluginManager().registerEvents(new ReaderListeners(),this);
        getServer().getPluginManager().registerEvents(new MenuListeners(),this);

        menuRegistry.load();
        getCommand("aquaticguis").setExecutor(new Commands());
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

    public Manager getManager() {
        return manager;
    }
}
