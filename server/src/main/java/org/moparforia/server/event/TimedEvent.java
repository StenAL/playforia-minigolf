package org.moparforia.server.event;

import org.moparforia.server.Server;

public abstract class TimedEvent extends Event {

    private final long time;

    public TimedEvent(long milliseconds) {
        this.time = System.currentTimeMillis() + milliseconds;
    }

    @Override
    public boolean shouldProcess(Server server) {
        return System.currentTimeMillis() > time;
    }

    @Override
    public abstract void process(Server server);

}
