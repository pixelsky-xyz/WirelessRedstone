package xyz.pixelsky.wirelessredstone.Commands.Device;

import xyz.pixelsky.wirelessredstone.Config.C_Value;
import xyz.pixelsky.wirelessredstone.Events.E_DeviceClick;
import xyz.pixelsky.wirelessredstone.Messages.M_Utility;
import xyz.pixelsky.wirelessredstone.Redstone.*;
import xyz.pixelsky.wirelessredstone.Utils.U_Permissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.pixelsky.wirelessredstone.Redstone.DeviceType;
import xyz.pixelsky.wirelessredstone.Redstone.R_Device;
import xyz.pixelsky.wirelessredstone.Redstone.R_Link;
import xyz.pixelsky.wirelessredstone.Redstone.R_Links;


import static xyz.pixelsky.wirelessredstone.Commands.Device.CD_Utility.*;
import static xyz.pixelsky.wirelessredstone.Commands.Device.CD_Validity.*;

//REWORKED
public class CD_Handle {
    public static boolean handleInfo(Player player, String[] args){
        if(U_Permissions.isEnabled())
            if(!U_Permissions.check(player, U_Permissions.Permissions.WIRELESSREDSTONE_COMMANDS_DEVICE_INFO))
                return CD_Push.pushInvalidPermission(player);

        R_Device device = getDeviceByTarget(player);
        if(!isValidTargetBlock(player, device, args))
            return true;

        if(!hasValidArguments(player, args, 1, new String[] { "/device info" }))
            return true;

        String str = "\n";
        Player owner = Bukkit.getPlayer(device.getPlayerId());
        if(owner != null && owner.isOnline())
            str += "Owner: " + owner.getDisplayName();
        else
            str += "Owner: " + device.getPlayerId();
        str += "\nLinks: " + device.getLinkCount();
        str += "\nPower: " + device.getSignalPower();
        M_Utility.sendMessage(player, M_Utility.getMessage("device_info", M_Utility.placeHolder("${devicetype}", device.getDeviceType(), "${info}", str)));
        return true;
    }

    public static boolean handleGive(Player player, String[] args){
        if(U_Permissions.isEnabled())
            if(!U_Permissions.check(player, U_Permissions.Permissions.WIRELESSREDSTONE_COMMANDS_DEVICE_GIVE))
                return CD_Push.pushInvalidPermission(player);

        if(!hasValidArguments(player, args, 3, 4, new String[] { "/device give <player> <type> <amount>" }))
            return true;

        Player target = null;
        DeviceType deviceType = null;
        ItemStack[] itemStack;
        boolean bothDevices = false;
        boolean allPlayers = false;
        int amount;

        if(args[1].equals("*") || args[1].equals("all"))
            allPlayers = true;
        else {
            target = isValidTarget(player, args);
            if (target == null)
                return true;
        }

        if(args[2].equals("*"))
            bothDevices = true;
        else {
            deviceType = hasValidDevice(player, args);
            if(deviceType == null)
                return true;
        }

        if(!hasValidPermission(player))
            return true;

        amount = isValidAmount(player, args);
        if(amount <= 0)
            return true;

        itemStack = getItemStacks(deviceType, bothDevices);
        if(allPlayers)
            for(Player p : Bukkit.getOnlinePlayers()){
                giveDeviceItemStack(p, amount, itemStack);
            }
        else
            giveDeviceItemStack(target, amount, itemStack);
        CD_Push.pushResult(player, target, deviceType, amount, allPlayers, bothDevices);

        return true;
    }

