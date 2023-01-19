package com.eonnations.eonjobs.menus;

import com.eonnations.eonjobs.EonJobs;
import com.eonnations.eonjobs.jobs.Jobs;
import com.eonnations.eonjobs.jobs.JobsManager;
import com.eonnations.eonjobs.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class JobChoiceMenu implements Listener {

    EonJobs plugin;
    JobInfoMenu jobInfoMenu;
    JobsManager jobsManager;
    Inventory inv, confirmationInv;
    public HashMap<UUID, Jobs> confirmationMap;

    public JobChoiceMenu(EonJobs plugin, JobInfoMenu jobInfoMenu, JobsManager jobsManager) {
        this.plugin = plugin;
        this.jobInfoMenu = jobInfoMenu;
        this.jobsManager = jobsManager;
        inv = getInventory();
        confirmationInv = getConfirmationInventory();
        confirmationMap = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().equals(inv)) {
            Jobs enumJob;
            switch (e.getCurrentItem().getType()) {
                case WOODEN_AXE -> enumJob = Jobs.WOODCUTTER;
                case FISHING_ROD -> enumJob = Jobs.FISHERMAN;
                case ANVIL -> enumJob = Jobs.BLACKSMITH;
                case BOW -> enumJob = Jobs.HUNTER;
                case WOODEN_SHOVEL -> enumJob = Jobs.DIGGER;
                case DIAMOND_PICKAXE -> enumJob = Jobs.MINER;
                case GOLDEN_HOE -> enumJob = Jobs.FARMER;
                default -> enumJob = null;
            }

            if (e.getClick().isRightClick() && enumJob != null) {
                if (!confirmationMap.containsKey(p.getUniqueId())) {
                    confirmationMap.put(p.getUniqueId(), enumJob);
                    p.closeInventory();
                    p.openInventory(confirmationInv);
                }
            } else if (e.getClick().isLeftClick() && enumJob != null) {
                // Paused on implementation of the InfoMenu
                /*
                Job currentJob = JobsManager.getPlayerJob(p.getUniqueId());
                if (currentJob != null && currentJob.getEnumJob().equals(enumJob)) {
                    p.openInventory(jobInfoMenu.getInventory(p, currentJob));
                } else p.openInventory(jobInfoMenu.getInventory(p, enumJob));
                */
                p.sendMessage(Component.text("Information Menu Coming Soon! For more information about what each job does, feel free to ask!"));
            }
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onConfirmationInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().equals(confirmationInv)) {
            switch (e.getCurrentItem().getType()) {
                case EMERALD_BLOCK -> {
                    Jobs enumJob = confirmationMap.remove(p.getUniqueId());
                    p.closeInventory();
                    jobsManager.addPlayerToJob(p.getUniqueId(), enumJob, 0.0);
                    p.sendMessage(Component.text("Joined job: " + StringUtils.capitalize(enumJob.name().toLowerCase())));
                }
                case REDSTONE_BLOCK -> {
                    p.closeInventory();
                    confirmationMap.remove(p.getUniqueId());
                }
            }
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onConfirmationInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().equals(confirmationInv)) {
            confirmationMap.remove(e.getPlayer().getUniqueId());
            e.getPlayer().sendMessage(Utils.getPrefix("jobs").append(Component.text("Left confirmation screen")));
        }
    }

    public Inventory getInv() {
        return inv;
    }

    private Inventory getConfirmationInventory() {
        Inventory inventory = Bukkit.createInventory(null, 27,
                Component.text("Jobs Confirmation").color(TextColor.color(0, 255, 0)));
        List<Component> warningLore = List.of(
                Component.text("Joining a new job will RESET your progress. " +
                        "Do not continue if you wish to keep your progress.").color(TextColor.color(255, 0, 0)));
        Utils.createItem(inventory, Material.EMERALD_BLOCK, 1, 12, Component.text("Confirm").color(TextColor.color(0, 255, 0)), warningLore);
        Utils.createItem(inventory, Material.REDSTONE_BLOCK, 1, 16, Component.text("Go Back").color(TextColor.color(255, 128, 0)));
        return inventory;
    }

    private Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 27,
                Component.text("Jobs Menu").color(TextColor.color(128, 255, 0)));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(""));
        lore.add(Component.text("Right click to join job"));
        lore.add(Component.text("Left click for more info"));

        Utils.createItem(inventory, Material.WOODEN_AXE, 1, 11,
                Component.text("Woodcutter").color(TextColor.color(160, 160, 160)), lore);
        Utils.createItem(inventory, Material.FISHING_ROD, 1, 12,
                Component.text("Fisherman").color(TextColor.color(102, 178, 255)), lore);
        Utils.createItem(inventory, Material.ANVIL, 1, 13,
                Component.text("Blacksmith").color(TextColor.color(255, 102, 102)), lore);
        Utils.createItem(inventory, Material.BOW, 1, 14,
                Component.text("Hunter").color(TextColor.color(178, 102, 255)), lore);
        Utils.createItem(inventory, Material.ENCHANTED_BOOK, 1, 15,
                Component.text("Alchemist"), lore);
        Utils.createItem(inventory, Material.WOODEN_SHOVEL, 1, 16,
                Component.text("Digger").color(TextColor.color(0, 204, 0)), lore);
        Utils.createItem(inventory, Material.DIAMOND_PICKAXE, 1, 17,
                Component.text("Miner").color(TextColor.color(0, 204, 0)), lore);
        Utils.createItem(inventory, Material.GOLDEN_HOE, 1, 23,
                Component.text("Farmer").color(TextColor.color(102, 255, 102)), lore);
        Utils.makeDummySlots(inventory);
        return inventory;
    }

}

