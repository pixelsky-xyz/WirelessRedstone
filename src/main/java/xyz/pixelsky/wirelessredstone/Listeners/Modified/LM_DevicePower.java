package xyz.pixelsky.wirelessredstone.Listeners.Modified;

import xyz.pixelsky.wirelessredstone.Events.E_DevicePower;
import xyz.pixelsky.wirelessredstone.Redstone.R_Device;
import xyz.pixelsky.wirelessredstone.WirelessRedstone;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LM_DevicePower implements Listener {
    public LM_DevicePower(WirelessRedstone plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEvent(E_DevicePower e) {
        R_Device device = e.getDevice();
        device.updateSignalPower();
    }
}
