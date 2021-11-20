package edu.qc.seclass.glm;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class typeListActivity extends AppCompatActivity {

    private DataBaseManager mRLM;

    private RecyclerView recyclerView;
    private typeListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Reminder Types");

        mRLM = DataBaseManager.get(this);

        setContentView(R.layout.type_list_recycler);
        recyclerView = (RecyclerView) findViewById(R.id.type_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new typeListAdapter(mRLM);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        setTitle("Reminder Types");

        mRLM = DataBaseManager.get(this);

        setContentView(R.layout.type_list_recycler);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.type_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        typeListAdapter adapter = new typeListAdapter(mRLM);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manager_type_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_type_list:
                EditText editText = new EditText(this);
                AlertDialog.Builder inputDialog = new AlertDialog.Builder(this);
                inputDialog.setTitle("Please enter a name for the new type:").setView(editText);
                inputDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newType = editText.getText().toString();
                                if(newType.equals("")){
                                    new AlertDialog.Builder(typeListActivity.this).setTitle("Warning!")
                                            .setMessage("Cannot be empty!")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            }).show();
                                }
                                else if(mRLM.addType(newType) == false){
                                    new AlertDialog.Builder(typeListActivity.this).setTitle("Warning!")
                                            .setMessage("The type already exists!")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            }).show();
                                }
                                else {
                                    mRLM.addType(newType);
                                    onResume();
                                }
                            }
                        }).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
