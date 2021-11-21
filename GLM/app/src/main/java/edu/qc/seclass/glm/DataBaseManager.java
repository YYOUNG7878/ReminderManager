package edu.qc.seclass.glm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DataBaseManager {
    private static DataBaseManager sDataBaseManager;

    private Context mContext;
    private SQLiteDatabase mDB;


    //Constructor
    public DataBaseManager(Context context){
        mContext = context.getApplicationContext();
        mDB = new DataBaseHelper(mContext).getWritableDatabase();
    }

    //To make sure that there only one static DataBaseManager
    public static DataBaseManager get(Context context) {
        if (sDataBaseManager == null) {
            sDataBaseManager = new DataBaseManager(context);
        }
        return sDataBaseManager;
    }

    /*
    Below are functions of DataBaseManager
     */
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
    }

    public void addReminderList(ReminderList rl){
        ContentValues addvalues = new ContentValues();
        addvalues.put("rl_id", rl.getId().toString());
        addvalues.put("rl_name", rl.getName());
        mDB.insert("reminderlists", null, addvalues);
    }

    public void renameReminderList(String rl_name, UUID rl_id){
        ContentValues values = new ContentValues();
        values.put("rl_name", rl_name);
        String whereClause = "rl_id" + "=?";
        String whereArgs[] = new String[]{rl_id.toString()};
        mDB.update("reminderlists", values, whereClause, whereArgs);
    }

    public void deleteReminderList(UUID rl_id){
        String whereClause = "rl_id" + "=?";
        String whereArgs[] = new String[]{rl_id.toString()};
        mDB.delete("reminderlists", whereClause, whereArgs);
        mDB.delete("reminders", whereClause, whereArgs);
    }

    public String[] reminderlistNames(){
        List<String> rl_names = new ArrayList<>();
        String rawquery = "select * from reminderlists;";
        Cursor cursor = mDB.rawQuery(rawquery,null);
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndexOrThrow("rl_name"));
            rl_names.add(name);
        }
        cursor.close();

        String[] names = new String[rl_names.size()];
        for(int i = 0; i < names.length; i++){
            names[i] = rl_names.get(i).toString();
        }
        return names;
    }

    public String[] reminderlistIds(){
        List<String> rl_ids = new ArrayList<>();
        String rawquery = "select * from reminderlists;";
        Cursor cursor = mDB.rawQuery(rawquery,null);
        while(cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndexOrThrow("rl_id"));
            rl_ids.add(id);
        }
        cursor.close();

        String[] ids = new String[rl_ids.size()];
        for(int i = 0; i < ids.length; i++){
            ids[i] = rl_ids.get(i).toString();
        }
        return ids;
    }

    public List<Reminder> getReminders(UUID rl_id){
        List<Reminder> rs = new ArrayList<>();

        String rawquery = "select * from reminders where rl_id = " + "'" + rl_id.toString() + "'" +";";
        Cursor cursor = mDB.rawQuery(rawquery,null);
        while(cursor.moveToNext()){
            UUID id =  UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("r_id")));
            UUID list_id = UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("rl_id")));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("r_name"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("r_type"));
            String repeat = cursor.getString(cursor.getColumnIndexOrThrow("r_repeat"));
            Date date = new Date(cursor.getLong(cursor.getColumnIndexOrThrow("r_date")));
            Boolean check_off = false;
            if(cursor.getInt(cursor.getColumnIndexOrThrow("r_checkoff")) == 1){
                check_off = true;
            }
            Reminder r = new Reminder(id, list_id, name, type, repeat, date, check_off);
            rs.add(r);
        }
        cursor.close();
        return rs;
    }

    public List<Reminder> getRemindersByType(String r_type){
        List<Reminder> rs = new ArrayList<>();

        String rawquery = "select * from reminders where r_type = " + "'" + r_type + "'" +";";
        Cursor cursor = mDB.rawQuery(rawquery,null);
        while(cursor.moveToNext()){
            UUID id =  UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("r_id")));
            UUID list_id = UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("rl_id")));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("r_name"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("r_type"));
            String repeat = cursor.getString(cursor.getColumnIndexOrThrow("r_repeat"));
            Date date = new Date(cursor.getLong(cursor.getColumnIndexOrThrow("r_date")));
            Boolean check_off = false;
            if(cursor.getInt(cursor.getColumnIndexOrThrow("r_checkoff")) == 1){
                check_off = true;
            }
            Reminder r = new Reminder(id, list_id, name, type, repeat, date, check_off);
            rs.add(r);
        }
        cursor.close();
        return rs;
    }

    public Reminder getReminder(UUID r_id){
        String rawquery = "select * from reminders where r_id = " + "'" + r_id.toString() + "'" +";";
        Cursor cursor = mDB.rawQuery(rawquery,null);
        while(cursor.moveToNext()){
            UUID id =  UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("r_id")));
            UUID list_id = UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("rl_id")));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("r_name"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("r_type"));
            String repeat = cursor.getString(cursor.getColumnIndexOrThrow("r_repeat"));
            Date date = new Date(cursor.getLong(cursor.getColumnIndexOrThrow("r_date")));
            Boolean check_off = false;
            if(cursor.getInt(cursor.getColumnIndexOrThrow("r_checkoff")) == 1){
                check_off = true;
            }
            Reminder r = new Reminder(id, list_id, name, type, repeat, date, check_off);
            return r;
        }
        cursor.close();

        return null;
    }

    public void addReminder(Reminder r){
        ContentValues values = new ContentValues();
        values.put("r_id", r.getId().toString());
        values.put("rl_id", r.getList_id().toString());
        values.put("r_name", r.getName());
        values.put("r_type", r.getType());
        values.put("r_repeat", r.getRepeat());
        values.put("r_date", r.getDate().getTime());
        values.put("r_checkoff", (r.isCheckoff())? 1 : 0);
        mDB.insert("reminders", null, values);
    }

    public Reminder addReminderByType(String typeName){
        UUID list_id;
        String rawquery = "select * from reminderlists where rl_name = " + "'(unclassified reminders)'" +";";
        Cursor cursor = mDB.rawQuery(rawquery,null);
        if(cursor.getCount() > 0){
            cursor.moveToNext();
            list_id = UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("rl_id")));
        } else {
            ReminderList reminderList = new ReminderList();
            reminderList.setName("(unclassified reminders)");
            addReminderList(reminderList);
            list_id = reminderList.getId();
        }
        cursor.close();

        Reminder reminder = new Reminder(list_id);
        reminder.setType(typeName);
        addReminder(reminder);

        return reminder;
    }

    public void deleteReminder(UUID r_id){
        String whereClause = "r_id" + "=?";
        String whereArgs[] = new String[]{r_id.toString()};
        mDB.delete("reminders", whereClause, whereArgs);
    }

    public void updateReminder(Reminder r){
        ContentValues values = new ContentValues();
        values.put("r_name", r.getName());
        values.put("r_type", r.getType());
        values.put("r_repeat", r.getRepeat());
        values.put("r_date", r.getDate().getTime());
        values.put("r_checkoff", (r.isCheckoff())? 1 : 0);
        String whereClause = "r_id" + "=?";
        String whereArgs[] = new String[]{r.getId().toString()};
        mDB.update("reminders", values, whereClause, whereArgs);
    }

    public void updateCheckOff(Reminder r){
        ContentValues values = new ContentValues();
        values.put("r_checkoff", (r.isCheckoff())? 1 : 0);
        String whereClause = "r_id" + "=?";
        String whereArgs[] = new String[]{r.getId().toString()};
        mDB.update("reminders", values, whereClause, whereArgs);
    }

    public String[] reminderNames(UUID rl_id){
        List<String> r_names = new ArrayList<>();
        String rawquery = "select * from reminders where rl_id = " + "'" + rl_id.toString() + "'" +";";
        Cursor cursor = mDB.rawQuery(rawquery,null);
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndexOrThrow("r_name"));
            r_names.add(name);
        }
        cursor.close();

        String[] names = new String[r_names.size()];
        for(int i = 0; i < names.length; i++){
            names[i] = r_names.get(i);
        }
        return names;
    }

    public String[] reminderNamesByType(String typeName){
        List<String> r_names = new ArrayList<>();
        String rawquery = "select * from reminders where r_type = " + "'" + typeName + "'" +";";
        Cursor cursor = mDB.rawQuery(rawquery,null);
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndexOrThrow("r_name"));
            r_names.add(name);
        }
        cursor.close();

        String[] names = new String[r_names.size()];
        for(int i = 0; i < names.length; i++){
            names[i] = r_names.get(i);
        }
        return names;
    }

    public void changeListId(String r_id, String rl_id){
        ContentValues values = new ContentValues();
        values.put("rl_id", rl_id);
        String whereClause = "r_id" + "=?";
        String whereArgs[] = new String[]{r_id};
        mDB.update("reminders", values, whereClause, whereArgs);
    }

    public void changeType(String r_id, String typeName){
        ContentValues values = new ContentValues();
        values.put("r_type", typeName);
        String whereClause = "r_id" + "=?";
        String whereArgs[] = new String[]{r_id};
        mDB.update("reminders", values, whereClause, whereArgs);
    }

    public void clearCheckOff(UUID rl_id){
        ContentValues values = new ContentValues();
        values.put("r_checkoff", 0);
        String whereClause = "rl_id" + "=?";
        String whereArgs[] = new String[]{rl_id.toString()};
        mDB.update("reminders", values, whereClause, whereArgs);
    }

    public void clearCheckOffByType(String typeName){
        ContentValues values = new ContentValues();
        values.put("r_checkoff", 0);
        String whereClause = "r_type" + "=?";
        String whereArgs[] = new String[]{typeName};
        mDB.update("reminders", values, whereClause, whereArgs);
    }

    public List<String> getTypes_arrlist(){
        List<String> types = new ArrayList<>();

        String rawquery = "select * from types;";
        Cursor cursor = mDB.rawQuery(rawquery,null);
        while(cursor.moveToNext()){
            String t_name = cursor.getString(cursor.getColumnIndexOrThrow("t_name"));
            types.add(t_name);
        }
        cursor.close();
        return types;
    }

    public String[] getTypes(){
        List<String> types = new ArrayList<>();

        String rawquery = "select * from types;";
        Cursor cursor = mDB.rawQuery(rawquery,null);
        while(cursor.moveToNext()){
            String t_name = cursor.getString(cursor.getColumnIndexOrThrow("t_name"));
            types.add(t_name);
        }
        cursor.close();

        String[] type_arr = new String[types.size()];
        for(int i = 0; i < types.size(); i++){
            type_arr[i] = types.get(i);
        }
        return type_arr;
    }

    public boolean addType(String newType){
        String rawquery = "select * from types where t_name = " + "'" + newType + "';";
        Cursor cursor = mDB.rawQuery(rawquery, null);
        if(cursor.getCount() != 0){
            return false;
        } else {
            ContentValues values = new ContentValues();
            values.put("t_name", newType);
            mDB.insert("types", null, values);
            return true;
        }
    }

    public boolean renameType(String old_name, String new_name){
        String rawquery = "select * from types where t_name = " + "'" + new_name + "';";
        Cursor cursor = mDB.rawQuery(rawquery, null);
        if(cursor.getCount() != 0) {
            return false;
        } else {
            ContentValues values = new ContentValues();
            values.put("t_name", new_name);
            String whereClause = "t_name" + "=?";
            String whereArgs[] = new String[]{old_name};
            mDB.update("types", values, whereClause, whereArgs);

            ContentValues values2 = new ContentValues();
            values2.put("r_type", new_name);
            String whereClause2 = "r_type" + "=?";
            String whereArgs2[] = new String[]{old_name};
            mDB.update("reminders", values2, whereClause2, whereArgs2);
            return true;
        }
    }

    public void deleteType(String typeName){
        String whereClause = "t_name" + "=?";
        String whereArgs[] = new String[]{typeName};
        mDB.delete("types", whereClause, whereArgs);
        String whereClause2 = "r_type" + "=?";
        String whereArgs2[] = new String[]{typeName};
        mDB.delete("reminders", whereClause2, whereArgs2);
    }

    public List<Reminder> getBySearch(String keyword){
        List<Reminder> rs = new ArrayList<>();
        String rawquery = "select * from reminders where r_name like " + "'%" + keyword + "%' or r_type like '%" + keyword + "%';";
        Cursor cursor = mDB.rawQuery(rawquery, null);
        while(cursor.moveToNext()){
            UUID id =  UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("r_id")));
            UUID list_id = UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("rl_id")));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("r_name"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("r_type"));
            String repeat = cursor.getString(cursor.getColumnIndexOrThrow("r_repeat"));
            Date date = new Date(cursor.getLong(cursor.getColumnIndexOrThrow("r_date")));
            Boolean check_off = false;
            if(cursor.getInt(cursor.getColumnIndexOrThrow("r_checkoff")) == 1){
                check_off = true;
            }
            Reminder r = new Reminder(id, list_id, name, type, repeat, date, check_off);
            rs.add(r);
        }
        cursor.close();
        return rs;
    }

    public int getAlarmCount() {
        String rawquery = "select * from alarms;";
        Cursor cursor = mDB.rawQuery(rawquery, null);
        cursor.moveToNext();
        int count = cursor.getInt(cursor.getColumnIndexOrThrow("a_id"));
        return count;
    }

    public void updateAlarmCount(int i) {
        int count = i+1;
        ContentValues values = new ContentValues();
        values.put("a_id", count);
        String whereClause = "a_id" + "=" + i;
        mDB.update("alarms", values, whereClause, null);
    }
}
