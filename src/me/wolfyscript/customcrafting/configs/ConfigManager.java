package me.wolfyscript.customcrafting.configs;

import me.wolfyscript.customcrafting.configs.customconfigs.CustomConfig;
import me.wolfyscript.customcrafting.configs.customconfigs.ExampleConfig;
import me.wolfyscript.customcrafting.configs.mainconfig.DataContainer;
import me.wolfyscript.customcrafting.configs.mainconfig.MainConfig;
import net.cubespace.yamler.Config.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private Plugin plugin;

    private MainConfig mainConfig;

    private List<CustomConfig> customConfigs = new ArrayList<>();

    public ConfigManager(Plugin plugin){
        this.plugin = plugin;
        DataContainer.load();
        mainConfig = new MainConfig(plugin);
        try {
            mainConfig.init();
            mainConfig.addIngredients();
            mainConfig.save();
            mainConfig.reload();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        loadCustomConfigs();
    }

    public void loadCustomConfigs(){
        customConfigs.clear();
        File dir = new File(plugin.getDataFolder()+File.separator+"recipes");
        if(!dir.exists()){
            dir.mkdirs();
        }

        ExampleConfig exampleConfig = new ExampleConfig(this.plugin);
        try {
            exampleConfig.init();
            exampleConfig.addMaterials();
            exampleConfig.addLoreExample();
            exampleConfig.save();
            exampleConfig.reload();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.split("\\.")[1].equalsIgnoreCase("yml") && !name.split("\\.")[0].equals("Example"))
                    return true;
                return false;
            }
        });
        if(files != null){
            for (File file : files){
                customConfigs.add(new CustomConfig(this.plugin, file.getName()));

            }
        }
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public List<CustomConfig> getCustomConfigs() {
        return customConfigs;
    }
}
