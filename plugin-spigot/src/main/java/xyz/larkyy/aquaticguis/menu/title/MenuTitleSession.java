package xyz.larkyy.aquaticguis.menu.title;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.aquaticguis.AquaticGUIs;
import xyz.larkyy.aquaticguis.api.MenuSession;
import xyz.larkyy.aquaticguis.api.sessions.AbstractSession;

public class MenuTitleSession extends BukkitRunnable {

    private final MenuTitle title;
    private final AbstractSession menuSession;
    private boolean started = false;

    private int index = 0;

    public MenuTitleSession(MenuTitle title, AbstractSession menuSession) {
        this.title = title;
        this.menuSession = menuSession;
        if (!title.getFrames().isEmpty() && title.getUpdate() > 0) {
            this.runTaskTimer(AquaticGUIs.getInstance(),title.getUpdate(), title.getUpdate());
            started = true;
        }
    }

    @Override
    public void run() {
        index++;
        if (index >= title.getFrames().size()) {
            index = 0;
        }
        updateTitle();
    }

    private void updateTitle() {
        menuSession.setTitle(getActualTitle());
        menuSession.updateTitle();
    }

    public String getActualTitle() {
        String title;
        if (this.title.getFrames().isEmpty()) {
            title = "";
        } else {
            title = this.title.getFrames().get(index);
        }
        return title;
    }

    public void stop() {
        if (started) {
            this.cancel();
        }
    }
}