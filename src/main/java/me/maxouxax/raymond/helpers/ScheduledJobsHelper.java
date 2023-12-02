package me.maxouxax.raymond.helpers;

import java.time.*;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledJobsHelper {

    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public static void scheduleAt(int hour, int minute, int seconds, Runnable job) {
        long delay = computeNextDelay(hour, minute, seconds);
        executorService.schedule(job, delay, TimeUnit.SECONDS);
    }

    public static void scheduleAtDays(int hour, int minute, int seconds, Set<DayOfWeek> days, Runnable job) {
        long delay = computeNextDelay(hour, minute, seconds, days);
        executorService.schedule(job, delay, TimeUnit.SECONDS);
    }

    private static long computeNextDelay(int targetHour, int targetMin, int targetSec) {
        return computeNextDelay(targetHour, targetMin, targetSec, null);
    }

    private static long computeNextDelay(int targetHour, int targetMin, int targetSec, Set<DayOfWeek> days) {
        if(days.isEmpty()) days = null;

        LocalDateTime localNow = LocalDateTime.now();
        ZoneId currentZone = ZoneId.systemDefault();
        ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
        ZonedDateTime zonedNextTarget = zonedNow.withHour(targetHour).withMinute(targetMin).withSecond(targetSec);

        while (days != null && !days.contains(zonedNextTarget.getDayOfWeek())) {
            // Adding one day to the current next date if the day is not in the list
            // Since the days Set is set to null if it's empty, this loop won't be infinite
            zonedNextTarget = zonedNextTarget.plusDays(1);
        }

        Duration duration = Duration.between(zonedNow, zonedNextTarget);
        return duration.getSeconds();
    }

    public static void stop() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
    }

}
