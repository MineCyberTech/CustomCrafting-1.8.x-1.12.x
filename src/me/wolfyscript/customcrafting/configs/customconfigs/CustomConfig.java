package me.wolfyscript.customcrafting.configs.customconfigs;

import net.cubespace.yamler.Config.*;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomConfig extends YamlConfig{

    public CustomConfig(Plugin plugin, String fileName){
        CONFIG_HEADER = new String[]{"Example-config for custom recipe!"};
        CONFIG_FILE = new File(plugin.getDataFolder()+File.separator+"recipes", fileName);
        try {
            this.init();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Path("recipe_ID")
    public String recipeName = "";

    @Path("recipe.shapeless")
    public boolean shapeless = false;

    @Path("shape.shape")
    public String[] recipeShape = new String[]{};

    @Comments({"Item ID or the name from Spigot can be used!","When Shapeless recipe add materials with any key!"})
    @Path("shape.materials")
    public HashMap<String, String[]> recipeMaterials = new HashMap<>();

    @Path("output.material")
    public String outputMaterial = "";

    @Path("output.customName")
    public String outputName = "";

    @Path("output.lore")
    public List<String> lore = new ArrayList<String>();

    @Comment("All the enchantments! > ID:Level < The Enchant IDs can be found here -> https://minecraft.gamepedia.com/Enchanting#Summary_of_enchantments")
    @Path("output.enchantments")
    public String[] enchantments = new String[]{};
}
