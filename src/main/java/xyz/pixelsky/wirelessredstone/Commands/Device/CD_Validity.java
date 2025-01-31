package xyz.pixelsky.wirelessredstone.Commands.Device;

import xyz.pixelsky.wirelessredstone.Redstone.DeviceType;
import xyz.pixelsky.wirelessredstone.Redstone.R_Device;
import xyz.pixelsky.wirelessredstone.Utils.U_Device;
import xyz.pixelsky.wirelessredstone.Utils.U_Permissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

//REWORKED
public class CD_Validity {
    public static boolean hasValidArguments(Player player, String[] args, int min, int max, String[] commands){
        if(args.length < min || args.length > max)
            return (!CD_Push.pushInvalidArguments(player, commands));
        return true;
    }

    public static boolean hasValidArguments(Player player, String[] args, int range, String[] commands){
        if(args.length != range)
            return (!CD_Push.pushInvalidArguments(player, commands));
        return true;
    }

    public static boolean hasValidPermission(Player player){
        if(U_Permissions.isEnabled())
            if (!U_Permissions.check(player, U_Permissions.Permissions.WIRELESSREDSTONE_COMMANDS_DEVICE_GIVE)) {
                CD_Push.pushInvalidPermission(player);
                return false;
            }
        return true;
    }

    public static DeviceType hasValidDevice(Player player, String[] args) {
        DeviceType deviceType;
        if(isValidDeviceByString(DeviceType.RedstoneSender, args[2]))
            deviceType = DeviceType.RedstoneSender;
        else if(isValidDeviceByString(DeviceType.RedstoneReceiver, args[2]))
            deviceType = DeviceType.RedstoneReceiver;
        else
            return CD_Push.pushInvalidDeviceType(player, args);
        return deviceType;
    }

    public static Player isValidTarget(Player player, String[] args){
        Player target = Bukkit.getPlayer(args[1]);
        if(target == null)
            CD_Push.pushInvalidPlayer(player, args);
        return target;
    }

    public static boolean isValidTargetBlock(Player player, R_Device device, String[] args){
        if(args == null)
            return false;
        if(args.length == 1 && device == null)
            return CD_Push.pushInvalidTargetBlock(player);
        if(args.length == 2 && args[1].equalsIgnoreCase("cancel"))
            return true;
        else if(args.length == 2 && device == null)
            return CD_Push.pushInvalidTargetBlock(player);
        return true;
    }

    public static int isValidAmount(Player player, String[] args){
        try {
            if(args.length != 4)
                return 1;
            int i = Integer.parseInt(args[3]);
            if(i > 0)
                return i;
        }catch (Exception ignored) { }
        CD_Push.pushInvalidAmount(player, args);
        return 0;
    }

    public static boolean isValidDeviceByString(DeviceType deviceType, String name){
        if((deviceType.equals(DeviceType.RedstoneSender) && name.equalsIgnoreCase(U_Device.getDeviceName(deviceType).replace(" ", ""))) || (deviceType.equals(DeviceType.RedstoneReceiver) && name.equalsIgnoreCase(U_Device.getDeviceName(deviceType).replace(" ", ""))))
            return true;
        boolean b = name.equalsIgnoreCase(deviceType.toString().toLowerCase());
        return deviceType.equals(DeviceType.RedstoneSender) && (name.equalsIgnoreCase("sender") || b) || (deviceType.equals(DeviceType.RedstoneReceiver) && (name.equalsIgnoreCase("receiver") || b));
    }
}
