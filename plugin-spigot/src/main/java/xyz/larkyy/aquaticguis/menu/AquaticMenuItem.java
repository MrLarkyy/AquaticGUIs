package xyz.larkyy.aquaticguis.menu;

import org.bukkit.inventory.ItemStack;
import xyz.larkyy.aquaticguis.api.MenuItem;
import xyz.larkyy.aquaticguis.clickaction.ClickActions;

import java.util.Arrays;
import java.util.List;

public class AquaticMenuItem extends MenuItem {

    private final int priority;
    private final ClickActions clickActions;
    public AquaticMenuItem(ItemStack is, List<Integer> slots, int priority, ClickActions clickActions) {
        super(is, Arrays.asList(
                e -> {
                    if (clickActions == null) {
                        return;
                    }
                    clickActions.run(e.getActionType(), e.getPlayer());
                }
        ), slots);
        this.priority = priority;
        this.clickActions = clickActions;
    }

    public int getPriority() {
        return priority;
    }
}
