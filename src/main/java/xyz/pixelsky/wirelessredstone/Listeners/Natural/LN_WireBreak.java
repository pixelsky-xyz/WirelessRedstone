package xyz.pixelsky.wirelessredstone.Listeners.Natural;

import xyz.pixelsky.wirelessredstone.Events.E_DevicePower;
import xyz.pixelsky.wirelessredstone.Redstone.R_Device;
import xyz.pixelsky.wirelessredstone.Utils.U_Environment;
import xyz.pixelsky.wirelessredstone.WirelessRedstone;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class LN_WireBreak implements Listener {
    public LN_WireBreak(WirelessRedstone plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEvent(BlockBreakEvent e) {
        if(e.getBlock().getType().equals(Material.REDSTONE_WIRE))
            Roulette(e.getBlock());
    }

    private void Roulette(Block powerBlock){
        for(R_Device device : U_Environment.getSurroundingDevices(powerBlock))
        {
            if(device == null)
                continue;

            if(device.isReceiver())
                continue;

            if (U_Environment.isFacing(device.getBlock(), powerBlock))
                Bukkit.getServer().getPluginManager().callEvent(new E_DevicePower(device));
        }
    }
}
