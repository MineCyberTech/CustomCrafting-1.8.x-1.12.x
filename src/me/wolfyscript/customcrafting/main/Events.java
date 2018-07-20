package me.wolfyscript.customcrafting.main;

import me.wolfyscript.customcrafting.CustomCraftEvent;
import me.wolfyscript.customcrafting.configs.mainconfig.DataContainer;
import me.wolfyscript.customcrafting.recipes.CustomRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Events implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        CustomCrafting.setAdvancedCrafting(event.getPlayer(), false);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        CustomCrafting.setAdvancedCrafting(event.getPlayer(), false);
    }

    @EventHandler
    public void onPreCraftNew(PrepareItemCraftEvent e) {
        if (CustomCrafting.isAdvancedCrafting((Player) e.getView().getPlayer())) {
            CustomRecipe customRecipe = CustomCrafting.getRecipeLoader().getByItemStack(e.getRecipe().getResult());
            if (customRecipe != null) {
                List<ItemStack> recipeMatrix = new ArrayList<>();
                for (ItemStack itemStack : e.getInventory().getMatrix()) {
                    if (itemStack != null && itemStack.getType() != Material.AIR) {
                        recipeMatrix.add(itemStack);
                    }
                }
                CustomCraftEvent customCraftEvent = new CustomCraftEvent(e.isRepair(), customRecipe, e.getRecipe(), e.getInventory(), recipeMatrix);
                customCraftEvent.setOutPut(customRecipe.getResult());
                Bukkit.getPluginManager().callEvent(customCraftEvent);
                if (!customCraftEvent.isCancelled()) {
                    e.getInventory().setResult(customCraftEvent.getOutPut());
                } else {
                    e.getInventory().setResult(new ItemStack(Material.AIR));
                }

            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.WORKBENCH) {
            String code = DataContainer.getData(block.getLocation());
            if (!code.equals("")) {
                block.setType(Material.AIR);
                ItemStack drop = new ItemStack(Material.WORKBENCH);
                ItemMeta meta = drop.getItemMeta();
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.setDisplayName(code);
                drop.setItemMeta(meta);
                block.getWorld().dropItemNaturally(block.getLocation(), drop);
                DataContainer.removeData(block.getLocation());
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlaceAdvancedWorkbench(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (event.getItemInHand().getType() == Material.WORKBENCH) {
            if (event.getItemInHand().hasItemMeta()) {
                ItemStack itemStack = event.getItemInHand();
                if (CustomCrafting.isAdvancedCraftingBench(itemStack.getItemMeta().getDisplayName())) {
                    DataContainer.setData(block.getLocation(), itemStack.getItemMeta().getDisplayName());
                }
            }
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        if (CustomCrafting.isAdvancedCrafting((Player) event.getPlayer())) {
            CustomCrafting.setAdvancedCrafting((Player) event.getPlayer(), false);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock() != null) {
                Block block = event.getClickedBlock();
                String nameAndCode = DataContainer.getData(block.getLocation());
                if (nameAndCode.split(":").length > 1) {
                    String code = CustomCrafting.unhideString(nameAndCode.split(":")[1]);
                    if (code.equals("customcraftingWorkbench" + CustomCrafting.configManager.getMainConfig().securityCode)) {
                        CustomCrafting.setAdvancedCrafting(event.getPlayer(), true);
                    }
                }
            }
        }
    }
}
