package xyz.larkyy.aquaticguis.api;

import java.util.ArrayList;
import java.util.List;

public class PacketFilter {

    private final List<Object> packets = new ArrayList<>();

    public void addPacket(Object packet) {
        packets.add(packet);
    }

    public boolean containsPacket(Object packet) {
        return packets.contains(packet);
    }

    public boolean removePacket(Object packet) {
        return packets.remove(packet);
    }

}
