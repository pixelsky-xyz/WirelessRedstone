package xyz.pixelsky.wirelessredstone.Commands.Device;

import xyz.pixelsky.wirelessredstone.Messages.M_Utility;
import xyz.pixelsky.wirelessredstone.Redstone.DeviceType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static xyz.pixelsky.wirelessredstone.Commands.Device.CD_Utility.getDeviceTypeString;
import static xyz.pixelsky.wirelessredstone.Commands.Device.CD_Utility.getPlayerString;

//REWORKED
public class CD_Push {
    public static boolean pushResult(Player player, Player target, DeviceType deviceType, int amount, boolean allPlayers, boolean bothDevices){
        String playerString = getPlayerString(target, allPlayers);
        String deviceString = getDeviceTypeString(deviceType, amount, bothDevices);
        if(player != null)
            M_Utility.sendMessage(player, M_Utility.getMessage("give_device", M_Utility.placeHolder("${playername}", playerString, "${amount}", amount, "${devicetype}", deviceString)));
        if(allPlayers)
            for(Player p : Bukkit.getOnlinePlayers())
                M_Utility.sendMessage(p, M_Utility.getMessage("receive_device", M_Utility.placeHolder("${amount}", amount, "${devicetype}", deviceString)));
        else
            target.sendMessage(M_Utility.getMessage("receive_device", M_Utility.placeHolder("${amount}", amount, "${devicetype}", deviceString)));
        return true;
    }

    public static boolean pushInvalidArguments(Player player, String[] commands){
        String s = "";
        for(String str : commands)
            s = s + "\n" + str;
        M_Utility.sendMessage(player, M_Utility.getMessage("invalid_arguments", M_Utility.placeHolder("${commandlist}", s)));
        return true;
    }

    public static void pushInvalidPlayer(Player player, String[] args){
        M_Utility.sendMessage(player, M_Utility.getMessage("invalid_playername", M_Utility.placeHolder("${playername}", args[1])));
    }

    public static DeviceType pushInvalidDeviceType(Player player, String[] args){
        M_Utility.sendMessage(player, M_Utility.getMessage("invalid_devicetype", M_Utility.placeHolder("${devicetype1}", DeviceType.RedstoneSender, "${devicetype2}", DeviceType.RedstoneReceiver, "${devicetype3}", args[2])));
        return null;
    }

    public static boolean pushInvalidTargetBlock(Player player){
        M_Utility.sendMessage(player, M_Utility.getMessage("invalid_targetblock"));
        return false;
    }

    public static void pushInvalidAmount(Player player, String[] args){
        M_Utility.sendMessage(player, M_Utility.getMessage("invalid_amount", M_Utility.placeHolder("${amount}", args[2])));
    }

    public static boolean pushInvalidPermission(Player player){
        M_Utility.sendMessage(player, M_Utility.getMessage("invalid_permission"));
        return true;
    }

    public static boolean pushLinkCancel(Player player){
        M_Utility.sendMessage(player, M_Utility.getMessage("link_cancel"));
        return true;
    }

    public static boolean pushNoLinkCancel(Player player){
        M_Utility.sendMessage(player, M_Utility.getMessage("link_none"));
        return true;
    }

    public static boolean pushLinkBreakAll(Player player, int i){
        M_Utility.sendMessage(player, M_Utility.getMessage("link_break_all", M_Utility.placeHolder("${amount}", i)));
        return true;
    }

    public static boolean pushLinkBreakFirst(Player player){
        M_Utility.sendMessage(player, M_Utility.getMessage("link_break_first"));
        return true;
    }

    public static boolean pushLinkBreakLast(Player player){
        M_Utility.sendMessage(player, M_Utility.getMessage("link_break_last"));
        return true;
    }
}
