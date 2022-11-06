package xyz.larkyy.aquaticguis.menu;

import org.bukkit.entity.Player;
import xyz.larkyy.aquaticguis.action.ActionList;
import xyz.larkyy.aquaticguis.api.NMSHandler;
import xyz.larkyy.aquaticguis.api.menus.impl.FakeMenu;
import xyz.larkyy.aquaticguis.api.sessions.AbstractSession;
import xyz.larkyy.aquaticguis.menu.title.MenuTitle;

import java.util.function.Consumer;

public class AquaticFakeMenu extends FakeMenu {

    private final ActionList openActions;
    private final ActionList closeActions;
    private final MenuTitle title;

    public AquaticFakeMenu(String id,MenuTitle title, NMSHandler nmsHandler, ActionList openActions,
                           ActionList closeActions) {
        super(id,title.getFrames().get(0), nmsHandler);
        this.title = title;
        this.closeActions = closeActions;
        this.openActions = openActions;
    }

    @Override
    public AbstractSession open(Player player, Consumer<AbstractSession> factory) {
        openActions.run(player);
        AbstractSession ms = super.open(player);
        title.run(ms);
        return ms;
    }

    @Override
    public AbstractSession open(Player player) {
        openActions.run(player);
        AbstractSession ms = super.open(player);
        title.run(ms);
        return ms;
    }

    public void register() {
        getNmsHandler().addFakeMenu(this);
    }

    public ActionList getCloseActions() {
        return closeActions;
    }
}
