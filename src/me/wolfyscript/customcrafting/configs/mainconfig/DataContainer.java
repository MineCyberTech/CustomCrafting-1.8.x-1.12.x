package me.wolfyscript.customcrafting.configs.mainconfig;

import me.wolfyscript.customcrafting.main.CustomCrafting;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataContainer {

    public static File ConfigFile;
    public static FileConfiguration Config;
    public static String kickMessage;

    public static void load() {
        ConfigFile = new File(CustomCrafting.plugin.getDataFolder()+File.separator, "data.yml");
        Config = YamlConfiguration.loadConfiguration(ConfigFile);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!isLoaded()){
            setList();
        }

    }

    public static boolean isLoaded() {
        return Config.contains(".data");
    }

    public static void save() throws IOException {
        Config.save(ConfigFile);
    }

    public static void setList(){
        List<String> locations = new ArrayList<>();
        Config.set(".locations", locations);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addLocation(Location location){
        List<String> locations = getStringLocations();
        locations.add(location.getWorld().getName()+"."+location.getBlockX()+";"+location.getBlockY()+";"+location.getBlockZ());
        Config.set(".locations", locations);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeLocation(Location location){
        List<String> locations = getStringLocations();
        locations.remove(location.getWorld().getName()+"."+location.getBlockX()+";"+location.getBlockY()+";"+location.getBlockZ());
        Config.set(".locations", locations);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getStringLocations(){
        return Config.getStringList(".locations");
    }

    public static List<Location> getLocations(){
        List<Location> locations = new ArrayList<>();
        for(String code : getStringLocations()){
            String cords = code.split("\\.")[1];
            Location location = new Location(Bukkit.getWorld(code.split("\\.")[0]), Double.parseDouble(cords.split(";")[0]), Double.parseDouble(cords.split(";")[1]), Double.parseDouble(cords.split(";")[2]));
            locations.add(location);
        }
        return locations;
    }


    public static void setData(Location location, String data){
        addLocation(location);
        Config.set(".data."+location.getWorld().getName()+"."+location.getBlockX()+";"+location.getBlockY()+";"+location.getBlockZ(), data);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeData(Location location){
        removeLocation(location);
        Config.set(".data."+location.getWorld().getName()+"."+location.getBlockX()+";"+location.getBlockY()+";"+location.getBlockZ(), "");
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getData(Location location){
        if(Config.isSet(".data."+location.getWorld().getName()+"."+location.getBlockX()+";"+location.getBlockY()+";"+location.getBlockZ())){
            return Config.getString(".data."+location.getWorld().getName()+"."+location.getBlockX()+";"+location.getBlockY()+";"+location.getBlockZ());
        }
        return "";
    }

    public static String getData(String path){
        if(Config.isSet(path)){
            return Config.getString(path);
        }
        return "";
    }


}
