package xyz.larkyy.aquaticguis.menu;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.aquaticguis.AquaticGUIs;
import xyz.larkyy.aquaticguis.api.MenuItem;
import xyz.larkyy.aquaticguis.clickaction.ClickActions;

import java.util.Arrays;
import java.util.List;

public class AquaticMenuItem extends MenuItem {

    private final int priority;
    public AquaticMenuItem(ItemStack is, List<Integer> slots, int priority, ClickActions clickActions) {
        super(is, Arrays.asList(
                e -> {
                    if (clickActions == null) {
                        return;
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            clickActions.run(e.getActionType(), e.getPlayer());
                        }
                    }.runTask(AquaticGUIs.getInstance());
                }
        ), slots);
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
