package xyz.pixelsky.wirelessredstone.Commands.Device;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static xyz.pixelsky.wirelessredstone.Commands.Device.CD_Validity.hasValidArguments;

//REWORKED
public class CD_CommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args){
        if(!(sender instanceof Player))
            return false;

        Player player = ((Player) sender).getPlayer();

        if(!CD_Validity.hasValidArguments(player, args, 1, 4, new String[] {
                "- /device info",
                "- /device give",
                "- /device link"
        }))
            return true;

        switch(args[0]){
            case "info":
                return CD_Handle.handleInfo(player, args);
            case "give":
                return CD_Handle.handleGive(player, args);
            case "link":
                return CD_Handle.handleLink(player, args);
            //case "deleteall":
            //    return handleDelete(player, args);
            default:
                return CD_Push.pushInvalidArguments(player, new String[] {
                        "- /device info",
                        "- /device give",
                        "- /device link"
                });
        }
    }
}
