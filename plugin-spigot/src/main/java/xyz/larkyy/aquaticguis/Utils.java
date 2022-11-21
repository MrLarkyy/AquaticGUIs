package xyz.larkyy.aquaticguis;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import xyz.larkyy.colorutils.Colors;

public class Utils {

    public static String format(String input, Player player) {
        if (player != null) {
            var plugin = AquaticGUIs.getInstance().getServer().getPluginManager().getPlugin("PlaceholderAPI");
            if (plugin != null && plugin.isEnabled()) {
                input = PlaceholderAPI.setPlaceholders(player, input);
            }
        }
        return Colors.format(input);
    }

}
