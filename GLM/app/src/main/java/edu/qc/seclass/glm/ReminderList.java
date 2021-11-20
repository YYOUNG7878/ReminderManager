package edu.qc.seclass.glm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ReminderList {
    private UUID id;
    private String name;

    public ReminderList(){
        id = UUID.randomUUID();
        name = "New ReminderList";
    }

    public ReminderList(UUID rl_id, String rl_name){
        id = rl_id;
        name = rl_name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
