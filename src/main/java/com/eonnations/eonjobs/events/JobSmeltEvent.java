package com.eonnations.eonjobs.events;

import com.eonnations.eonjobs.jobs.Job;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class JobSmeltEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    Player p;
    Material material;
    int amount;
    Job job;

    public JobSmeltEvent(Player p, Material material, int amount, Job job) {
        super(true);
        this.p = p;
        this.material = material;
        this.amount = amount;
        this.job = job;
    }

    public Player getPlayer() {
        return p;
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public Job getJob() {
        return job;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
