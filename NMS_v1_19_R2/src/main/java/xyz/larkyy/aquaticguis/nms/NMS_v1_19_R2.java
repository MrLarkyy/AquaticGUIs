package xyz.larkyy.aquaticguis.nms;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.aquaticguis.api.*;

import java.util.List;

public final class NMS_v1_19_R2 implements NMSHandler {

    private final PacketFilter filter = new PacketFilter();
    private final JavaPlugin plugin;
    public NMS_v1_19_R2(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void setSlotPacket(Player player, int inventoryId, int slot, ItemStack item) {
        var serverPlayer = ((CraftPlayer)player).getHandle();
        var pkt = new ClientboundContainerSetSlotPacket(
                inventoryId,
                serverPlayer.containerMenu.getStateId(),
                slot,
                CraftItemStack.asNMSCopy(item)
        );
        serverPlayer.connection.send(pkt);
    }

    @Override
    public void emptyPlayerInventory(Player player, boolean filter) {
        var serverPlayer = ((CraftPlayer)player).getHandle();
        NonNullList<net.minecraft.world.item.ItemStack> list = NonNullList.withSize(45,CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)));
        var pkt = new ClientboundContainerSetContentPacket(0,serverPlayer.containerMenu.getStateId(),list,CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)));
        if (filter) {
            this.filter.addPacket(pkt);
        }
        serverPlayer.connection.send(pkt);
        setSlotPacket(player,0,45,new ItemStack(Material.AIR));

    }

    @Override
    public void setContentPacket(Player player, int inventoryId, List<ItemStack> itemStacks) {

    }
    @Override
    public void openScreen(Player player, int inventoryId, String inventoryType, String title) {
        var pkt = new ClientboundOpenScreenPacket(inventoryId,translateInventoryType(inventoryType), Component.translatable(title));
        ((CraftPlayer)player).getHandle().connection.send(pkt);
    }

    @Override
    public int getInventoryId(Player player) {
        var serverPlayer = ((CraftPlayer)player).getHandle();
        return serverPlayer.containerMenu.containerId;
    }

    @Override
    public void injectPlayer(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        var channel = craftPlayer.getHandle().connection.connection.channel;

        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {

            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                Inventory inventory = player.getOpenInventory().getTopInventory();

                if (packet instanceof ServerboundContainerClickPacket p) {

                    if (inventory.getHolder() instanceof Menu menu) {
                        var menuSession = menu.getSession(player);

                        // Remove the item from player's cursor
                        setSlotPacket(player,-1,-1,new ItemStack(Material.AIR));

                        InventoryActionEvent.ActionType actionType = translateClick(p.getButtonNum(),p.getClickType());

                        // Check if player swapped the item and if so, set the offhand item to AIR
                        if (actionType == InventoryActionEvent.ActionType.SWAP) {
                            setSlotPacket(player,0,45,new ItemStack(Material.AIR));
                        }

                        var item = menuSession.getItem(p.getSlotNum());
                        if (item != null) {
                            var event = new InventoryActionEvent(player,p.getSlotNum(),item,actionType);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    Bukkit.getServer().getPluginManager().callEvent(event);
                                    item.activate(event);
                                }
                            }.runTask(plugin);
                        }

                        p.getChangedSlots().keySet().forEach(i -> {
                            var menuItem = menuSession.getItem(i);
                            ItemStack is;
                            if (menuItem == null) {
                                is = new ItemStack(Material.AIR);
                            } else {
                                is = menuItem.getItemStack();
                            }
                            setSlotPacket(player,p.getContainerId(),i,is);

                        });
                        return;
                    }
                }
                if (packet instanceof ServerboundContainerClosePacket p) {
                    super.channelRead(channelHandlerContext, packet);
                    if (p.getContainerId() == getInventoryId(player)) {
                        var pkt = new ClientboundContainerClosePacket(p.getContainerId());
                        ((CraftPlayer)player).getHandle().connection.send(pkt);
                    }
                    return;
                }

                super.channelRead(channelHandlerContext, packet);
            }

            @Override
            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {

                if (filter.removePacket(packet)) {
                    super.write(channelHandlerContext, packet, channelPromise);
                    return;
                }

                if (packet instanceof ClientboundContainerSetContentPacket p) {
                    Inventory inventory = player.getOpenInventory().getTopInventory();
                    if (inventory.getHolder() instanceof Menu) {
                        return;
                    }
                }

                super.write(channelHandlerContext, packet, channelPromise);
            }
        };

        if (channel != null) {
            channel.eventLoop().execute(() -> {
                channel.pipeline().addBefore("packet_handler", "inventory_packet_reader", channelDuplexHandler);
            });
        }
    }

    @Override
    public void ejectPlayer(Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().connection.connection.channel;
        if (channel != null) {
            try {
                channel.pipeline().remove("inventory_packet_reader");
            } catch (Exception ignored) {

            }
        }
    }

    @Override
    public JavaPlugin getPlugin() {
        return plugin;
    }

    private MenuType translateInventoryType(String type) {
        switch (type.toLowerCase()) {
            case "anvil" -> {
                return MenuType.ANVIL;
            }
            case "beacon" -> {
                return MenuType.BEACON;
            }
            case "blast_furnace" -> {
                return MenuType.BLAST_FURNACE;
            }
            case "brewing_stand" -> {
                return MenuType.BREWING_STAND;
            }
            case "cartography_table" -> {
                return MenuType.CARTOGRAPHY_TABLE;
            }
            case "crafting" -> {
                return MenuType.CRAFTING;
            }
            case "enchantment" -> {
                return MenuType.ENCHANTMENT;
            }
            case "furnace" -> {
                return MenuType.FURNACE;
            }
            case "generic_3x3" -> {
                return MenuType.GENERIC_3x3;
            }
            case "generic_9x2" -> {
                return MenuType.GENERIC_9x2;
            }
            case "generic_9x3" -> {
                return MenuType.GENERIC_9x3;
            }
            case "generic_9x4" -> {
                return MenuType.GENERIC_9x4;
            }
            case "generic_9x5" -> {
                return MenuType.GENERIC_9x5;
            }
            case "generic_9x6" -> {
                return MenuType.GENERIC_9x6;
            }
            case "grindstone" -> {
                return MenuType.GRINDSTONE;
            }
            case "hopper" -> {
                return MenuType.HOPPER;
            }
            case "lectern" -> {
                return MenuType.LECTERN;
            }
            case "loom" -> {
                return MenuType.LOOM;
            }
            case "merchant" -> {
                return MenuType.MERCHANT;
            }
            case "shulker_box" -> {
                return MenuType.SHULKER_BOX;
            }
            case "smithing" -> {
                return MenuType.SMITHING;
            }
            case "smoker" -> {
                return MenuType.SMOKER;
            }
            case "stonecutter" -> {
                return MenuType.STONECUTTER;
            }
            default -> {
                return MenuType.GENERIC_9x1;
            }
        }
    }

    private InventoryActionEvent.ActionType translateClick(int button, ClickType clickType) {
        InventoryActionEvent.ActionType actionType = InventoryActionEvent.ActionType.LEFT;
        switch (clickType) {
            case PICKUP -> {
                if (button == 0) {
                    actionType = InventoryActionEvent.ActionType.LEFT;
                } else {
                    actionType = InventoryActionEvent.ActionType.RIGHT;
                }
            }
            case THROW -> {
                actionType = InventoryActionEvent.ActionType.THROW;
            }
            case QUICK_MOVE -> {
                if (button == 0) {
                    actionType = InventoryActionEvent.ActionType.SHIFT_LEFT;
                } else {
                    actionType = InventoryActionEvent.ActionType.SHIFT_RIGHT;
                }
            }
            case SWAP -> {

                switch (button) {
                    case 0 -> {
                        actionType = InventoryActionEvent.ActionType.NUM_1;
                    }
                    case 1 -> {
                        actionType = InventoryActionEvent.ActionType.NUM_2;
                    }
                    case 2 -> {
                        actionType = InventoryActionEvent.ActionType.NUM_3;
                    }
                    case 3 -> {
                        actionType = InventoryActionEvent.ActionType.NUM_4;
                    }
                    case 4 -> {
                        actionType = InventoryActionEvent.ActionType.NUM_5;
                    }
                    case 5 -> {
                        actionType = InventoryActionEvent.ActionType.NUM_6;
                    }
                    case 6 -> {
                        actionType = InventoryActionEvent.ActionType.NUM_7;
                    }
                    case 7 -> {
                        actionType = InventoryActionEvent.ActionType.NUM_8;
                    }
                    case 8 -> {
                        actionType = InventoryActionEvent.ActionType.NUM_9;
                    }
                    case 40 -> {
                        actionType = InventoryActionEvent.ActionType.SWAP;
                    }
                }
            }
            default -> actionType = InventoryActionEvent.ActionType.LEFT;
        }
        return actionType;
    }
}

