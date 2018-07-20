package me.wolfyscript.customcrafting.configs.mainconfig;

import me.wolfyscript.customcrafting.main.CustomCrafting;
import net.cubespace.yamler.Config.Comment;
import net.cubespace.yamler.Config.Path;
import net.cubespace.yamler.Config.YamlConfig;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;

public class MainConfig extends YamlConfig {


    public MainConfig(Plugin plugin){
        CONFIG_HEADER = new String[]{"Main Config - CustomCrafting v"+plugin.getDescription().getVersion()};
        CONFIG_FILE = new File(plugin.getDataFolder(), "Main-Config.yml");
    }
    @Path("debugging")
    public boolean debug = false;

    @Path("Workbench.name")
    public String cutomCraftingBenchName = "&6Advanced Workbench";

    @Comment("This code is here to make sure that nobody can cheat the Advanced Workbench! Dont't change this Code!")
    @Path("Workbench.securityCode")
    public String securityCode = CustomCrafting.getCode();

    @Path("Workbench.shape")
    public String[] recipeShape = new String[]{" G "," C "," R "};

    @Comment("Item ID or the name from Spigot can be used!")
    @Path("Workbench.materials")
    public HashMap<String, String> recipeMaterials = new HashMap<>();

    public void addIngredients(){
        recipeMaterials.put("G", Material.GOLD_INGOT.name());
        recipeMaterials.put("C", Material.WORKBENCH.name());
        recipeMaterials.put("R", Material.REDSTONE.name());
    }

}
