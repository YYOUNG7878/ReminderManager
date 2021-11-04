package com.example.glm;

import java.util.Date;
import java.util.UUID;

public class Reminder {
    private UUID id;
    private UUID list_id;
    private String name;
    private String type;
    private Date date;
    private boolean checkoff;

    public Reminder(){
        id = UUID.randomUUID();
        date = new Date();
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
