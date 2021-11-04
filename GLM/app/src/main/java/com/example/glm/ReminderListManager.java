package com.example.glm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReminderListManager {
    public static List<ReminderList> RLs = new ArrayList<>();

    public ReminderListManager(){
        RLs.add(new ReminderList());
    }

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

}
