package com.eonnations.eonjobs.listeners;

import com.eonnations.eonjobs.EonJobs;
import com.eonnations.eonjobs.jobs.JobsManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {
    EonJobs plugin;
    JobsManager jobsManager;

    public JoinListener(EonJobs plugin, JobsManager jobsManager) {
        this.plugin = plugin;
        this.jobsManager = jobsManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        jobsManager.loadPlayer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (jobsManager.hasJob(e.getPlayer())) {
            jobsManager.savePlayer(e.getPlayer());
        }
    }
}
