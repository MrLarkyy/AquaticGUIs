package xyz.larkyy.aquaticguis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.aquaticguis.api.Menu;
import xyz.larkyy.aquaticguis.api.MenuItem;
import xyz.larkyy.aquaticguis.api.MenuSession;
import xyz.larkyy.aquaticguis.api.NMSHandler;
import xyz.larkyy.aquaticguis.nms.NMS_v1_19_R2;

import java.util.ArrayList;
import java.util.Arrays;

public final class AquaticGUIs extends JavaPlugin {

    private static AquaticGUIs instance;
    private NMSHandler nmsHandler;

    @Override
    public void onEnable() {
        instance = this;
        nmsHandler = new NMS_v1_19_R2(this);
        validatePlayers();

        getServer().getPluginManager().registerEvents(new ReaderListeners(),this);

        Player p = Bukkit.getPlayer("MrLarkyy_");
        Menu menu = new Menu("Test Menu",18, Menu.Type.DEFAULT,nmsHandler, m -> {
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
            }
        }.runTaskLater(this,0);

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
}
