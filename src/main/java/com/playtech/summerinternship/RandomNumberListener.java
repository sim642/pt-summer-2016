package com.playtech.summerinternship;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class RandomNumberListener implements ServletContextListener {
    private static final Random RANDOM = new Random();

    private final SortedMap<Long, Long> numbersByTimestamp = Collections.synchronizedSortedMap(new TreeMap<>());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().setAttribute("random", this); // add itself to context for access in service

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(() -> {
            numbersByTimestamp.put(Instant.now().getEpochSecond(), RANDOM.nextLong()); // synchronized implicitly
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    public List<Long> getAfter(long after) {
        synchronized (numbersByTimestamp) { // better safe than sorry, avoid relying on implementation detail of Synchronized*
            return new ArrayList<>(numbersByTimestamp.tailMap(after).values()); // synchronized implicitly (?)
        }
    }
}
