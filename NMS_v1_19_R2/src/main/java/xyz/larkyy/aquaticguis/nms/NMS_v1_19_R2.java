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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.aquaticguis.api.*;
import xyz.larkyy.aquaticguis.api.menus.impl.FakeMenu;
import xyz.larkyy.aquaticguis.api.sessions.impl.FakeMenuSession;
import xyz.larkyy.aquaticguis.api.sessions.impl.MenuSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public final class NMS_v1_19_R2 implements NMSHandler {

    private final PacketFilter filter = new PacketFilter();
    private final OpenedMenus openedMenus = new OpenedMenus();
    private final Map<String, FakeMenu> fakeMenus = new HashMap<>();
    private final JavaPlugin plugin;
    private final Pattern pattern = Pattern.compile("<aquaticguis:(.*?)>");
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
    public void setContentPacket(Player player, int inventoryId, List<ItemStack> itemStacks, boolean filter) {
        var serverPlayer = ((CraftPlayer)player).getHandle();
        NonNullList<net.minecraft.world.item.ItemStack> list = NonNullList.create();
        itemStacks.forEach(is -> {
            list.add(CraftItemStack.asNMSCopy(is));
        });
        var pkt = new ClientboundContainerSetContentPacket(inventoryId,serverPlayer.containerMenu.getStateId(),list,CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)));
        if (filter) {
            this.filter.addPacket(pkt);
        }
        serverPlayer.connection.send(pkt);
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

        ChannelDuplexHandler cdh = new ChannelDuplexHandler() {

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
                if (packet instanceof ServerboundContainerClickPacket p) {
                    var ms = openedMenus.getMenu(p.getContainerId());
                    if (ms != null) {

                        if (ms instanceof MenuSession || ms instanceof FakeMenuSession) {
                            // Remove the item from player's cursor
                            setSlotPacket(player, -1, -1, new ItemStack(Material.AIR));

                            MenuActionEvent.ActionType actionType = translateClick(p.getButtonNum(), p.getClickType());

                            // Check if player swapped the item and if so, set the offhand item to AIR
                            if (actionType == MenuActionEvent.ActionType.SWAP) {
                                setSlotPacket(player, 0, 45, new ItemStack(Material.AIR));
                            }

                            var item = ms.getItem(p.getSlotNum());
                            if (item != null) {
                                var event = new MenuActionEvent(player, p.getSlotNum(), item, actionType);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        Bukkit.getServer().getPluginManager().callEvent(event);
                                        item.activate(event);
                                    }
                                }.runTask(plugin);
                            }

                            p.getChangedSlots().keySet().forEach(i -> {
                                var menuItem = ms.getItem(i);
                                ItemStack is;
                                if (menuItem == null) {
                                    is = new ItemStack(Material.AIR);
                                } else {
                                    is = menuItem.getItemStack();
                                }
                                setSlotPacket(player, p.getContainerId(), i, is);

                            });
                            return;
                        }
                    }
                }

                if (packet instanceof ServerboundContainerClosePacket p) {
                    super.channelRead(ctx, packet);
                    var session = getOpenedMenus().getMenu(p.getContainerId());
                    if (session != null) {
                        var event = new MenuCloseEvent(player,session);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Bukkit.getPluginManager().callEvent(event);
                            }
                        }.runTask(plugin);
                        getOpenedMenus().removeMenu(p.getContainerId());
                    }
                    if (p.getContainerId() == getInventoryId(player)) {
                        var pkt = new ClientboundContainerClosePacket(p.getContainerId());
                        ((CraftPlayer)player).getHandle().connection.send(pkt);
                    }
                    return;
                }
                super.channelRead(ctx, packet);
            }

            @Override
            public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {

                if (filter.removePacket(packet)) {
                    super.write(ctx, packet, promise);
                    return;
                }

                if (packet instanceof ClientboundOpenScreenPacket p) {
                    var matcher = pattern.matcher(p.getTitle().getString());
                    if (matcher.find()) {
                        String id = matcher.group(1);
                        var fakeMenu = fakeMenus.get(id);
                        if (fakeMenu != null) {
                            fakeMenu.open(player);
                            return;
                        }
                    }
                }

                if (packet instanceof ClientboundContainerSetContentPacket p) {
                    var ms = openedMenus.getMenu(p.getContainerId());
                    if (ms != null) {
                        if (ms instanceof MenuSession) {
                            return;
                        }
                        if (ms instanceof FakeMenuSession) {
                            return;
                        }
                    }
                }

                super.write(ctx, packet, promise);
            }
        };

        if (channel != null) {
            channel.eventLoop().execute(() -> {
                channel.pipeline().addBefore("packet_handler", "inventory_packet_reader", cdh);
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

    @Override
    public OpenedMenus getOpenedMenus() {
        return openedMenus;
    }

    @Override
    public void addFakeMenu(FakeMenu fakeMenu) {
        fakeMenus.put(fakeMenu.getId(),fakeMenu);
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

    private MenuActionEvent.ActionType translateClick(int button, ClickType clickType) {
        MenuActionEvent.ActionType actionType = MenuActionEvent.ActionType.LEFT;
        switch (clickType) {
            case PICKUP -> {
                if (button == 0) {
                    actionType = MenuActionEvent.ActionType.LEFT;
                } else {
                    actionType = MenuActionEvent.ActionType.RIGHT;
                }
            }
            case THROW -> {
                actionType = MenuActionEvent.ActionType.THROW;
            }
            case QUICK_MOVE -> {
                if (button == 0) {
                    actionType = MenuActionEvent.ActionType.SHIFT_LEFT;
                } else {
                    actionType = MenuActionEvent.ActionType.SHIFT_RIGHT;
                }
            }
            case SWAP -> {

                switch (button) {
                    case 0 -> {
                        actionType = MenuActionEvent.ActionType.NUM_1;
                    }
                    case 1 -> {
                        actionType = MenuActionEvent.ActionType.NUM_2;
                    }
                    case 2 -> {
                        actionType = MenuActionEvent.ActionType.NUM_3;
                    }
                    case 3 -> {
                        actionType = MenuActionEvent.ActionType.NUM_4;
                    }
                    case 4 -> {
                        actionType = MenuActionEvent.ActionType.NUM_5;
                    }
                    case 5 -> {
                        actionType = MenuActionEvent.ActionType.NUM_6;
                    }
                    case 6 -> {
                        actionType = MenuActionEvent.ActionType.NUM_7;
                    }
                    case 7 -> {
                        actionType = MenuActionEvent.ActionType.NUM_8;
                    }
                    case 8 -> {
                        actionType = MenuActionEvent.ActionType.NUM_9;
                    }
                    case 40 -> {
                        actionType = MenuActionEvent.ActionType.SWAP;
                    }
                }
            }
            default -> actionType = MenuActionEvent.ActionType.LEFT;
        }
        return actionType;
    }
}

