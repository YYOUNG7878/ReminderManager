package com.example.glm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    ReminderListManager RLL = new ReminderListManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_list_recycler);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.reminder_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ReminderListAdapter adapter = new ReminderListAdapter(RLL);
        recyclerView.setAdapter(adapter);
    }
}