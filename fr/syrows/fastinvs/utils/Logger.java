package fr.syrows.fastinvs.utils;

import fr.syrows.fastinvs.FastInvsAPI;

import java.util.logging.Level;

public class Logger {

    public static void log(Level lvl, String msg) {
        if(FastInvsAPI.isDebuggerEnabled()) FastInvsAPI.getPlugin().getLogger().log(lvl, msg);
    }
}
