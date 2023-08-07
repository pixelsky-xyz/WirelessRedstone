package xyz.pixelsky.wirelessredstone.Listeners.Natural;

import xyz.pixelsky.wirelessredstone.Config.C_Value;
import xyz.pixelsky.wirelessredstone.Events.E_DevicePlace;
import xyz.pixelsky.wirelessredstone.Messages.M_Utility;
import xyz.pixelsky.wirelessredstone.Redstone.DeviceType;
import xyz.pixelsky.wirelessredstone.Redstone.R_Devices;
import xyz.pixelsky.wirelessredstone.Redstone.R_Device;
import xyz.pixelsky.wirelessredstone.Redstone.R_Items;
import xyz.pixelsky.wirelessredstone.Utils.U_Environment;
import xyz.pixelsky.wirelessredstone.Utils.U_Permissions;
import xyz.pixelsky.wirelessredstone.WirelessRedstone;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

//HALF REWORKED
public class LN_BlockPlace implements Listener {
    public LN_BlockPlace(WirelessRedstone plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private static boolean itemCheck(BlockPlaceEvent e){
        return (R_Items.isRedstoneDevice(e.getItemInHand()));
    }

    private static DeviceType getDeviceFromHandItem(BlockPlaceEvent e){
        if(R_Items.isRedstoneSender(e.getItemInHand()))
            return DeviceType.RedstoneSender;
        return DeviceType.RedstoneReceiver;
    }

    @EventHandler
    public void onEvent(BlockPlaceEvent e) {

        if(e.isCancelled()){
            return;
        }

        Player player = e.getPlayer();
        if(!itemCheck(e)) return;

        DeviceType deviceType = getDeviceFromHandItem(e);

        if(C_Value.getMaxDevicesInServer() > 0)
            if (R_Devices.getList().size() >= C_Value.getMaxDevicesInServer()) {
                M_Utility.sendMessage(player, M_Utility.getMessage("device_limit"));
                e.setCancelled(true);
                return;
            }

        if(U_Permissions.isEnabled())
            if(!U_Permissions.check(player, U_Permissions.Permissions.WIRELESSREDSTONE_DEVICE_PLACE)){
                M_Utility.sendMessage(player, M_Utility.getMessage("device_no_permission_place"));
                e.setCancelled(true);
                return;
            }

        if(C_Value.getMaxDevicesPerPlayer() > 0)
            if(R_Devices.getList(e.getPlayer().getUniqueId()).size() >= C_Value.getMaxDevicesPerPlayer()){
                if(U_Permissions.isEnabled()){
                    if(!U_Permissions.check(player, U_Permissions.Permissions.WIRELESSREDSTONE_DEVICE_NOPLACELIMIT)){
                        M_Utility.sendMessage(player, M_Utility.getMessage("device_no_permission_max_device", M_Utility.placeHolder("${maxdevicesperplayer}", C_Value.getMaxDevicesPerPlayer())));
                        e.setCancelled(true);
                        return;
                    }
                }
                else {
                    M_Utility.sendMessage(player, M_Utility.getMessage("device_max_device", M_Utility.placeHolder("${maxdevicesperplayer}", C_Value.getMaxDevicesPerPlayer())));
                    e.setCancelled(true);
                    return;
                }
            }

        Bukkit.getServer().getPluginManager().callEvent(new E_DevicePlace(deviceType, e.getPlayer(), e.getBlock().getLocation(), e));
    }

    @EventHandler
    public void onEvent2(BlockPlaceEvent e){
        Block block = e.getBlock();
        for(R_Device device : U_Environment.getSurroundingDevices(block))
        {
            if(device == null)
                continue;

            if(device.isReceiver()) {
                //device.getSignalPower();
                device.emitSignal(device.getSignalPower());
            }
        }
    }
}
