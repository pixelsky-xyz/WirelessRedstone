package xyz.pixelsky.wirelessredstone;

import xyz.pixelsky.wirelessredstone.Commands.C_Center;
import xyz.pixelsky.wirelessredstone.Config.C_Utility;
import xyz.pixelsky.wirelessredstone.Listeners.Modified.*;
import xyz.pixelsky.wirelessredstone.Listeners.Natural.*;
import xyz.pixelsky.wirelessredstone.Messages.M_Utility;
import xyz.pixelsky.wirelessredstone.Redstone.*;
import xyz.pixelsky.wirelessredstone.Utils.U_Metrics;
import xyz.pixelsky.wirelessredstone.Utils.U_Log;
import xyz.pixelsky.wirelessredstone.Utils.U_Permissions;
import xyz.pixelsky.wirelessredstone.Versions.V_Manager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.pixelsky.wirelessredstone.Listeners.Modified.*;
import xyz.pixelsky.wirelessredstone.Listeners.Natural.*;
import xyz.pixelsky.wirelessredstone.Redstone.*;

public final class WirelessRedstone extends JavaPlugin {

    private static int pluginId = 16969;
    private static WirelessRedstone plugin;
    private static C_Center commandCenter = new C_Center();

    @Override
    public void onEnable() {
        // Plugin startup logic
        initializePlugin(false);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        disablePlugin();
    }

    private void initializePlugin(boolean reload) {
        plugin = this;
        U_Metrics metrics = new U_Metrics(plugin, pluginId);

        String latest = V_Manager.getLatestVersion();
        if(latest != null && !latest.equals(V_Manager.pluginVersion)){
            WirelessRedstone.Log(new U_Log(U_Log.LogType.WARNING, "New update available [" + latest + "]! Visit " + "https://www.spigotmc.org/resources/101871/" + " to download the latest version."));
        }
        V_Manager.setVersion();
        Bukkit.getConsoleSender().sendMessage(getDescription().getFullName() + " by " + ChatColor.RED + getDescription().getAuthors().toString().replace("[", "").replace("]", ""));
        if(!V_Manager.isCompatible()){
            WirelessRedstone.Log(new U_Log(U_Log.LogType.ERROR, "Unsupported version [" + V_Manager.minecraftVersion + "]. Disabling WirelessRedstone."));
            Bukkit.getPluginManager().disablePlugin(WirelessRedstone.getPlugin());
            return;
        }

        C_Utility.initialize(reload);
        R_Items.initialize();

        if(!this.isEnabled())
            return;

        R_DeviceUtility.initialize();
        R_LinkUtility.initialize();
        M_Utility.initialize();
        commandCenter.initialize();
        U_Permissions.register();

        if(!reload)
            for(R_Link link : R_Links.getList().values())
                if(link.isLinked()){
                    link.getReceiver().setUpdating(true);
                    link.getSender().updateSignalPower();
                }

        if(!reload)
            registerEvents();
    }

    private void registerEvents(){
        new LN_BlockPlace(this);
        new LN_BlockBreak(this);
        new LN_BlockClick(this);
        new LN_BlockMove(this);
        new LN_BlockPower(this);
        new LN_WireBreak(this);
        new LN_RecipeUnlock(this);
        new LN_PlayerJoin(this);
        new LN_TorchPower(this);
        new LN_HopperPower(this);

        new LM_DevicePlace(this);
        new LM_DeviceBreak(this);
        new LM_DeviceClick(this);
        new LM_DeviceMove(this);
        new LM_DevicePower(this);
    }

    private void disablePlugin(){
        try {
            R_DeviceUtility.save();
            R_LinkUtility.save();
        }catch (Exception ignored) {}

        commandCenter.unload();
        R_Items.unload();
    }

    public void reloadPlugin(){
        disablePlugin();
        initializePlugin(true);
    }

    public static void Log(U_Log logMes){
        String type = "";
        if(logMes.logType.equals(U_Log.LogType.ERROR))
            type = ChatColor.RED +  "[" + logMes.logType + "] ";
        if(logMes.logType.equals(U_Log.LogType.WARNING))
            type = ChatColor.GOLD +  "[" + logMes.logType + "] ";
        Bukkit.getConsoleSender().sendMessage("[" + getPlugin().getDescription().getName() + "] "
                + type
                + logMes.message);
    }

    public static WirelessRedstone getPlugin(){
        return plugin;
    }
}
