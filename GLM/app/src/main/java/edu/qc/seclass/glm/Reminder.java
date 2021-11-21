package edu.qc.seclass.glm;

import java.util.Date;
import java.util.UUID;

public class Reminder {
    private UUID id;
    private UUID list_id;
    private String name;
    private String type;
    private String repeat;
    private Date date;
    private Boolean checkoff;

    public Reminder(UUID r_list_id){
        id = UUID.randomUUID();
        list_id = r_list_id;
        name = "new Reminder";
        type = "Appointment";
        repeat = "Never";
        date = new Date();
        checkoff = false;
    }

    public Reminder(UUID r_id, UUID r_list_id, String r_name, String r_type, String r_repeat, Date r_date, Boolean r_checkoff){
        id = r_id;
        list_id = r_list_id;
        name = r_name;
        type = r_type;
        repeat = r_repeat;
        date = r_date;
        checkoff = r_checkoff;
    }

    public UUID getId() {
        return id;
    }

    public UUID getList_id() {
        return list_id;
    }

    public void setList_id(UUID list_id) {
        this.list_id = list_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isCheckoff() {
        return checkoff;
    }

    public void setCheckoff(boolean checkoff) {
        this.checkoff = checkoff;
    }



}
