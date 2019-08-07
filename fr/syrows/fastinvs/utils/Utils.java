package fr.syrows.fastinvs.utils;

import org.bukkit.ChatColor;

public class Utils {

    public static String parseColors(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
