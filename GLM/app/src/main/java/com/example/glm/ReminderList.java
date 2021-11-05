package com.example.glm;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReminderList {
    private UUID id;
    private String name;
    private List<Reminder> reminders;

    public ReminderList(){
        id = UUID.randomUUID();
        name = "Reminder List 1";
        reminders = new ArrayList<>();
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

    public List<Reminder> getReminders(){
        return reminders;
    }

    public Reminder getReminder(UUID id){
        for(Reminder reminder : reminders){
            if(reminder.getId().equals(id)){
                return reminder;
            }
        }
        return null;
    }

    public void addReminder(Reminder r){
        reminders.add(r);
        r.setList_id(this.id);
    }

    public void deleteReminder(UUID reminderId){
        for(Reminder reminder : reminders){
            if(reminder.getId().equals(reminderId)){
                reminders.remove(reminder);
            }
        }
    }
}
