package edu.qc.seclass.glm;

import java.util.UUID;

public class AlarmSet {

    int id;
    UUID r_id;
    boolean isSet;

    public AlarmSet(int id, UUID r_id, boolean isSet) {
        this.id = id;
        this.r_id = r_id;
        this.isSet = isSet;

    }
}
