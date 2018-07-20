package me.wolfyscript.customcrafting;

import me.wolfyscript.customcrafting.recipes.CustomRecipe;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.*;

import java.util.HashMap;
import java.util.List;

public class CustomCraftEvent extends Event implements Cancellable{

    private static final HandlerList handlers = new HandlerList();
    private ItemStack outPut;
    private CraftingInventory craftingInventory;
    private Recipe recipe;
    private boolean isRepair;
    private boolean cancelled;
    private CustomRecipe customRecipe;
    private List<ItemStack> recipeMatrix;

    public CustomCraftEvent(boolean isRepair, CustomRecipe customRecipe, Recipe recipe, CraftingInventory craftingInventory, List<ItemStack> recipeMatrix){
        this.customRecipe = customRecipe;
        this.recipe = recipe;
        this.craftingInventory = craftingInventory;
        this.isRepair = isRepair;
        this.outPut = recipe.getResult();
        this.recipeMatrix = recipeMatrix;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public List<ItemStack> getRecipeMatrix(){
        return recipeMatrix;
    }

    public CraftingInventory getCraftingInventory() {
        return craftingInventory;
    }

    public ItemStack getOutPut() {
        return outPut;
    }

    public void setOutPut(ItemStack outPut) {
        this.outPut = outPut;
    }

    public boolean isRepair() {
        return isRepair;
    }

    public CustomRecipe getCustomRecipe(){
        return this.customRecipe;
    }

    @Override
    public String getEventName() {
        return super.getEventName();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
