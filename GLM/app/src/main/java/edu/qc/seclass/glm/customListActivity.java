package edu.qc.seclass.glm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class customListActivity extends AppCompatActivity {

    private DataBaseManager mRLM;

    private RecyclerView recyclerView;
    private customListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("User's Lists");

        mRLM = DataBaseManager.get(this);

        setContentView(R.layout.custom_list_recycler);
        recyclerView = (RecyclerView) findViewById(R.id.custom_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new customListAdapter(mRLM);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        setTitle("User's List");

        mRLM = DataBaseManager.get(this);

        setContentView(R.layout.custom_list_recycler);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.custom_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        customListAdapter adapter = new customListAdapter(mRLM);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manager_custom_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_custom_list:
                ReminderList reminderlist = new ReminderList();
                mRLM.addReminderList(reminderlist);
                onResume();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}