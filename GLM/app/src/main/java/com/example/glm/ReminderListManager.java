package com.example.glm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ReminderListManager {
    public static List<ReminderList> RLs;
    public static String[] types = {"Appointment", "Event", "Meeting", "Task"};

    public ReminderListManager(){
        RLs = new ArrayList<>();
        RLs.add(new ReminderList());
    }

    //public static ReminderListManager getManager() { return this;}

    public List<ReminderList> getReminderLists(){
        return RLs;
    }

    public static ReminderList getList(UUID id){
        for (ReminderList RL : RLs){
            if(RL.getId().equals(id)){
                return RL;
            }
        }

        return null;
    }

    public void addReminderList(ReminderList rl){
        RLs.add(rl);
    }

    public static void deleteReminderList(UUID reminderlistId){
        Iterator<ReminderList> iter = RLs.iterator();
        while(iter.hasNext()){
            ReminderList item = iter.next();
            if(item.getId().equals(reminderlistId)){
                iter.remove();
            }
        }
    }

}
