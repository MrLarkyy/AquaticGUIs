package xyz.larkyy.aquaticguis.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.aquaticguis.AquaticGUIs;
import xyz.larkyy.aquaticguis.api.MenuItem;
import xyz.larkyy.aquaticguis.clickaction.ClickActions;
import xyz.larkyy.aquaticguis.condition.ConditionList;
import xyz.larkyy.aquaticguis.menu.session.AquaticMenuSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AquaticMenuItem extends MenuItem implements Updatable {

    private final List<AquaticMenuItem> conditionalMenuItems;
    private final AquaticMenuItem parent;
    private final ConditionList conditionList;
    private final int updateInterval;

    public AquaticMenuItem(ItemStack is, List<Integer> slots, ClickActions clickActions, List<AquaticMenuItem> conditionalMenuItems, ConditionList conditionList, AquaticMenuItem parent, int updateInterval) {
        super(is, new ArrayList<>(), slots);
        this.parent = parent;
        this.conditionList = conditionList;
        this.conditionalMenuItems = conditionalMenuItems;
        this.setActions(Arrays.asList(
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
                })
        );
        this.updateInterval = updateInterval;
    }

    public void addConditionalMenuItem(AquaticMenuItem item) {
        conditionalMenuItems.add(item);
    }

    public boolean isMet(Player player) {
        return conditionList.areMet(player);
    }

    public ItemStack getItemStack(Player player) {
        for (AquaticMenuItem mi : conditionalMenuItems) {
            if (mi.isMet(player)) {
                return mi.getItemStack();
            }
        }
        return super.getItemStack();
    }

    public MenuItem getMenuItem(Player player) {
        if (parent != null) {
            return parent.getMenuItem(player);
        }
        if (conditionalMenuItems == null) {
            return this;
        }
        for (AquaticMenuItem mi : conditionalMenuItems) {
            if (mi.isMet(player)) {
                return mi;
            }
        }
        return this;
    }

    public void place(int inventoryId, Player player) {
        var is = getItemStack(player);
        getSlots().forEach(slot -> {
            AquaticGUIs.getInstance().getNmsHandler().setSlotPacket(player, inventoryId, slot, is);
        });
    }

    @Override
    public void update(AquaticMenuSession session) {
        if (session.updateMenuItemValue(getSlots().get(0))) {
            place(session.getInventoryId(), session.getPlayer());
        }
    }

    @Override
    public int getInterval() {
        return updateInterval;
    }
}
