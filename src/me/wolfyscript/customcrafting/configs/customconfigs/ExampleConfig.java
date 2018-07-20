package me.wolfyscript.customcrafting.configs.customconfigs;

import net.cubespace.yamler.Config.Comment;
import net.cubespace.yamler.Config.Comments;
import net.cubespace.yamler.Config.Path;
import net.cubespace.yamler.Config.YamlConfig;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExampleConfig extends YamlConfig {


    public ExampleConfig(Plugin plugin){
        CONFIG_HEADER = new String[]{"Example-config for custom recipe!"};
        CONFIG_FILE = new File(plugin.getDataFolder()+File.separator+"recipes", "Example.yml");
    }

    @Path("recipe_ID")
    public String recipeName = "example_recipe";

    @Path("output.customName")
    public String outputName = "&4SUPER_WOOD";

    @Path("output.material")
    public String outputMaterial = "WOOD";


    @Comment("All the enchantments! > ID:Level < The Enchant IDs can be found here -> https://minecraft.gamepedia.com/Enchanting#Summary_of_enchantments")
    @Path("output.enchantments")
    public String[] enchantments = new String[]{"0:4","5:3","16:5","33:1","etc."};

    @Path("shape.shape")
    public String[] recipeShape = new String[]{" W ","WDW"," W "};

    @Path("shape.shapeless")
    public boolean shapeless = false;

    @Comments({"Item ID or the name from Spigot can be used!","When Shapeless recipe add materials with any key you want!"})
    @Path("shape.materials")
    public HashMap<Character, String[]> recipeMaterials = new HashMap<>();

    public void addMaterials(){
        if(recipeMaterials.isEmpty()){
            recipeMaterials.put('W',new String[]{"WOOD","variants=0,1,2,3,4,5"});
            recipeMaterials.put('D',new String[]{"DIAMOND"});
        }
    }

    @Path("output.lore")
    public List<String> lore = new ArrayList<>();

    public void addLoreExample(){
        if(lore.isEmpty()){
            lore.add("This is the lore");
            lore.add("type anything here");
            lore.add("...");
        }
    }
}
