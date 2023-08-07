package xyz.pixelsky.wirelessredstone.Listeners.Modified;

import xyz.pixelsky.wirelessredstone.Events.E_DeviceMove;
import xyz.pixelsky.wirelessredstone.Redstone.R_Device;
import xyz.pixelsky.wirelessredstone.WirelessRedstone;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class LM_DeviceMove implements Listener {
    public LM_DeviceMove(WirelessRedstone plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEvent(E_DeviceMove e) {
        R_Device device = e.getDevice();

        device.setLocation(e.getNewLocation());
        new BukkitRunnable() {
            @Override
            public void run() {
                if(device.isSender()){
                    device.setSignalPower(device.getBlock().getBlockPower());
                    device.sendSignal();
                }
                else
                    device.emitSignal(device.getSignalPower());
            }

        }.runTaskLater(WirelessRedstone.getPlugin(), 0);
    }
}
