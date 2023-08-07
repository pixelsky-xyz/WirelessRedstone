package xyz.pixelsky.wirelessredstone.Commands.WirelessRedstone;

import xyz.pixelsky.wirelessredstone.Messages.M_Utility;
import xyz.pixelsky.wirelessredstone.Utils.U_Log;
import xyz.pixelsky.wirelessredstone.WirelessRedstone;
import org.bukkit.entity.Player;


public class CW_Push {
    public static boolean pushInvalidArguments(Player player, String[] commands){
        String s = "";
        for(String str : commands)
            s = s + "\n" + str;
        M_Utility.sendMessage(player, M_Utility.getMessage("invalid_arguments", M_Utility.placeHolder("${commandlist}", s)));
        return true;
    }

    public static boolean pushInvalidPermission(Player player){
        M_Utility.sendMessage(player, M_Utility.getMessage("invalid_permission"));
        return true;
    }

    public static boolean pushReloading(Player player){
        M_Utility.sendMessage(player, M_Utility.getMessage("plugin_reload"));
        WirelessRedstone.Log(new U_Log(U_Log.LogType.WARNING, "Reloading plugin..."));
        return true;
    }

    public static boolean pushReloaded(Player player){
        M_Utility.sendMessage(player, M_Utility.getMessage("plugin_reloaded"));
        return true;
    }

    public static boolean pushDisabling(Player player){
        M_Utility.sendMessage(player, M_Utility.getMessage("plugin_disable"));
        WirelessRedstone.Log(new U_Log(U_Log.LogType.WARNING, "Disabling plugin..."));
        return true;
    }
}
