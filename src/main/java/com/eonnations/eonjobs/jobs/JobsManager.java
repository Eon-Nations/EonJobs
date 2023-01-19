package com.eonnations.eonjobs.jobs;

import com.eonnations.eonjobs.EonJobs;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * A manager class for handling player jobs and experience using Jedis.
 */
public class JobsManager {

    private final JedisPool jedisPool;
    private final EonJobs plugin;

    /**
     * Constructs a new JobsManager with the given JedisPool and plugin.
     *
     * @param plugin the EonJobs plugin instance
     */
    public JobsManager(EonJobs plugin) {
        this.jedisPool = setupPool();
        this.plugin = plugin;
    }

    public JobsManager(JedisPool pool, EonJobs plugin) {
        this.jedisPool = pool;
        this.plugin = plugin;
    }

    /**
     * Gets the player's job and experience based on their UUID
     * @return the Job object of the player, Optional.empty() if not found
     */
    private JedisPool setupPool() {
        String serverURL = Optional.ofNullable(plugin.getConfig().getString("redis-url")).orElse("redis://localhost:6709");
        return new JedisPool(serverURL);
    }

    /**
     * Gets the player's job and experience based on their UUID
     * @param uuid player's UUID
     * @return the Job object of the player, Optional.empty() if not found
     */
    public Job getPlayerJob(UUID uuid) {
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> jobData = jedis.hgetAll("playerJobs:" + uuid.toString());
            if (jobData.isEmpty()) {
                return Job.defaultJob();
            }
            String job = jobData.get("job");
            double experience = Double.parseDouble(jobData.get("experience"));
            return new Job(Jobs.valueOf(job), experience);
        }
    }

    /**
     * Adds the player to the given job with the given experience
     * @param uuid player's UUID
     * @param enumJob the job to add the player to
     * @param experience the experience of the player in the job
     */
    public void addPlayerToJob(UUID uuid, Jobs enumJob, double experience) {
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> jobData = new HashMap<>();
            jobData.put("job", enumJob.name());
            jobData.put("experience", Double.toString(experience));
            jedis.hmset("playerJobs:" + uuid.toString(), jobData);
        }
    }

    /**
     * Check if a player has a job
     * @param p player
     * @return true if the player has a job, false otherwise
     */
    public boolean hasJob(Player p) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists("playerJobs:" + p.getUniqueId().toString());
        }
    }
}
