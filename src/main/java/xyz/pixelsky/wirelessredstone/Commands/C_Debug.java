package xyz.pixelsky.wirelessredstone.Commands;

import xyz.pixelsky.wirelessredstone.Redstone.R_Device;
import xyz.pixelsky.wirelessredstone.Redstone.R_Devices;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class C_Debug implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String [] args) {
        if (!(sender instanceof Player))
            return false;
        //Command variables
        Player player = ((Player) sender).getPlayer();
        Block block = player.getTargetBlockExact(10);

        if(block != null) {
            R_Device device = R_Devices.get(block.getLocation());
            if (device != null){
                Bukkit.broadcastMessage("power: " + block.isBlockPowered());
            }
        }
        return true;
    }
}

