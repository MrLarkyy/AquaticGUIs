package xyz.larkyy.aquaticguis;

import xyz.larkyy.aquaticguis.api.Menu;

public class Utils {

    public static String translateMenuType(Menu.Type type, int size) {
        switch (type) {
            case DISPENSER -> {
                return "generic_3x3";
            }
            default -> {
                switch (size) {
                    case 9 -> {
                        return "generic_9x1";
                    }
                    case 18 -> {
                        return "generic_9x2";
                    }
                    case 27 -> {
                        return "generic_9x3";
                    }
                    case 36 -> {
                        return "generic_9x4";
                    }
                    case 45 -> {
                        return "generic_9x5";
                    }
                    case 54 -> {
                        return "generic_9x6";
                    }
                }
            }
        }
        return "generic_9x1";
    }

}
