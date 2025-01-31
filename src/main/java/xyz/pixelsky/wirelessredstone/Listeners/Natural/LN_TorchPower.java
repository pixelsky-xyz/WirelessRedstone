package xyz.pixelsky.wirelessredstone.Listeners.Natural;

import xyz.pixelsky.wirelessredstone.Redstone.R_Device;
import xyz.pixelsky.wirelessredstone.Redstone.R_Devices;
import xyz.pixelsky.wirelessredstone.Versions.V_Manager;
import xyz.pixelsky.wirelessredstone.WirelessRedstone;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import static xyz.pixelsky.wirelessredstone.Utils.U_Environment.getTorchBlock;

public class LN_TorchPower implements Listener {
    public LN_TorchPower(WirelessRedstone plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEvent(BlockPlaceEvent e) {
        if(!e.getBlock().getType().equals(Material.REDSTONE_WALL_TORCH) && !e.getBlock().getType().equals(Material.REDSTONE_TORCH))
            return;
        handle(e.getBlock(), null);
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onEvent(BlockPhysicsEvent e) {
        if(!e.getBlock().getType().equals(Material.REDSTONE_WALL_TORCH) && !e.getBlock().getType().equals(Material.REDSTONE_TORCH))
            return;
        handle(e.getBlock(), e);
    }

    private static void handle(Block torch, BlockPhysicsEvent e){
        Block block = getTorchBlock(torch);
        Location location = block.getLocation();
        R_Device device = R_Devices.get(location);

        if(device == null)
            return;

        if(device.isReceiver()) {
            if(device.getSignalPower() > 0)
            {
                if(e == null || V_Manager.minecraftVersion.equals("1.19")){
                    Lightable lightable = (Lightable) torch.getBlockData();
                    lightable.setLit(false);
                    torch.setBlockData(lightable);
                }
                if(e != null) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
