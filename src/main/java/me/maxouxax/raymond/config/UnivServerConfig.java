package me.maxouxax.raymond.config;

import me.maxouxax.raymond.schedule.UnivSchedule;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class UnivServerConfig {

    @BsonProperty("univ_schedule")
    private UnivSchedule univSchedule;

    public UnivServerConfig() {
        this.univSchedule = new UnivSchedule();
    }

    @BsonCreator
    public UnivServerConfig(@BsonProperty("univ_schedule") final UnivSchedule univSchedule) {
        this.univSchedule = univSchedule;
    }

    public UnivSchedule getUnivSchedule() {
        return univSchedule;
    }

    public void setUnivSchedule(UnivSchedule univSchedule) {
        this.univSchedule = univSchedule;
    }

}
