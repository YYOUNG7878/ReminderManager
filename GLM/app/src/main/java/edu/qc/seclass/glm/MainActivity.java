package edu.qc.seclass.glm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    ReminderListManager RLM;
    private RecyclerView recyclerView;
    private ReminderListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RLM = new ReminderListManager(this);

        setContentView(R.layout.reminder_list_recycler);
        recyclerView = (RecyclerView) findViewById(R.id.reminder_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ReminderListAdapter(RLM);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume(){
        super.onResume();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.reminder_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ReminderListAdapter adapter = new ReminderListAdapter(RLM);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reminderlist_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_reminderlist:
                ReminderList reminderlist = new ReminderList(this);
                RLM.addReminderList(reminderlist);
                mAdapter.updateReminderListManager(RLM);
                recyclerView.setAdapter(mAdapter);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}