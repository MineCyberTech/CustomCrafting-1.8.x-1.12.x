package me.wolfyscript.customcrafting.recipes;

import me.wolfyscript.customcrafting.configs.ConfigManager;
import me.wolfyscript.customcrafting.configs.customconfigs.CustomConfig;
import me.wolfyscript.customcrafting.main.CustomCrafting;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RecipeLoader {

    private ConfigManager configManager;

    private List<ItemStack> recipesResults = new ArrayList<>();

    private HashMap<String, Recipe> recipesList = new HashMap<>();
    private ArrayList<CustomRecipe> customRecipes = new ArrayList<>();

    public RecipeLoader(Plugin plugin, ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void loadRecipesNew() {
        CustomCrafting.sendMessageConsole("--------------------------------------------------------------------");
        CustomCrafting.sendMessageConsole("                         Loading recipes.....                       ");
        CustomCrafting.sendMessageConsole("--------------------------------------------------------------------");
        CustomCrafting.sendMessageConsole("                               ");

        recipesResults.clear();
        if (configManager.getCustomConfigs() != null && !configManager.getCustomConfigs().isEmpty()) {
            ItemStack outputItem;
            for (CustomConfig config : configManager.getCustomConfigs()) {
                System.out.print("Loading recipe out of " + config.CONFIG_FILE.getName() + "...");
                Material outPutMaterial = Material.getMaterial(config.outputMaterial);
                if (outPutMaterial == null) {
                    outPutMaterial = Material.matchMaterial(config.outputMaterial);
                }
                if (outPutMaterial != null) {
                    outputItem = new ItemStack(outPutMaterial);
                    ItemMeta itemMeta = outputItem.getItemMeta();
                    for (String code : config.enchantments) {
                        Enchantment enchantment = Enchantment.getById(Integer.parseInt(code.split(":")[0]));
                        if (enchantment != null) {
                            itemMeta.addEnchant(enchantment, Integer.parseInt(code.split(":")[1]), false);
                        }
                    }
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.outputName));
                    List<String> loreColor = new ArrayList<>();
                    for(String loreLine : config.lore){
                        loreColor.add(ChatColor.translateAlternateColorCodes('&', loreLine));
                    }
                    itemMeta.setLore(loreColor);
                    outputItem.setItemMeta(itemMeta);

                    ItemStack defaultOutput = new ItemStack(Material.STONE);
                    ItemMeta defaultItemMeta = defaultOutput.getItemMeta();
                    defaultItemMeta.setDisplayName(config.recipeName);
                    defaultOutput.setItemMeta(defaultItemMeta);

                    if (config.shapeless) {
                        ArrayList<ShapelessRecipe> variantsRecipes = new ArrayList<>();

                        for (String ingredientKey : config.recipeMaterials.keySet()) {
                            boolean onlyWithCustomName = false;
                            boolean onlyWithoutCustomName = false;
                            int[] variants = null;
                            int amount = 1;
                            if (config.recipeMaterials.get(ingredientKey).length > 1) {
                                for (String attribute : config.recipeMaterials.get(ingredientKey)) {
                                    if (attribute.startsWith("onlyWithCustomName")) {
                                        String value = attribute.split("=")[1];
                                        onlyWithCustomName = Boolean.getBoolean(value);
                                    }
                                    if (attribute.startsWith("onlyWithoutCustomName")) {
                                        String value = attribute.split("=")[1];
                                        onlyWithoutCustomName = Boolean.getBoolean(value);
                                    }
                                    if (attribute.startsWith("variants")) {
                                        String[] variantsStrings = attribute.split("=")[1].split(",");
                                        int[] variantsDummy = new int[variantsStrings.length];
                                        for (int i = 0; i < variantsStrings.length; i++) {
                                            variantsDummy[i] = Integer.parseInt(variantsStrings[i]);
                                        }
                                        variants = variantsDummy;
                                    }
                                    if(attribute.startsWith("amount")){
                                        String value = attribute.split("=")[1];
                                        try {
                                            amount = Integer.parseInt(value);
                                        }catch (NumberFormatException e){
                                            amount = 1;
                                        }
                                    }
                                }
                            }
                            Material ingredientMaterial = Material.getMaterial(String.valueOf(config.recipeMaterials.get(ingredientKey)[0]));
                            if (ingredientMaterial == null) {
                                ingredientMaterial = Material.matchMaterial(String.valueOf(config.recipeMaterials.get(ingredientKey)[0]));
                            }
                            if (ingredientMaterial != null) {
                                if(variantsRecipes.isEmpty()){
                                    if(variants!=null && variants.length!=0){
                                        for(int variant : variants){
                                            ShapelessRecipe recipe = new ShapelessRecipe(defaultOutput);
                                            recipe.addIngredient(amount, ingredientMaterial, variant);
                                            variantsRecipes.add(recipe);
                                        }
                                    }else{
                                        ShapelessRecipe recipe = new ShapelessRecipe(defaultOutput);
                                        recipe.addIngredient(amount, ingredientMaterial);
                                        variantsRecipes.add(recipe);
                                    }
                                }else{
                                    if (variants != null && variants.length != 0) {
                                        int count = variants.length;
                                        ArrayList<ShapelessRecipe> copiedList = new ArrayList<>();
                                        for (ShapelessRecipe recipeToCopy : variantsRecipes) {
                                            for (int i = 0; i < count; i++) {
                                                ShapelessRecipe recipeCopy = new ShapelessRecipe(recipeToCopy.getResult());
                                                for(ItemStack ingred : recipeToCopy.getIngredientList()){
                                                    recipeCopy.addIngredient(ingred.getAmount(), ingred.getType(), ingred.getData().getData());
                                                }
                                                copiedList.add(recipeCopy);
                                            }
                                        }
                                        variantsRecipes.clear();
                                        variantsRecipes.addAll(copiedList);
                                        int variant = 0;
                                        for (ShapelessRecipe recipe : variantsRecipes) {
                                            recipe.addIngredient(amount, ingredientMaterial, variants[variant]);
                                            if (variant + 1 == count) {
                                                variant = 0;
                                            } else {
                                                variant++;
                                            }
                                        }
                                    }
                                }
                            } else {
                                System.out.print("ERROR loading Custom recipe: " + config.recipeName);
                                System.out.print("Cause: can't get Material: " + config.recipeMaterials.get(ingredientKey)[0]);
                            }
                        }
                        int i = 1;
                        for (ShapelessRecipe recipe : variantsRecipes){
                            if(CustomCrafting.getConfigManager().getMainConfig().debug){
                                System.out.print("Recipe " + i + ": " + recipe.getIngredientList());
                                for (ItemStack item : recipe.getIngredientList()) {
                                    if (item != null) {
                                        System.out.print(" -> " + item.getType() + ":" + item.getData().getData());
                                    }
                                }
                            }
                            Bukkit.addRecipe(recipe);
                            this.recipesResults.add(outputItem);
                            this.recipesList.put(config.recipeName, recipe);
                            customRecipes.add(new CustomRecipe(config.recipeName, recipe, outputItem));
                            i++;
                        }

                    } else {
                        ArrayList<ShapedRecipe> variantsRecipes = new ArrayList<>();
                        for (String ingredientKey : config.recipeMaterials.keySet()) {
                            boolean onlyWithCustomName = false;
                            boolean onlyWithoutCustomName = false;
                            int[] variants = null;
                            if (config.recipeMaterials.get(ingredientKey).length > 1) {
                                for (String attribute : config.recipeMaterials.get(ingredientKey)) {
                                    if (attribute.startsWith("onlyWithCustomName")) {
                                        String value = attribute.split("=")[1];
                                        onlyWithCustomName = Boolean.getBoolean(value);
                                    }
                                    if (attribute.startsWith("onlyWithoutCustomName")) {
                                        String value = attribute.split("=")[1];
                                        onlyWithoutCustomName = Boolean.getBoolean(value);
                                    }
                                    if (attribute.startsWith("variants")) {
                                        String[] variantsStrings = attribute.split("=")[1].split(",");
                                        int[] variantsDummy = new int[variantsStrings.length];
                                        for (int i = 0; i < variantsStrings.length; i++) {
                                            variantsDummy[i] = Integer.parseInt(variantsStrings[i]);
                                        }
                                        variants = variantsDummy;
                                    }
                                }
                            }
                            Material ingredientMaterial = Material.getMaterial(String.valueOf(config.recipeMaterials.get(ingredientKey)[0]));
                            if (ingredientMaterial == null) {
                                ingredientMaterial = Material.matchMaterial(String.valueOf(config.recipeMaterials.get(ingredientKey)[0]));
                            }
                            if (ingredientMaterial != null) {
                                if (variantsRecipes.isEmpty()) {
                                    if (variants != null && variants.length != 0) {
                                        for (int variant : variants) {
                                            ShapedRecipe recipe = new ShapedRecipe(defaultOutput);
                                            recipe.shape(config.recipeShape);
                                            recipe.setIngredient(ingredientKey.charAt(0), ingredientMaterial, variant);
                                            variantsRecipes.add(recipe);
                                        }
                                    } else {
                                        ShapedRecipe recipe = new ShapedRecipe(defaultOutput);
                                        recipe.shape(config.recipeShape);
                                        recipe.setIngredient(ingredientKey.charAt(0), ingredientMaterial);
                                        variantsRecipes.add(recipe);
                                    }
                                    if(CustomCrafting.getConfigManager().getMainConfig().debug){
                                        System.out.print("Recipe amount: " + variantsRecipes.size());
                                    }
                                } else {
                                    if (variants != null && variants.length != 0) {
                                        int count = variants.length;
                                        ArrayList<ShapedRecipe> copiedList = new ArrayList<>();
                                        for (ShapedRecipe recipeToCopy : variantsRecipes) {
                                            for (int i = 0; i < count; i++) {
                                                ShapedRecipe recipeCopy = new ShapedRecipe(recipeToCopy.getResult());
                                                recipeCopy.shape(recipeToCopy.getShape());
                                                for (char key : recipeToCopy.getIngredientMap().keySet()) {
                                                    if (recipeToCopy.getIngredientMap().get(key) != null) {
                                                        recipeCopy.setIngredient(key, recipeToCopy.getIngredientMap().get(key).getType(), recipeToCopy.getIngredientMap().get(key).getData().getData());
                                                    }
                                                }
                                                copiedList.add(recipeCopy);
                                            }
                                        }
                                        variantsRecipes.clear();
                                        variantsRecipes.addAll(copiedList);
                                        int variant = 0;
                                        for (ShapedRecipe recipe : variantsRecipes) {
                                            recipe.setIngredient(ingredientKey.charAt(0), ingredientMaterial, variants[variant]);
                                            if (variant + 1 == count) {
                                                variant = 0;
                                            } else {
                                                variant++;
                                            }
                                        }
                                    } else {
                                        for (ShapedRecipe recipe : variantsRecipes) {
                                            recipe.setIngredient(ingredientKey.charAt(0), ingredientMaterial);
                                        }
                                    }
                                }
                            } else {
                                System.out.print("ERROR loading Custom recipe: " + config.recipeName);
                                System.out.print("Cause: can't get Material: " + config.recipeMaterials.get(ingredientKey)[0]);
                            }
                        }
                        if(CustomCrafting.getConfigManager().getMainConfig().debug){
                            System.out.print("Recipe amount: " + variantsRecipes.size());
                        }
                        int i = 0;
                        for (ShapedRecipe recipe : variantsRecipes) {
                            if(CustomCrafting.getConfigManager().getMainConfig().debug){
                                System.out.print("Recipe " + i + ": " + recipe.getIngredientMap());
                                for (char key : recipe.getIngredientMap().keySet()) {
                                    if (recipe.getIngredientMap().get(key) != null) {
                                        System.out.print(" -> " + recipe.getIngredientMap().get(key).getType() + ":" + recipe.getIngredientMap().get(key).getData());
                                    }
                                }
                            }
                            Bukkit.addRecipe(recipe);
                            this.recipesResults.add(outputItem);
                            this.recipesList.put(config.recipeName, recipe);
                            customRecipes.add(new CustomRecipe(config.recipeName, recipe, outputItem));
                            i++;
                        }
                    }
                    System.out.print("Successfully loaded recipe \"" + config.recipeName + "\" out of " + config.CONFIG_FILE.getName());
                    CustomCrafting.sendMessageConsole(
                            "********************************************************************");
                } else {
                    System.out.print("ERROR loading Custom recipe: " + config.recipeName);
                }

            }
        }
        CustomCrafting.sendMessageConsole("                                                                                                  \n" +
                "                                                                          \n" +
                "                           Everything successfully loaded!                           \n" +
                "  ______ ______ ______ ______ ______ ______ ______ ______ ______ ______ ______ ______\n" +
                " /_____//_____//_____//_____//_____//_____//_____//_____//_____//_____//_____//_____/\n" +
                "                                                                                      ");
    }

    public List<ItemStack> getRecipesResults() {
        return recipesResults;
    }

    public HashMap<String, Recipe> getRecipesList() {
        return recipesList;
    }

    public CustomRecipe getByRecipe(Recipe recipe) {
        for (CustomRecipe customRecipe : customRecipes) {
            if(recipe.getResult().hasItemMeta()){
                if(recipe.getResult().getItemMeta().getDisplayName().equals(customRecipe.getNameId())){
                    return customRecipe;
                }
            }
        }
        return null;
    }

    public CustomRecipe getByItemStack(ItemStack itemStack) {
        for (CustomRecipe customRecipe : customRecipes) {
            if (itemStack.hasItemMeta()) {
                if(itemStack.getItemMeta().getDisplayName().equals(customRecipe.getNameId())){
                    return customRecipe;
                }
            }
        }
        return null;
    }

    public boolean isCustomRecipe(Recipe recipe) {
        return getByRecipe(recipe) != null;
    }

    public boolean isCustomRecipe(ItemStack itemStack) {
        return getByItemStack(itemStack) != null;
    }
}
