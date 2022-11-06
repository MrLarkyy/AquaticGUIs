package xyz.larkyy.aquaticguis.menu.title;

import xyz.larkyy.aquaticguis.api.MenuSession;
import xyz.larkyy.aquaticguis.api.sessions.AbstractSession;

import java.util.List;

public class MenuTitle {

    private final List<String> frames;
    private final int update;

    public MenuTitle(List<String> frames, int update) {
        this.frames= frames;
        this.update = update;
    }

    public void run(AbstractSession ms) {
        var titlesession = new MenuTitleSession(this,ms);
        ms.addData("title-session", titlesession);
    }

    public int getUpdate() {
        return update;
    }

    public List<String> getFrames() {
        return frames;
    }
}
