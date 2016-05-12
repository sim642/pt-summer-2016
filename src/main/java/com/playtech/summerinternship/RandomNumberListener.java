package com.playtech.summerinternship;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@WebListener
public class RandomNumberListener implements ServletContextListener {
    private static final Random RANDOM = new Random();

    private final SortedMap<Long, Long> numbersByTimestamp;

    public RandomNumberListener() {
        this.numbersByTimestamp = new TreeMap<>();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().setAttribute("random", this); // add itself to context for access in service

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(() -> {
            numbersByTimestamp.put(Instant.now().getEpochSecond(), RANDOM.nextLong());
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    public List<Long> getAfter(long after) {
        return numbersByTimestamp.entrySet().stream()
                .filter(longLongEntry -> longLongEntry.getKey() > after)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
