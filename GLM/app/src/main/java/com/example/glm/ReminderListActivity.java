package com.example.glm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.UUID;

public class ReminderListActivity extends AppCompatActivity {

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

