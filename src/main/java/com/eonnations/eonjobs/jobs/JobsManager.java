package com.eonnations.eonjobs.jobs;

import com.eonnations.eonjobs.EonJobs;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class JobsManager {

    HashMap<UUID, Job> playerJobs;
    LuckPerms luckPerms;
    EonJobs plugin;

    public JobsManager(EonJobs plugin, LuckPerms luckPerms) {
        playerJobs = new HashMap<>();
        this.luckPerms = luckPerms;
        this.plugin = plugin;
    }

    public Job getPlayerJob(UUID uuid) {
        return playerJobs.get(uuid);
    }

    public List<UUID> getPlayersInJob(Jobs job) {
        List<UUID> toReturn = new ArrayList<>();
        for (UUID uuid : playerJobs.keySet()) {
            if (playerJobs.get(uuid).getEnumJob().equals(job)) {
                toReturn.add(uuid);
            }
        }
        return toReturn;
    }

    public void addPlayerToJob(UUID uuid, Jobs enumJob) {
        Job job = new Job(enumJob);
        playerJobs.put(uuid, job);
    }

    public void loadPlayer(Player p) {
        CachedMetaData metaData = luckPerms.getPlayerAdapter(Player.class).getMetaData(p);
        String jobString = metaData.getMetaValue("job");
        if (jobString != null) {
            Job job = Job.fromString(jobString);
            playerJobs.put(p.getUniqueId(), job);
        }
    }

    public void savePlayer(Player p) {
        luckPerms.getUserManager().modifyUser(p.getUniqueId(), user -> {
            Job job = playerJobs.remove(p.getUniqueId());
            MetaNode node = MetaNode.builder("job", job.toString()).build();
            user.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals("job")));
            user.data().add(node);
        });
    }

    public boolean hasJob(Player p) {
        if (playerJobs.containsKey(p.getUniqueId()))
            return true;
        else {
            CachedMetaData metaData = luckPerms.getPlayerAdapter(Player.class).getMetaData(p);
            return metaData.getMetaValue("job") != null;
        }
    }
}
