package com.eonnations.eonjobs.events;

import com.eonnations.eonjobs.jobs.Job;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class JobCraftEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    Player p;
    int amount;
    Job job;
    Material type;

    public JobCraftEvent(Player p, int amount, Material type) {
        super(true);
        this.p = p;
        this.amount = amount;
        this.type = type;
    }

    public Player getPlayer() {
        return p;
    }

    public int getAmount() {
        return amount;
    }

    public Material getType() {
        return type;
    }

    public Job getJob() { return job; }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
