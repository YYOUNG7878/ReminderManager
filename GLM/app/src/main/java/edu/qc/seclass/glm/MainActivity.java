package edu.qc.seclass.glm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button customList;
    Button typeList;
    ImageButton searchButton;

    private DataBaseManager mDBM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDBM = DataBaseManager.get(this);

        setContentView(R.layout.home_screen);
        customList = findViewById(R.id.button_custom_list);
        typeList = findViewById(R.id.button_type_list);
        searchButton = findViewById(R.id.button_search);

        customList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), customListActivity.class);
                startActivity(intent);
            }
        });

        typeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), typeListActivity.class);
                startActivity(intent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = new EditText(MainActivity.this);
                AlertDialog.Builder inputDialog = new AlertDialog.Builder(MainActivity.this);
                inputDialog.setTitle("Search: ").setView(editText);
                inputDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String target = editText.getText().toString();
                                List<Reminder> result = mDBM.getBySearch(target);
                                if(result.isEmpty()){
                                    new AlertDialog.Builder(view.getContext()).setTitle("no result!")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            }).show();
                                }
                                else{
                                    Intent intent = new Intent();
                                    intent.setClass(MainActivity.this, SearchActivity.class);
                                    intent.putExtra("keyword", target);
                                    startActivity(intent);
                                }
                            }
                        }).show();
            }
        });
    }
}
