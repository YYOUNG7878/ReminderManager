package edu.qc.seclass.glm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private List<Reminder> mReminders;

    private DataBaseManager mDBM;

    private RecyclerView recyclerView;
    private SearchItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Result");

        mDBM = DataBaseManager.get(this);

        String target = (String)getIntent().getSerializableExtra("keyword");
        mReminders = mDBM.getBySearch(target);

        setContentView(R.layout.search_list_recycler);
        recyclerView = findViewById(R.id.search_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new SearchItemAdapter(mReminders);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        setTitle("Result");

        mDBM = DataBaseManager.get(this);

        String target = (String)getIntent().getSerializableExtra("keyword");
        mReminders = mDBM.getBySearch(target);

        setContentView(R.layout.search_list_recycler);
        recyclerView = findViewById(R.id.search_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new SearchItemAdapter(mReminders);
        recyclerView.setAdapter(mAdapter);
    }

}

