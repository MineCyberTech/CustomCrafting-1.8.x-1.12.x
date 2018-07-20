package me.wolfyscript.customcrafting.main;

import me.wolfyscript.customcrafting.configs.ConfigManager;
import me.wolfyscript.customcrafting.configs.mainconfig.DataContainer;
import me.wolfyscript.customcrafting.recipes.RecipeLoader;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CustomCrafting extends JavaPlugin {

    static ConfigManager configManager;

    private static RecipeLoader recipeLoader;

    public static  Plugin plugin;

    private static Random random = new Random();

    private static HashMap<String, Boolean> advancedCraftingPlayers = new HashMap<>();

    public static HashMap<String, Boolean> getAdvancedCraftingPlayers() {
        return advancedCraftingPlayers;
    }

    static void setAdvancedCrafting(Player player, boolean advancedCrafting){
        advancedCraftingPlayers.put(player.getUniqueId().toString(), advancedCrafting);
    }

    static boolean isAdvancedCrafting(Player player){
        return advancedCraftingPlayers.get(player.getUniqueId().toString());
    }

    public void onEnable() {
        plugin = this;
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new Events(), this);
        File dir = new File(plugin.getDataFolder()+File.separator+"recipes");
        if(!dir.exists()){
            dir.mkdirs();
        }
        //exportConfigRecipeDir("Example", CustomCrafting.class);

        Bukkit.getConsoleSender().sendMessage("\n" +
                "    ______           __                  ______           ______  _            \n" +
                "   / ____/_  _______/ /_____  ____ ___  / ____/________ _/ __/ /_(_)___  ____ _\n" +
                "  / /   / / / / ___/ __/ __ \\/ __ `__ \\/ /   / ___/ __ `/ /_/ __/ / __ \\/ __ `/\n" +
                " / /___/ /_/ (__  ) /_/ /_/ / / / / / / /___/ /  / /_/ / __/ /_/ / / / / /_/ / \n" +
                " \\____/\\__,_/____/\\__/\\____/_/ /_/ /_/\\____/_/   \\__,_/_/  \\__/_/_/ /_/\\__, /" +"\n"+
                "                                                                      /____/ "+"v "+plugin.getDescription().getVersion()+"\n");

        configManager = new ConfigManager(this);
        recipeLoader = new RecipeLoader(this, configManager);
        recipeLoader.loadRecipesNew();

        ItemStack outPut = new ItemStack(Material.WORKBENCH);
        ItemMeta itemMeta = outPut.getItemMeta();
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',configManager.getMainConfig().cutomCraftingBenchName)+hideString(":customcraftingWorkbench"+configManager.getMainConfig().securityCode));
        outPut.setItemMeta(itemMeta);

        ShapedRecipe recipe = new ShapedRecipe(outPut);
        recipe.shape(configManager.getMainConfig().recipeShape);
        for(String name : configManager.getMainConfig().recipeMaterials.keySet()){
            Material ingredientMaterial = Material.getMaterial(String.valueOf(configManager.getMainConfig().recipeMaterials.get(name)));
            if(ingredientMaterial == null){
                ingredientMaterial = Material.matchMaterial(String.valueOf(configManager.getMainConfig().recipeMaterials.get(name)));
            }
            if(ingredientMaterial != null){
                recipe.setIngredient(name.charAt(0), ingredientMaterial);
            }else{
                System.out.print("ERROR loading Workbench crafting recipe!");
            }
        }
        Bukkit.addRecipe(recipe);

        for(Player player : Bukkit.getServer().getOnlinePlayers()){
            setAdvancedCrafting(player, false);
            player.closeInventory();
        }

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                List<Location> locations = DataContainer.getLocations();
                if(locations != null && !locations.isEmpty()){
                    for(Location location : locations){
                        location.getWorld().spigot().playEffect(location.clone().add(0.3,1,0.3), Effect.CRIT, 10, 0,0f,0f,0,0.001f,1,4);
                        location.getWorld().spigot().playEffect(location.clone().add(0.3,1,0.5), Effect.MAGIC_CRIT, 10, 0,0f,0f,0,0.001f,1,4);
                        location.getWorld().spigot().playEffect(location.clone().add(0.3,1,0.7), Effect.CRIT, 10, 0,0f,0f,0,0.001f,1,4);
                        location.getWorld().spigot().playEffect(location.clone().add(0.5,1,0.3), Effect.MAGIC_CRIT, 10, 0,0f,0f,0,0.001f,1,4);
                        location.getWorld().spigot().playEffect(location.clone().add(0.5,1,0.5), Effect.CRIT, 10, 0,0f,0f,0,0.001f,1,4);
                        location.getWorld().spigot().playEffect(location.clone().add(0.5,1,0.7), Effect.MAGIC_CRIT, 10, 0,0f,0f,0,0.001f,1,4);
                        location.getWorld().spigot().playEffect(location.clone().add(0.7,1,0.3), Effect.CRIT, 10, 0,0f,0f,0,0.001f,1,4);
                        location.getWorld().spigot().playEffect(location.clone().add(0.7,1,0.5), Effect.MAGIC_CRIT, 10, 0,0f,0f,0,0.001f,1,4);
                        location.getWorld().spigot().playEffect(location.clone().add(0.7,1,0.7), Effect.CRIT, 10, 0,0f,0f,0,0.001f,1,4);
                        location.getWorld().spigot().playEffect(location.clone().add(0.5,0.5,0.5), Effect.PORTAL, 10, 0,0f,0f,0,0.2f,10,10);
                    }
                }
            }
        },40,1 );
    }


    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static RecipeLoader getRecipeLoader() {
        return recipeLoader;
    }

    public static void sendMessageConsole(String msg){
        Bukkit.getConsoleSender().sendMessage(msg);
    }
    /*
    Sets a § before every letter!
    example:
        Hello World -> §H§e§l§l§o§ §W§o§r§l§d

    Because of this the String will be invisible in Minecraft!
     */
    public static String hideString(String hide) {
        char[] data = new char[hide.length() * 2];

        for (int i = 0; i < data.length; i += 2) {
            data[i] = 167;
            data[i + 1] = hide.charAt(i == 0 ? 0 : i / 2);
        }

        return new String(data);
    }

    public static String unhideString(String unhide) {
        return unhide.replace("§", "");
    }

    public static boolean isAdvancedCraftingBench(String name){
        String code = name.split(":")[1];
        if(code!=null && !code.isEmpty()){
            String unCensored = unhideString(code);
            if(unCensored.equals("customcraftingWorkbench"+getConfigManager().getMainConfig().securityCode)){
                return true;
            }
        }
        return false;
    }

    public static String getCode(){
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final int x = alphabet.length();
        StringBuilder sB = new StringBuilder("");
        for(int i = 0; i < 16; i++){
            sB.append(alphabet.charAt(random.nextInt(x)));
        }
        return sB.toString();
    }


    public static void exportConfigRecipeDir(String resourceName, Class<?> usedJar) {
        URL inputUrl = usedJar.getResource("/recipes/"+resourceName+".yml");
        File targetFile = new File(plugin.getDataFolder() + File.separator + "recipes"+ File.separator + resourceName+".yml");
        if(!targetFile.exists()){
            try {
                System.out.print("Export default "+resourceName+".yml into the recipe folder!");
                FileUtils.copyURLToFile(inputUrl, targetFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isAlreadyExported(String resourceName){
        return new File(plugin.getDataFolder() + File.separator + "recipes"+ File.separator + resourceName+".yml").exists();
    }

    public static void reloadConfigsAndRecipes(){
        if(getConfigManager() != null){
            getConfigManager().loadCustomConfigs();

        }
        if(getRecipeLoader() != null){
            getRecipeLoader().loadRecipesNew();
        }
    }
}
