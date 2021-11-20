package edu.qc.seclass.glm;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ReminderListActivity extends AppCompatActivity{

    private UUID mListId;
    private String mListName;
    private List<Reminder> mReminders;
    private List<String> mListIds;
    private RecyclerView recyclerView;
    private ReminderAdapter mAdapter;

    private SQLiteDatabase mDB;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDB = new ReminderBaseHelper(this).getWritableDatabase();

        setContentView(R.layout.reminder_recycler);

        Intent i = getIntent();
        mListId = (UUID) i.getSerializableExtra("reminderlist_id");
        mListName = (String) i.getSerializableExtra("reminderlist_name");

        mReminders = getReminders(mListId);

        this.setTitle(mListName);

        recyclerView = (RecyclerView) findViewById(R.id.reminder_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ReminderAdapter(mReminders, this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume(){
        super.onResume();

        mDB = new ReminderBaseHelper(this).getWritableDatabase();

        setContentView(R.layout.reminder_recycler);

        Intent i = getIntent();
        mListId = (UUID) i.getSerializableExtra("reminderlist_id");
        mListName = (String) i.getSerializableExtra("reminderlist_name");

        mReminders = getReminders(mListId);

        this.setTitle(mListName);

        recyclerView = (RecyclerView) findViewById(R.id.reminder_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ReminderAdapter(mReminders, this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reminder_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_reminder:
                Reminder reminder = new Reminder(mListId);
                addReminder_db(reminder);

                Intent intent = new Intent();
                intent.setClass(this, AddReminderActivity.class);
                intent.putExtra("reminderlist_id", reminder.getList_id());
                intent.putExtra("reminder_id", reminder.getId());
                startActivity(intent);

                mReminders.add(reminder);
                mAdapter.updateReminderList(mReminders);
                recyclerView.setAdapter(mAdapter);
                return true;

            case R.id.rename_reminderlist:
                EditText editText = new EditText(this);
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Enter a new name:").setView(editText);
                dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getSupportActionBar().setTitle(editText.getText().toString());
                        renameReminderList_db(editText.getText().toString(), mListId);
                        Toast.makeText(getApplicationContext(), "succeed!", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
                return true;

            case R.id.delete_reminderlist:
                AlertDialog.Builder dialog_2 = new AlertDialog.Builder(this);
                dialog_2.setTitle("Are you sure you want to delete this list?");
                dialog_2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), mListName + " has been deleted.", Toast.LENGTH_SHORT).show();
                        deleteReminderList_db(mListId);
                        finish();
                    }
                });
                dialog_2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog_2.show();
                return true;

            case R.id.clear_checkoff:
                for (Reminder r : mReminders) {
                    r.setCheckoff(false);
                }
                clearCheckOff(mListId);
                onResume();
                return true;

            case R.id.move_reminder:
                final int[] selected_r = {-1};
                String[] rl_names = reminderlistNames();
                AlertDialog.Builder listDialog2 = new AlertDialog.Builder(this);
                listDialog2.setTitle("Choose the list:");
                listDialog2.setItems(rl_names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(selected_r[0] != -1) {
                            UUID r_id = mReminders.get(selected_r[0]).getId();
                            Iterator<Reminder> iter = mReminders.iterator();
                            while(iter.hasNext()){
                                Reminder item = iter.next();
                                if(item.getId().equals(r_id)){
                                    iter.remove();
                                }
                            }
                            mAdapter.updateReminderList(mReminders);
                            recyclerView.setAdapter(mAdapter);

                            String rl_id = mListIds.get(which);
                            changeListId(r_id.toString(),rl_id);
                        }
                    }
                });

                String[] r_names = reminderNames();
                AlertDialog.Builder listDialog = new AlertDialog.Builder(this);
                listDialog.setTitle("Choose the reminder you want to move:");
                listDialog.setItems(r_names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selected_r[0] = which;
                        listDialog2.show();
                    }
                });
                listDialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteReminderList_db(UUID rl_id){
        String whereClause = "rl_id" + "=?";
        String whereArgs[] = new String[]{rl_id.toString()};
        mDB.delete("reminderlists", whereClause, whereArgs);
        mDB.delete("reminders", whereClause, whereArgs);
    }

    public void renameReminderList_db(String rl_name, UUID rl_id){
        ContentValues values = new ContentValues();
        values.put("rl_name", rl_name);
        String whereClause = "rl_id" + "=?";
        String whereArgs[] = new String[]{rl_id.toString()};
        mDB.update("reminderlists", values, whereClause, whereArgs);
    }

    public void addReminder_db(Reminder r){
        ContentValues values = new ContentValues();
        values.put("r_id", r.getId().toString());
        values.put("rl_id", r.getList_id().toString());
        values.put("r_name", r.getName());
        values.put("r_type", r.getType());
        values.put("r_date", r.getDate().getTime());
        values.put("r_checkoff", (r.isCheckoff())? 1 : 0);
        mDB.insert("reminders", null, values);
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
            Date date = new Date(cursor.getLong(cursor.getColumnIndexOrThrow("r_date")));
            Boolean check_off = false;
            if(cursor.getInt(cursor.getColumnIndexOrThrow("r_checkoff")) == 1){
                check_off = true;
            }
            Reminder r = new Reminder(id, list_id, name, type, date, check_off);
            rs.add(r);
        }
        cursor.close();
        return rs;
    }

    public void clearCheckOff(UUID rl_id){
        ContentValues values = new ContentValues();
        values.put("r_checkoff", 0);
        String whereClause = "rl_id" + "=?";
        String whereArgs[] = new String[]{rl_id.toString()};
        mDB.update("reminders", values, whereClause, whereArgs);
    }

    public String[] reminderNames(){
        String[] names = new String[mReminders.size()];
        for(int i = 0; i < names.length; i++){
            names[i] = mReminders.get(i).getName().toString();
        }
        return names;
    }

    public String[] reminderlistNames(){
        List<String> rl_names = new ArrayList<>();
        mListIds = new ArrayList<>();
        String rawquery = "select * from reminderlists;";
        Cursor cursor = mDB.rawQuery(rawquery,null);
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndexOrThrow("rl_name"));
            String id = cursor.getString(cursor.getColumnIndexOrThrow("rl_id"));
            rl_names.add(name);
            mListIds.add(id);
        }
        cursor.close();

        String[] names = new String[rl_names.size()];
        for(int i = 0; i < names.length; i++){
            names[i] = rl_names.get(i).toString();
        }
        return names;
    }

    public void deleteReminder_db(UUID r_id){
        String whereClause = "r_id" + "=?";
        String whereArgs[] = new String[]{r_id.toString()};
        mDB.delete("reminders", whereClause, whereArgs);
    }

    public String getListID(int position){
        List<String> rl_ids = new ArrayList<>();
        String rawquery = "select * from reminderlists;";
        Cursor cursor = mDB.rawQuery(rawquery,null);
        while(cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndexOrThrow("rl_id"));
            rl_ids.add(id);
        }
        cursor.close();

        return rl_ids.get(position);
    }

    public void changeListId(String r_id, String rl_id){
        ContentValues values = new ContentValues();
        values.put("rl_id", rl_id);
        String whereClause = "r_id" + "=?";
        String whereArgs[] = new String[]{r_id};
        mDB.update("reminders", values, whereClause, whereArgs);
    }
}

