package xyz.larkyy.aquaticguis.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.aquaticguis.AquaticGUIs;
import xyz.larkyy.aquaticguis.api.MenuItem;
import xyz.larkyy.aquaticguis.clickaction.ClickActions;
import xyz.larkyy.aquaticguis.condition.ConditionList;

import java.util.Arrays;
import java.util.List;

public class ConditionalMenuItem extends MenuItem {

    private final ConditionList conditionList;

    public ConditionalMenuItem(ItemStack is, List<Integer> slots, ClickActions clickActions, ConditionList conditionList) {
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
        this.conditionList = conditionList;
    }

    public boolean isMet(Player player) {
        return conditionList.areMet(player);
    }

}
