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

public class RemindersByTypeActivity extends AppCompatActivity {

    private DataBaseManager mRLM;

    private UUID mListId;
    private String mTypeName;
    private List<Reminder> mReminders;
    private RecyclerView recyclerView;
    private RemindersAdapter mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRLM = DataBaseManager.get(this);

        setContentView(R.layout.reminder_recycler);

        Intent i = getIntent();
        mTypeName = (String) i.getSerializableExtra("type_name");

        mReminders = mRLM.getRemindersByType(mTypeName);

        this.setTitle(mTypeName);

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

        if(mTypeName == null) {
            Intent i = getIntent();
            mTypeName = (String) i.getSerializableExtra("type_name");
        }

        mReminders = mRLM.getRemindersByType(mTypeName);

        this.setTitle(mTypeName);

        recyclerView = (RecyclerView) findViewById(R.id.reminder_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new RemindersAdapter(mReminders, this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reminder_type_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_reminder_type_list:

                Reminder reminder = mRLM.addReminderByType(mTypeName);

                Intent intent = new Intent();
                intent.setClass(this, SingleReminderActivity.class);
                intent.putExtra("reminder_id", reminder.getId());
                startActivity(intent);

                onResume();
                return true;

            case R.id.rename_type_list:

                EditText editText = new EditText(this);
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Enter a new name:").setView(editText);
                dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = editText.getText().toString();
                        if(name.equals("")){
                            new AlertDialog.Builder(RemindersByTypeActivity.this).setTitle("Warning!")
                                    .setMessage("Name cannot be empty!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                        }
                        else if(mRLM.renameType(mTypeName, name) == false){
                            new AlertDialog.Builder(RemindersByTypeActivity.this).setTitle("Warning!")
                                    .setMessage("The type already exists!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                        }
                        else {
                            getSupportActionBar().setTitle(name);
                            Toast.makeText(getApplicationContext(), "succeed!", Toast.LENGTH_SHORT).show();
                            mTypeName = name;
                            onResume();
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

            case R.id.delete_type_list:

                AlertDialog.Builder dialog_2 = new AlertDialog.Builder(this);
                dialog_2.setTitle("Are you sure you want to delete this type?");
                dialog_2.setMessage("It will remove all the reminders belonging to this type.");
                dialog_2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), mTypeName + " has been deleted.", Toast.LENGTH_SHORT).show();
                        mRLM.deleteType(mTypeName);
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

            case R.id.clear_checkoff_type_list:

                for (Reminder r : mReminders) {
                    r.setCheckoff(false);
                }
                mRLM.clearCheckOffByType(mTypeName);
                mAdapter.updateReminderList(mReminders);
                recyclerView.setAdapter(mAdapter);
                return true;

            case R.id.move_reminder_type_list:

                final int[] selected_r = {-1};
                String[] r_names = mRLM.reminderNamesByType(mTypeName);
                String[] t_names = mRLM.getTypes();
                AlertDialog.Builder listDialog = new AlertDialog.Builder(this);
                listDialog.setTitle("Choose the reminder you want to move:");
                listDialog.setItems(r_names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selected_r[0] = which;
                        AlertDialog.Builder listDialog2 = new AlertDialog.Builder(RemindersByTypeActivity.this);
                        listDialog2.setTitle("Move to:");
                        listDialog2.setItems(t_names, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(selected_r[0] != -1) {
                                    UUID r_id = mReminders.get(selected_r[0]).getId();
                                    String t_name = t_names[which];
                                    if(t_name.equals(mTypeName)){

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
                                        mRLM.changeType(r_id.toString(), t_name);
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