    public static boolean handleLink(Player player, String[] args){
        R_Device device = getDeviceByTarget(player);
        if(!isValidTargetBlock(player, device, args))
            return true;
        if(!hasValidArguments(player, args, 1, 2, new String[] {
                "- /device link",
                "- /device link cancel",
                "- /device link breakall",
                "- /device link breakfirst",
                "- /device link breaklast" }))
            return true;

        if(args.length == 1) {
            Bukkit.getServer().getPluginManager().callEvent(new E_DeviceClick(device, player, null));
            return true;
        }

        if(!isLinkedWithDevice(player, device, args))
            return true;

        String arg = args[1].toLowerCase();
        if(!C_Value.allowDestroyThirdLinks() && !arg.equalsIgnoreCase("cancel")) {
            if (!device.getPlayerId().equals(player.getUniqueId())) {
                if (U_Permissions.isEnabled()) {
                    if (!U_Permissions.check(player, U_Permissions.Permissions.WIRELESSREDSTONE_LINK_DESTROYTHIRDLINKS)) {
                        M_Utility.sendMessage(player, M_Utility.getMessage("link_no_permission_destroy_third_links"));
                        return true;
                    }
                } else {
                    M_Utility.sendMessage(player, M_Utility.getMessage("link_destroy_third_links"));
                    return true;
                }
            }
        }

        switch(arg){
            case "cancel": {
                if(U_Permissions.isEnabled())
                    if(!U_Permissions.check(player, U_Permissions.Permissions.WIRELESSREDSTONE_COMMANDS_DEVICE_LINK_CANCEL))
                        return CD_Push.pushInvalidPermission(player);

                R_Link link = null;
                for(R_Link l : R_Links.getList().values())
                    if(!l.isLinked()) {
                        if(player != null)
                            if (l.getPlayerId().equals(player.getUniqueId()))
                                link = l;
                    }

                if(link != null){
                    R_Links.remove(link);
                    return CD_Push.pushLinkCancel(player);
                }

                if(player != null)
                    return CD_Push.pushNoLinkCancel(player);
            }
            case "breakall": {
                if(U_Permissions.isEnabled())
                    if(!U_Permissions.check(player, U_Permissions.Permissions.WIRELESSREDSTONE_COMMANDS_DEVICE_LINK_BREAKALL))
                        return CD_Push.pushInvalidPermission(player);

                int i = device.getLinkCount();
                for (R_Link link : device.getLinks())
                    if (link != null)
                        link.destroy();
                return CD_Push.pushLinkBreakAll(player, i);
            }
            case "breakfirst": {
                if(U_Permissions.isEnabled())
                    if(!U_Permissions.check(player, U_Permissions.Permissions.WIRELESSREDSTONE_COMMANDS_DEVICE_LINK_BREAKFIRST))
                        return CD_Push.pushInvalidPermission(player);

                R_Link link = device.getLinks().get(0);
                if (link != null)
                    link.destroy();
                return CD_Push.pushLinkBreakFirst(player);
            }

            case "breaklast": {
                if(U_Permissions.isEnabled())
                    if(!U_Permissions.check(player, U_Permissions.Permissions.WIRELESSREDSTONE_COMMANDS_DEVICE_LINK_BREAKLAST))
                        return CD_Push.pushInvalidPermission(player);

                R_Link link = device.getLinks().get((device.getLinks().size() - 1));
                if(link != null)
                    link.destroy();
                return CD_Push.pushLinkBreakLast(player);
            }
            default:{
                if(U_Permissions.isEnabled())
                    if(!U_Permissions.check(player, U_Permissions.Permissions.WIRELESSREDSTONE_DEVICE_))
                        return CD_Push.pushInvalidPermission(player);

                return CD_Push.pushInvalidArguments(player, new String[] {
                        "- /device link cancel",
                        "- /device link breakall",
                        "- /device link breakfirst",
                        "- /device link breaklast" });
            }
        }
    }

    public static boolean handleDelete(Player player, String[] args){
        if(U_Permissions.isEnabled())
            if(!U_Permissions.check(player, U_Permissions.Permissions.WIRELESSREDSTONE_COMMANDS_DEVICE_DELETEALL))
                return CD_Push.pushInvalidPermission(player);

        if(!hasValidArguments(player, args, 1, new String[] { "/device deleteall" }))
            return true;

        M_Utility.sendMessage(player, "I'm sorry, this command is not reliable enough to use. Please keep your eyes open for a future update.");
        return true;
    }
}
