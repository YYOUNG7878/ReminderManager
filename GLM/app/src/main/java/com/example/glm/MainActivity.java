package com.example.glm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    ReminderListManager RLL = new ReminderListManager();
    private RecyclerView recyclerView;
    private ReminderListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_list_recycler);

        recyclerView = (RecyclerView) findViewById(R.id.reminder_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ReminderListAdapter(RLL);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume(){
        super.onResume();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.reminder_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ReminderListAdapter adapter = new ReminderListAdapter(RLL);
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
                ReminderList reminderlist = new ReminderList();
                RLL.addReminderList(reminderlist);
                mAdapter.updateReminderListManager(RLL);
                recyclerView.setAdapter(mAdapter);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}