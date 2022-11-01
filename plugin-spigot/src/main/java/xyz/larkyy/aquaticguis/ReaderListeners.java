package xyz.larkyy.aquaticguis;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.larkyy.aquaticguis.api.NMSHandler;

public class ReaderListeners implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        nmsHandler().injectPlayer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        nmsHandler().ejectPlayer(e.getPlayer());
    }

    private NMSHandler nmsHandler() {
        return AquaticGUIs.getInstance().getNmsHandler();
    }

}
