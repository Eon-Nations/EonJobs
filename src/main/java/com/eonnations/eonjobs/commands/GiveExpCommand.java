package com.eonnations.eonjobs.commands;

import com.eonnations.eonjobs.EonJobs;
import com.eonnations.eonjobs.jobs.Job;
import com.eonnations.eonjobs.jobs.JobsManager;
import com.eonnations.eonjobs.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GiveExpCommand implements CommandExecutor {

    EonJobs plugin;
    JobsManager jobsManager;

    public GiveExpCommand(EonJobs plugin, JobsManager jobsManager) {
        this.plugin = plugin;
        this.jobsManager = jobsManager;
        plugin.getCommand("givejobexp").setExecutor(this);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            double exp = Double.parseDouble(args[1]);

            if (target != null) {
                Job job = jobsManager.getPlayerJob(target.getUniqueId());
                job.addExp(exp);
                sender.sendMessage(Utils.getPrefix("jobs").append(
                        Component.text("Given " + target.getName() + " " + exp + " experience.")));
                target.sendMessage(Utils.getPrefix("jobs").append(
                        Component.text("You have been given " + exp + " experience by " + sender.getName())));
            }
        } else sender.sendMessage(Component.text("Invalid arguments. Usage: /givejobexp <player> <amount>"));
        return true;
    }
}
