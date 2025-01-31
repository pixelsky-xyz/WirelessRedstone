package xyz.pixelsky.wirelessredstone.Messages;

import xyz.pixelsky.wirelessredstone.Config.C_Value;
import xyz.pixelsky.wirelessredstone.Redstone.DeviceType;
import xyz.pixelsky.wirelessredstone.Utils.U_Device;
import xyz.pixelsky.wirelessredstone.Utils.U_Log;
import xyz.pixelsky.wirelessredstone.WirelessRedstone;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class M_Utility {
    private static LinkedHashMap<String, String> defaultMessages = new LinkedHashMap<>();
    private static LinkedHashMap<String, String> messages = new LinkedHashMap<>();
    private static final String messagesPath = WirelessRedstone.getPlugin().getDataFolder().getAbsolutePath();
    private static final String messagesFile = "messages.yml";

    public static void initialize(){
        File file = new File(messagesPath + "/" + messagesFile);
        WirelessRedstone.Log(new U_Log(U_Log.LogType.INFORMATION, ChatColor.GRAY + "Default language is set as "+ ChatColor.RED + C_Value.getLanguage() + ChatColor.GRAY + "."));
        String languageFile = "messages_" + C_Value.getLanguage() + ".yml";

        defaultMessages = readAndLoad(WirelessRedstone.getPlugin().getClass().getResourceAsStream("/" + languageFile));
        if (!file.exists()) {
            create();
            messages = defaultMessages;
        }

        try {
            messages = readAndLoad(new FileInputStream(file));
            update();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void create() {
        try {
            String languageFile = "messages_" + C_Value.getLanguage() + ".yml";
            byte[] buffer = WirelessRedstone.getPlugin().getClass().getResourceAsStream("/" + languageFile).readAllBytes();

            File targetFile = new File(messagesPath + "/" + messagesFile);
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);
            outStream.close();
        } catch (IOException e) {
            WirelessRedstone.Log(new U_Log(U_Log.LogType.ERROR, "Could not create " + messagesFile + ".\n" + e.getMessage()));
        }
    }

    private static LinkedHashMap<String, String> readAndLoad(InputStream inputStream){
        try {
            LinkedHashMap<String, String> values = new LinkedHashMap<>();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while(reader.ready()) {
                String line = reader.readLine();
                if(line.contains(":")) {
                    String[] l = line.split(":", 2);
                    String key = l[0].trim();
                    String value = l[1].trim();
                    if(value.endsWith("\"") && value.startsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                    }
                    values.put(key, value);
                }
            }
            reader.close();
            inputStream.close();
            return values;
        } catch (IOException ignored) {
        }
        return null;
    }

    private static void update(){
        boolean update = false;
        if(messages != null) {
            for (String string : defaultMessages.keySet()) {
                if (!messages.containsKey(string)) {
                    update = true;
                    messages.put(string, defaultMessages.get(string));
                }
            }
        }
        else{
            create();
            return;
        }

        if(update){
            try {
                StringBuilder data = new StringBuilder();
                File file = new File(messagesPath + "/" + messagesFile);
                file.getParentFile().mkdir();
                for (String s : messages.keySet()) {
                    if (data.toString().equalsIgnoreCase(""))
                        data.append(s).append(": ").append("\"").append(messages.get(s)).append("\"");
                    else
                        data.append("\n").append(s).append(": ").append("\"").append(messages.get(s)).append("\"");
                }
                file.createNewFile();
                Writer writer = new FileWriter(file, false);
                writer.write(data.toString());
                writer.flush();
                writer.close();
            } catch (Exception e) {
                WirelessRedstone.Log(new U_Log(U_Log.LogType.ERROR, "Could not update " + messagesFile + ".\n" + e.getMessage()));
            }
        }
    }

    public static String getMessage(String key){
        return getMessage(key, null);
    }

    public static String getMessage(String key, HashMap<String, Object> parameters){
        String str = colorString(messages.get(key));
        if(str == null)
            str = colorString(defaultMessages.get(key));
        if(str == null)
            return ChatColor.RED + "Could not find message key: " + ChatColor.DARK_RED + key + ChatColor.RED + ". This is a Bug.";
        if(parameters == null)
            return str;
        for(String k : parameters.keySet())
            if(str.contains(k))
                str = str.replace(k, parameters.get(k).toString());
        return str;
    }

    public static boolean sendMessage(Player player, String value){
        if(!C_Value.allowMessages())
            return false;
        if(player == null)
            return false;
        if(value.startsWith("!!"))
            return false;

        player.sendMessage(value);
        return true;
    }

    public static HashMap placeHolder(Object ... parameters){
        if (parameters == null || parameters.length % 2 != 0 || parameters.length == 0)
            return null;

        HashMap<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < parameters.length; i++) {
            String value = parameters[(i+1)].toString();
            if(value.equals(DeviceType.RedstoneSender.toString()))
                value = U_Device.getDeviceName(DeviceType.RedstoneSender);
            else if(value.equals(DeviceType.RedstoneReceiver.toString()))
                value = U_Device.getDeviceName(DeviceType.RedstoneReceiver);
            map.put(parameters[i].toString(), value);
            i++;
        }
        return map;
    }

    private static String colorString(String str){
        if(str == null)
            return null;
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
