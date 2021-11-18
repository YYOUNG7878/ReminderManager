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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.qc.seclass.glm.R;

import java.util.UUID;

public class ReminderListActivity extends AppCompatActivity{

    private static final String EXTRA_REMINDERLIST_ID = "reminderlist_id";

    public ReminderList mReminderlist;
    private RecyclerView recyclerView;
    private ReminderAdapter mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_recycler);

        Intent i = getIntent();
        UUID ListId = (UUID) i.getSerializableExtra(EXTRA_REMINDERLIST_ID);

        mReminderlist = ReminderListManager.getList(ListId);

        this.setTitle(mReminderlist.getName());

        recyclerView = (RecyclerView) findViewById(R.id.reminder_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ReminderAdapter(mReminderlist);
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new MyItemTouchHelperCallback(mAdapter));
        mItemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    public void onResume(){
        super.onResume();

        Intent i = getIntent();
        UUID ListId = (UUID) i.getSerializableExtra(EXTRA_REMINDERLIST_ID);

        ReminderList reminderlist = ReminderListManager.getList(ListId);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.reminder_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ReminderAdapter adapter = new ReminderAdapter(reminderlist);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new MyItemTouchHelperCallback(mAdapter));
        mItemTouchHelper.attachToRecyclerView(recyclerView);

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
                Reminder reminder = new Reminder();
                mReminderlist.addReminder(reminder);
                mAdapter.updateReminderList(mReminderlist);
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
                        mReminderlist.setName(editText.getText().toString());
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
                        Toast.makeText(getApplicationContext(), mReminderlist.getName() + " has been deleted.", Toast.LENGTH_SHORT).show();
                        ReminderListManager.deleteReminderList(mReminderlist.getId());
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
                for (Reminder r : mReminderlist.getReminders()) {
                    r.setCheckoff(false);
                }
                onResume();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

