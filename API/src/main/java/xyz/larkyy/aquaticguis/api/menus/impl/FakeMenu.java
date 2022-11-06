package xyz.larkyy.aquaticguis.api.menus.impl;

import org.bukkit.entity.Player;
import xyz.larkyy.aquaticguis.api.NMSHandler;
import xyz.larkyy.aquaticguis.api.menus.AbstractMenu;
import xyz.larkyy.aquaticguis.api.sessions.AbstractSession;
import xyz.larkyy.aquaticguis.api.sessions.impl.FakeMenuSession;

public class FakeMenu extends AbstractMenu {

    private final String id;
    public FakeMenu(String id, String title, NMSHandler nmsHandler) {
        super(title, nmsHandler);
        this.id = id;
    }

    @Override
    public AbstractSession open(Player player) {
        var ms = new FakeMenuSession(player,this,player.getOpenInventory().getTopInventory().getSize());
        getNmsHandler().getOpenedMenus().addMenu(ms);
        return ms;
    }

    @Override
    public Menu.Type getType() {
        return Menu.Type.CHEST;
    }

    public String getId() {
        return id;
    }
}
