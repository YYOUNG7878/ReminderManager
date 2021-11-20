package edu.qc.seclass.glm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class RemindersByListActivity extends AppCompatActivity{

    private DataBaseManager mRLM;

    private UUID mListId;
    private String mListName;
    private List<Reminder> mReminders;
    private RecyclerView recyclerView;
    private RemindersAdapter mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRLM = DataBaseManager.get(this);

        setContentView(R.layout.reminder_recycler);

        Intent i = getIntent();
        mListId = (UUID) i.getSerializableExtra("reminderlist_id");
        mListName = (String) i.getSerializableExtra("reminderlist_name");

        mReminders = mRLM.getReminders(mListId);

        this.setTitle(mListName);

        recyclerView = (RecyclerView) findViewById(R.id.reminder_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new RemindersAdapter(mReminders, this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume(){
        super.onResume();

        mRLM = DataBaseManager.get(this);

        setContentView(R.layout.reminder_recycler);

        Intent i = getIntent();
        mListId = (UUID) i.getSerializableExtra("reminderlist_id");
        mListName = (String) i.getSerializableExtra("reminderlist_name");

        mReminders = mRLM.getReminders(mListId);

        this.setTitle(mListName);

        recyclerView = (RecyclerView) findViewById(R.id.reminder_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new RemindersAdapter(mReminders, this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reminder_custom_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_reminder_custom_list:

                Reminder reminder = new Reminder(mListId);
                mRLM.addReminder(reminder);

                Intent intent = new Intent();
                intent.setClass(this, SingleReminderActivity.class);
                intent.putExtra("reminder_id", reminder.getId());
                startActivity(intent);

                mReminders.add(reminder);
                mAdapter.updateReminderList(mReminders);
                recyclerView.setAdapter(mAdapter);
                return true;

            case R.id.rename_custom_list:

                EditText editText = new EditText(this);
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Enter a new name:").setView(editText);
                dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = editText.getText().toString();
                        if(name.equals("")){
                            new AlertDialog.Builder(RemindersByListActivity.this).setTitle("Warning!")
                            .setMessage("Name cannot be empty!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                        } else {
                            getSupportActionBar().setTitle(name);
                            mRLM.renameReminderList(name, mListId);
                            Toast.makeText(getApplicationContext(), "succeed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
                return true;

            case R.id.delete_custom_list:

                AlertDialog.Builder dialog_2 = new AlertDialog.Builder(this);
                dialog_2.setTitle("Are you sure you want to delete this list?");
                dialog_2.setMessage("It will remove all the reminders in the list.");
                dialog_2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), mListName + " has been deleted.", Toast.LENGTH_SHORT).show();
                        mRLM.deleteReminderList(mListId);
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

            case R.id.clear_checkoff_custom_list:

                for (Reminder r : mReminders) {
                    r.setCheckoff(false);
                }
                mRLM.clearCheckOff(mListId);
                onResume();
                return true;

            case R.id.move_reminder_custom_list:

                final int[] selected_r = {-1};
                String[] rl_names = mRLM.reminderlistNames();
                String[] rl_ids = mRLM.reminderlistIds();

                String[] r_names = mRLM.reminderNames(mListId);
                AlertDialog.Builder listDialog = new AlertDialog.Builder(this);
                listDialog.setTitle("Choose the reminder you want to move:");
                listDialog.setItems(r_names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selected_r[0] = which;
                        AlertDialog.Builder listDialog2 = new AlertDialog.Builder(RemindersByListActivity.this);
                        listDialog2.setTitle("Choose the list:");
                        listDialog2.setItems(rl_names, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(selected_r[0] != -1) {
                                    UUID r_id = mReminders.get(selected_r[0]).getId();
                                    String rl_id = rl_ids[which];
                                    if(rl_id.equals(mListId.toString())){
                                        Toast.makeText(getApplicationContext(), "It's already in this list!", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Iterator<Reminder> iter = mReminders.iterator();
                                        while (iter.hasNext()) {
                                            Reminder item = iter.next();
                                            if (item.getId().equals(r_id)) {
                                                iter.remove();
                                            }
                                        }
                                        mAdapter.updateReminderList(mReminders);
                                        recyclerView.setAdapter(mAdapter);

                                        mRLM.changeListId(r_id.toString(), rl_id);
                                    }
                                }
                            }
                        }).show();
                    }
                });
                listDialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

