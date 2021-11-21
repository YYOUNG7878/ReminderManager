package edu.qc.seclass.glm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "reminderBase.db";

    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table reminderlists (rl_id varchar primary key, rl_name varchar)");
        db.execSQL("create table reminders (r_id varchar primary key, rl_id varchar, r_name varchar, r_type varchar, r_repeat varchar, r_date int, r_checkoff int)");
        db.execSQL("create table types (t_name varchar primary key)");
        db.execSQL("create table alarms (a_id int primary key)");
        db.execSQL("insert into types (t_name) values ('Appointment')");
        db.execSQL("insert into types (t_name) values ('Event')");
        db.execSQL("insert into types (t_name) values ('Meeting')");
        db.execSQL("insert into types (t_name) values ('Task')");
        db.execSQL("insert into alarms (a_id) values (1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}