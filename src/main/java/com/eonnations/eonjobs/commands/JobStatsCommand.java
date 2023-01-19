package com.eonnations.eonjobs.commands;

import com.eonnations.eonjobs.EonJobs;
import com.eonnations.eonjobs.jobs.Job;
import com.eonnations.eonjobs.jobs.JobsManager;
import com.eonnations.eonjobs.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JobStatsCommand implements CommandExecutor {

    EonJobs plugin;
    JobsManager jobsManager;

    public JobStatsCommand(EonJobs plugin, JobsManager jobsManager) {
        this.plugin = plugin;
        this.jobsManager = jobsManager;
        plugin.getCommand("jobstats").setExecutor(this);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) commandSender;
        Job job = jobsManager.getPlayerJob(p.getUniqueId());

        TextColor color = TextColor.color(160, 160, 160);

        p.sendMessage(Utils.getPrefix("nations"));
        p.sendMessage(Component.text("Job: " + StringUtils.capitalize(job.getEnumJob().name().toLowerCase()))
                .color(color));
        p.sendMessage(Component.text("Level: " + job.getLevel()).color(color));
        p.sendMessage(Component.text("Total Experience: " + Utils.round(job.getExp(), 2))
                .color(color));
        p.sendMessage(Component.text("Experience Needed for Next Level: " +
                        Utils.round(getNeededExperience(job), 2)).color(color));

        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
        return true;
    }

    private double getNeededExperience(Job job) {
        return Math.pow(job.getLevel() + 1, 2) / 0.0625;
    }
}
