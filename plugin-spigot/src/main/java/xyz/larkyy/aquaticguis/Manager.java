package xyz.larkyy.aquaticguis;

import xyz.larkyy.aquaticguis.action.ActionList;
import xyz.larkyy.aquaticguis.config.Config;

public class Manager {

    private final Config config = new Config(AquaticGUIs.getInstance(),"config.yml");

    private final ActionList joinActions;
    private final ActionList firstJoinActions;

    public Manager() {
        AquaticGUIs.getInstance().getDataFolder().mkdirs();
        config.load();
        joinActions = config.loadActionList("join-actions");
        firstJoinActions = config.loadActionList("first-join-actions");
    }

    public ActionList getJoinActions() {
        return joinActions;
    }

    public ActionList getFirstJoinActions() {
        return firstJoinActions;
    }
}
