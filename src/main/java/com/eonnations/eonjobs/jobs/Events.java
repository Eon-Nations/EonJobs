package com.eonnations.eonjobs.jobs;

public enum Events {
    BREAK,
    PLACE,
    FISH,
    KILL,
    BREED,
    MILK,
    SHEAR,
    ENCHANT,
    CRAFT,
    SMELT;

    public static Events[] getEventsFromJob(Jobs job) {
        switch (job) {
            case MINER, DIGGER, WOODCUTTER -> {
                Events[] events = new Events[1];
                events[0] = Events.BREAK;
                return events;
            }
            case FISHERMAN -> {
                Events[] events = new Events[1];
                events[0] = Events.FISH;
                return events;
            }
            case FARMER -> {
                Events[] events = new Events[5];
                events[0] = Events.BREAK;
                events[1] = Events.PLACE;
                events[2] = Events.BREED;
                events[3] = Events.MILK;
                events[4] = Events.SHEAR;
                return events;
            }
            case HUNTER -> {
                Events[] events = new Events[1];
                events[0] = Events.KILL;
                return events;
            }
            case ENCHANTER -> {
                Events[] events = new Events[1];
                events[0] = Events.ENCHANT;
                return events;
            }
            case BLACKSMITH -> {
                Events[] events = new Events[2];
                events[0] = Events.CRAFT;
                events[1] = Events.SMELT;
                return events;
            }
        }
        return null;
    }
}
