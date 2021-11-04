package com.example.glm;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.UUID;

public class ReminderListActivity extends AppCompatActivity {

    private static final String EXTRA_REMINDERLIST_ID = "reminderlist_id";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_recycler);
       // setContentView(R.layout.reminder_list);

        Intent i = getIntent();
        UUID ListId = (UUID) i.getSerializableExtra(EXTRA_REMINDERLIST_ID);

        //nameField = findViewById(R.id.reminder_name_title);
        //nameField.setText(ListId.toString());

        ReminderList reminderlist = ReminderListManager.getList(ListId);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.reminder_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ReminderAdapter adapter = new ReminderAdapter(reminderlist);
        recyclerView.setAdapter(adapter);
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
    }
}
