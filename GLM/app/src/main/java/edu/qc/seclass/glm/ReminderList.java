package edu.qc.seclass.glm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ReminderList {
    private UUID id;
    private String name;
    private List<Reminder> reminders;

    public ReminderList(){
        id = UUID.randomUUID();
        name = "New ReminderList";
        reminders = new ArrayList<>();
    }

    public ReminderList(String thisName, List<Reminder> theseReminders){
        name = "Sample Reminder";
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
        Iterator<Reminder> iter = reminders.iterator();
        while(iter.hasNext()){
            Reminder item = iter.next();
            if(item.getId().equals(reminderId)){
                iter.remove();
            }
        }
    }
}
