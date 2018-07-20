package me.wolfyscript.customcrafting.recipes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomRecipe {

    private ShapedRecipe shapedRecipe;
    private ShapelessRecipe shapelessRecipe;
    private boolean isShapeless;
    private String nameId;
    private ItemStack outputItem;
    private ItemStack idItem;

    public CustomRecipe(String nameId, ShapedRecipe recipe, ItemStack outputItem){
        this.nameId = nameId;
        this.shapedRecipe = recipe;
        this.shapelessRecipe = null;
        this.outputItem = outputItem;
        this.isShapeless = false;
        createIDItem(nameId);
    }

    public CustomRecipe(String nameId, ShapelessRecipe shapelessRecipe, ItemStack outputItem){
        this.nameId = nameId;
        this.shapelessRecipe = shapelessRecipe;
        this.shapedRecipe = null;
        this.isShapeless = true;
        this.outputItem = outputItem;
        createIDItem(nameId);
    }

    private void createIDItem(String nameId) {
        ItemStack itemStack = new ItemStack(Material.STONE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(nameId);
        itemStack.setItemMeta(itemMeta);
        this.idItem = itemStack;
    }

    public ItemStack getResult(){
        return outputItem;
    }

    public boolean isShapeless() {
        return isShapeless;
    }

    public String getNameId() {
        return nameId;
    }

    public ShapedRecipe getShapedRecipe(){
        return isShapeless ? null : shapedRecipe;
    }

    public ShapelessRecipe getShapelessRecipe() {
        return isShapeless ? shapelessRecipe : null;
    }
}
