package com.eonnations.eonjobs;

import com.eonnations.eonjobs.commands.GiveExpCommand;
import com.eonnations.eonjobs.commands.JobMenuCommand;
import com.eonnations.eonjobs.commands.JobStatsCommand;
import com.eonnations.eonjobs.jobs.JobFileManager;
import com.eonnations.eonjobs.jobs.JobsManager;
import com.eonnations.eonjobs.listeners.JobsEventListener;
import com.eonnations.eonjobs.listeners.JoinListener;
import com.eonnations.eonjobs.listeners.WorldInteractListener;
import com.eonnations.eonjobs.menus.JobInfoMenu;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class EonJobs extends JavaPlugin {
    LuckPerms luckPerms;
    Economy eco;

    @Override
    public void onEnable() {
        // Plugin startup logic
        registerLuckPerms();
        setupEconomy();
        JobsManager jobsManager = new JobsManager(this, luckPerms);
        JobFileManager jobFileManager = new JobFileManager(this);
        JobInfoMenu jobInfoMenu = new JobInfoMenu(this, jobFileManager);
        new GiveExpCommand(this, jobsManager);
        new JobMenuCommand(this, jobInfoMenu, jobsManager);
        new JobStatsCommand(this, jobsManager);
        new JobsEventListener(this, jobFileManager, eco);
        new JoinListener(this, jobsManager);
        new WorldInteractListener(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerLuckPerms() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
        }
    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().warning("Vault not found. Shutting down.");
            getServer().getPluginManager().disablePlugin(this);
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            eco = rsp.getProvider();
        } else {
            getLogger().warning("Vault not found. Shutting down.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }
}
