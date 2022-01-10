package com.eonnations.eonjobs.jobs;

import com.eonnations.eonjobs.EonJobs;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JobFileManager {

    EonJobs plugin;
    String basePath;

    public JobFileManager(EonJobs plugin) {
        this.plugin = plugin;
        basePath = plugin.getDataFolder().getPath() + File.separator + "jobs";
        createJobDirectory();
        createJobFiles();
    }



    private void createJobDirectory() {
        if (!new File(basePath).exists()) {
            try {
                Path path = Paths.get(basePath);
                Files.createDirectories(path);
                System.out.println("Created jobs directory!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createJobFiles() {
        for (Jobs job : Jobs.values()) {
            try {
                File file = new File(basePath + File.separator + job.name().toLowerCase() + ".yml");
                if (file.createNewFile()) {
                    System.out.println(job.name().toLowerCase() + ".yml has been created");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public double getPriceForAction(String action, Jobs job, String material) {
        FileConfiguration config = getConfigForJob(job);
        return config.getDouble(action + "." + material.toLowerCase() + ".income");
    }

    public double getExperienceForAction(String action, Jobs job, String material) {
        FileConfiguration config = getConfigForJob(job);
        return config.getDouble(action + "." + material.toLowerCase() + ".experience");
    }

    public Map<String, Double> getPricesFromAction(String action, Jobs job) {
        HashMap<String, Double> hashMap = new HashMap<>();
        FileConfiguration config = getConfigForJob(job);
        List<String> listString = config.getStringList(action);
        for (String value : listString) {
            double price = getPriceForAction(action, job, value);
            hashMap.put(value, price);
        }

        return hashMap;
    }

    private FileConfiguration getConfigForJob(Jobs job) {
        File file = new File(basePath + File.separator + job.name().toLowerCase() + ".yml");
        return YamlConfiguration.loadConfiguration(file);
    }
}
