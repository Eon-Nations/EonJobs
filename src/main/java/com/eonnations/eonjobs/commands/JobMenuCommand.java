package com.eonnations.eonjobs.commands;

import com.eonnations.eonjobs.EonJobs;
import com.eonnations.eonjobs.jobs.JobsManager;
import com.eonnations.eonjobs.menus.JobChoiceMenu;
import com.eonnations.eonjobs.menus.JobInfoMenu;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JobMenuCommand implements CommandExecutor {

    EonJobs plugin;
    JobChoiceMenu jobChoiceMenu;

    public JobMenuCommand(EonJobs plugin, JobInfoMenu jobInfoMenu, JobsManager jobsManager) {
        jobChoiceMenu = new JobChoiceMenu(plugin, jobInfoMenu, jobsManager);
        this.plugin = plugin;
        plugin.getCommand("jobmenu").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player p) {
            p.openInventory(jobChoiceMenu.getInv());
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
        }
        return true;
    }
}
