package xyz.larkyy.aquaticguis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.larkyy.aquaticguis.api.NMSHandler;
import xyz.larkyy.aquaticguis.nms.NMS_v1_19_R2;

public final class AquaticGUIs extends JavaPlugin {

    private static AquaticGUIs instance;
    private NMSHandler nmsHandler;

    @Override
    public void onEnable() {
        instance = this;
        nmsHandler = new NMS_v1_19_R2(this);
        validatePlayers();
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
