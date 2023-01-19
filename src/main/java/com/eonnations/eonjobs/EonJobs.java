package com.eonnations.eonjobs;

import com.eonnations.eonjobs.commands.GiveExpCommand;
import com.eonnations.eonjobs.commands.JobMenuCommand;
import com.eonnations.eonjobs.commands.JobStatsCommand;
import com.eonnations.eonjobs.jobs.JobFileManager;
import com.eonnations.eonjobs.jobs.JobsManager;
import com.eonnations.eonjobs.listeners.JobsEventListener;
import com.eonnations.eonjobs.listeners.WorldInteractListener;
import com.eonnations.eonjobs.menus.JobInfoMenu;
import me.lucko.helper.config.ConfigurationNode;
import me.lucko.helper.plugin.HelperPlugin;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public final class EonJobs extends JavaPlugin implements HelperPlugin {
    LuckPerms luckPerms;
    Economy eco;

    @Override
    public void onEnable() {
        // Plugin startup logic
        registerLuckPerms();
        setupEconomy();
        JobsManager jobsManager = new JobsManager(this);
        registerCommands(jobsManager);
    }

    public void registerCommands(JobsManager jobsManager) {
        JobFileManager jobFileManager = new JobFileManager(this);
        JobInfoMenu jobInfoMenu = new JobInfoMenu(this, jobFileManager);
        new GiveExpCommand(this, jobsManager);
        new JobMenuCommand(this, jobInfoMenu, jobsManager);
        new JobStatsCommand(this, jobsManager);
        new JobsEventListener(this, jobFileManager, eco);
        new WorldInteractListener(this, jobsManager);
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

    @NotNull
    @Override
    public <T extends Listener> T registerListener(@NotNull T t) {
        Bukkit.getPluginManager().registerEvents(t, this);
        return t;
    }

    @NotNull
    @Override
    public <T extends CommandExecutor> T registerCommand(@NotNull T t, String s, String s1, String s2, @NotNull String... strings) {
        return null;
    }

    @NotNull
    @Override
    public <T> T getService(@NotNull Class<T> aClass) {
        return null;
    }

    @NotNull
    @Override
    public <T> T provideService(@NotNull Class<T> aClass, @NotNull T t, @NotNull ServicePriority servicePriority) {
        return null;
    }

    @NotNull
    @Override
    public <T> T provideService(@NotNull Class<T> aClass, @NotNull T t) {
        return null;
    }

    @Override
    public boolean isPluginPresent(@NotNull String s) {
        return false;
    }

    @Nullable
    @Override
    public <T> T getPlugin(@NotNull String s, @NotNull Class<T> aClass) {
        return null;
    }

    @NotNull
    @Override
    public File getBundledFile(@NotNull String s) {
        return null;
    }

    @NotNull
    @Override
    public YamlConfiguration loadConfig(@NotNull String s) {
        return null;
    }

    @NotNull
    @Override
    public ConfigurationNode loadConfigNode(@NotNull String s) {
        return null;
    }

    @NotNull
    @Override
    public <T> T setupConfig(@NotNull String s, @NotNull T t) {
        return null;
    }

    @NotNull
    @Override
    public ClassLoader getClassloader() {
        return null;
    }

    @NotNull
    @Override
    public <T extends AutoCloseable> T bind(@NotNull T t) {
        return null;
    }
}
