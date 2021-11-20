package edu.qc.seclass.glm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ReminderListManager {
    public static List<ReminderList> RLs;
    public static String[] types = {"Appointment", "Event", "Meeting", "Task"};

    private Context mContext;
    private SQLiteDatabase mDB;

    public ReminderListManager(Context context){
        mContext = context.getApplicationContext();
        mDB = new ReminderBaseHelper(mContext).getWritableDatabase();

        RLs = new ArrayList<>();
        //RLs.add(new ReminderList());
    }

    //public static ReminderListManager getManager() { return this;}

    public List<ReminderList> getReminderLists(){

        List<ReminderList> rls = new ArrayList<>();
        Cursor cursor = mDB.query("reminderlists", null, null, null, null, null, null);
        while(cursor.moveToNext()){
            UUID id =  UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("rl_id")));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("rl_name"));
            ReminderList rl = new ReminderList(id, name);
            rls.add(rl);
        }
        cursor.close();
        return rls;

        //return RLs;
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
        ContentValues addvalues = new ContentValues();
        addvalues.put("rl_id", rl.getId().toString());
        addvalues.put("rl_name", rl.getName());
        mDB.insert("reminderlists", null, addvalues);
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
