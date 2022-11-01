package xyz.larkyy.aquaticguis.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface NMSHandler {

    void setSlotPacket(Player player, int inventoryId, int slot, ItemStack item);
    void emptyPlayerInventory(Player player, boolean filter);
    void setContentPacket(Player player, int inventoryId, List<ItemStack> itemStacks);
    int getInventoryId(Player player);
    void injectPlayer(Player player);
    void ejectPlayer(Player player);
    void openScreen(Player player, int inventoryId, String inventoryType, String title);

}
