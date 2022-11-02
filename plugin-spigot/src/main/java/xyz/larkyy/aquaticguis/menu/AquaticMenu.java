package xyz.larkyy.aquaticguis.menu;

import org.bukkit.entity.Player;
import xyz.larkyy.aquaticguis.action.ActionList;
import xyz.larkyy.aquaticguis.api.Menu;
import xyz.larkyy.aquaticguis.api.MenuSession;
import xyz.larkyy.aquaticguis.api.NMSHandler;
import xyz.larkyy.aquaticguis.condition.ConditionList;
import xyz.larkyy.aquaticguis.menu.title.MenuTitle;

import java.util.function.Consumer;

public class AquaticMenu extends Menu {

    private final ConditionList openConditions;
    private final ActionList openActions;
    private final ActionList closeActions;
    private final String id;
    private final MenuTitle title;

    public AquaticMenu(String id,MenuTitle title, int size, Type type, NMSHandler nmsHandler,
                       ConditionList openConditions, ActionList openActions,
                       ActionList closeActions) {
        super(title.getFrames().get(0), size, type, nmsHandler);
        this.title = title;
        this.openConditions = openConditions;
        this.closeActions = closeActions;
        this.openActions = openActions;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public MenuSession open(Player player, Consumer<MenuSession> factory) {
        if (!openConditions.areMet(player)) {
            return null;
        }
        openActions.run(player);
        MenuSession ms = super.open(player);
        title.run(ms);
        return ms;
    }
    @Override
    public MenuSession open(Player player) {
        if (!openConditions.areMet(player)) {
            return null;
        }
        openActions.run(player);
        MenuSession ms = super.open(player);
        title.run(ms);
        return ms;
    }

    public ActionList getCloseActions() {
        return closeActions;
    }
}
