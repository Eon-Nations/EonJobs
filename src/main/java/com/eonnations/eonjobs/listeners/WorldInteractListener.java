package com.eonnations.eonjobs.listeners;

import com.eonnations.eonjobs.EonJobs;
import com.eonnations.eonjobs.events.*;
import com.eonnations.eonjobs.jobs.Job;
import com.eonnations.eonjobs.jobs.JobsManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class WorldInteractListener implements Listener {

    EonJobs plugin;
    JobsManager jobsManager;

    public WorldInteractListener(EonJobs plugin, JobsManager jobsManager) {
        this.plugin = plugin;
        this.jobsManager = jobsManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {
        Player p = e.getPlayer();
        World world = e.getPlayer().getWorld();
        Job job = jobsManager.getPlayerJob(p.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                Bukkit.getPluginManager().callEvent(new JobBreakEvent(p, e.getBlock(), world, job)));
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e) {
        Material material = e.getBlock().getType();
        Player p = e.getPlayer();
        Job job = jobsManager.getPlayerJob(p.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                Bukkit.getPluginManager().callEvent(new JobPlaceEvent(p, material, job)));
    }

    @EventHandler
    public void onFishEvent(PlayerFishEvent e) {
        if (e.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
            if (e.getCaught() == null) return;
            if (e.getCaught() instanceof Item item) {
                String name = item.getItemStack().getType().name().toLowerCase();
                Player p = e.getPlayer();
                Job job = jobsManager.getPlayerJob(p.getUniqueId());

                Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                        Bukkit.getPluginManager().callEvent(new JobFishEvent(p, name, job)));
            }
        }
    }

    @EventHandler
    public void onCowMilk(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() instanceof Cow cow && e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BUCKET)) {
            Job job = jobsManager.getPlayerJob(e.getPlayer().getUniqueId());
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                    Bukkit.getPluginManager().callEvent(new JobMilkEvent(e.getPlayer(), cow, job)));
        }
    }

    @EventHandler
    public void onShearSheepEvent(PlayerShearEntityEvent e) {
        if (e.getEntity() instanceof Sheep sheep) {
            Player p = e.getPlayer();
            Job job = jobsManager.getPlayerJob(p.getUniqueId());

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                    Bukkit.getPluginManager().callEvent(new JobShearEvent(p, job, sheep)));
        }
    }

    @EventHandler
    public void onEnchantEvent(EnchantItemEvent e) {
        Player p = e.getEnchanter();
        Map<Enchantment, Integer> enchantMap = e.getEnchantsToAdd();
        ItemStack itemToEnchant = e.getItem();
        Job job = jobsManager.getPlayerJob(p.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                Bukkit.getPluginManager().callEvent(new JobEnchantEvent(p, enchantMap, job, itemToEnchant, e.getExpLevelCost())));
    }

    @EventHandler
    public void onFurnaceExtract(FurnaceExtractEvent e) {
        Player p = e.getPlayer();
        int amount = e.getItemAmount();
        Material material = e.getItemType();
        Job job = jobsManager.getPlayerJob(p.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                Bukkit.getPluginManager().callEvent(new JobSmeltEvent(p, material, amount, job)));
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            Player p = e.getEntity().getKiller();
            String name = e.getEntityType().name();
            Job job = jobsManager.getPlayerJob(p.getUniqueId());

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                    Bukkit.getPluginManager().callEvent(new JobKillEvent(p, job, name)));
        }
    }

    @EventHandler
    public void onEntityBreedEvent(EntityBreedEvent e) {
        if (e.getBreeder() != null && e.getBreeder() instanceof Player p) {
            Job job = jobsManager.getPlayerJob(p.getUniqueId());
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                    Bukkit.getPluginManager().callEvent(new JobBreedEvent(p, job, e.getMother())));
        }
    }

    @EventHandler
    public void onCraftEvent(InventoryClickEvent e) {
        if (e.getClickedInventory() != null && e.getClickedInventory().getType().equals(InventoryType.WORKBENCH)) {
            if (e.getSlot() == 0) {
                int beforeAmount = 0;
                Player p = (Player) e.getWhoClicked();
                ItemStack item = e.getCurrentItem();
                ItemStack[] beforeItems = p.getInventory().getContents();
                Material type = item.getType();

                for (ItemStack beforeItem : beforeItems) {
                    if (beforeItem != null && beforeItem.getType().equals(type)) {
                        beforeAmount += beforeItem.getAmount();
                    }
                }
                Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runAmountTask(p, type, beforeAmount), 1L);
            }
        }
    }

    private Runnable runAmountTask(Player p, Material type, int beforeAmount) {
        return () -> {
            int afterAmount = getAfterAmount(p, type);
            Bukkit.getPluginManager().callEvent(new JobCraftEvent(p, afterAmount - beforeAmount, type));
        };
    }

    public int getAfterAmount(Player p, Material type) {
        int amount = 0;
        for (ItemStack item : p.getInventory().getContents()) {
            if (item != null && item.getType().equals(type)) {
                amount += item.getAmount();
            }
        }
        return amount;
    }
}
