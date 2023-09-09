package me.maxouxax.raymond.schedule;

import me.maxouxax.multi4j.schedule.UnivClass;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.ArrayList;
import java.util.List;

public class UnivSchedule {

    @BsonProperty("current_week_classes")
    private final List<UnivClass> currentWeekClasses;

    @BsonProperty("next_week_classes")
    private final List<UnivClass> nextWeekClasses;

    @BsonCreator
    public UnivSchedule(@BsonProperty("current_week_classes") final List<UnivClass> currentWeekClasses,
                        @BsonProperty("next_week_classes") final List<UnivClass> nextWeekClasses) {
        this.currentWeekClasses = currentWeekClasses;
        this.nextWeekClasses = nextWeekClasses;
    }

    public UnivSchedule() {
        this.currentWeekClasses = new ArrayList<>();
        this.nextWeekClasses = new ArrayList<>();
    }

    public List<UnivClass> getCurrentWeekClasses() {
        return currentWeekClasses;
    }

    public List<UnivClass> getNextWeekClasses() {
        return nextWeekClasses;
    }

}
