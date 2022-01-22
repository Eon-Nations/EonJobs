package com.eonnations.eonjobs.events;

import com.eonnations.eonjobs.jobs.Job;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class JobBreakEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    Player p;
    int age;
    String name;
    World world;
    Job job;

    public JobBreakEvent(Player p, int age, String name, World world, Job job) {
        super(true);
        this.p = p;
        this.age = age;
        this.name = name;
        this.world = world;
        this.job = job;
    }

    public Player getPlayer() {
        return p;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public World getWorld() {
        return world;
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
