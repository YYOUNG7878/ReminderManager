package edu.qc.seclass.glm;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.TextView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import edu.qc.seclass.glm.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AddReminderActivity extends AppCompatActivity implements DateTimeFragment.ConfirmListener{
    private static final String DATETIME_DIALOG = "datetime_dialog";

    private UUID mReminderId;
    private UUID mListId;

    private Reminder mReminder;
    private String mName;
    private String mType;
    private Date mDate;
    private Boolean mCheckoff;

    private CheckBox checkoff;
    private EditText nameField;
    private TextView showType;
    private ImageButton typeButton;
    private TextView showTime;
    private Button dateButton;
    private Button setReminder;


    private SQLiteDatabase mDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reminder);

        mDB = new ReminderBaseHelper(this).getWritableDatabase();

        Intent i = getIntent();
        mListId = (UUID) getIntent().getSerializableExtra("reminderlist_id");
        mReminderId = (UUID) getIntent().getSerializableExtra("reminder_id");
        mReminder = getReminder(mReminderId);

        mName = mReminder.getName();
        mType = mReminder.getType();
        mDate = mReminder.getDate();
        mCheckoff = mReminder.isCheckoff();

        this.setTitle(mReminder.getName());

        nameField = findViewById(R.id.reminder_name);
        nameField.setText(mReminder.getName());
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mName = nameField.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        showType = findViewById(R.id.text_type);
        showType.setText(mReminder.getType());
        typeButton = findViewById(R.id.button_type);
        typeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int[] yourChoice = {-1};
                String types[] = getTypes();
                AlertDialog.Builder singleChoiceDialog =  new AlertDialog.Builder(view.getContext());
                singleChoiceDialog.setTitle("Select a type:");
                singleChoiceDialog.setSingleChoiceItems(types, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yourChoice[0] = which;
                    }
                });
                singleChoiceDialog.setNegativeButton("Add a new type", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        final EditText editText = new EditText(view.getContext());
                        AlertDialog.Builder inputDialog = new AlertDialog.Builder(view.getContext());
                        inputDialog.setTitle("Please enter a name:").setView(editText);
                        inputDialog.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String newType = editText.getText().toString();
                                        if(newType.equals("")){
                                            new AlertDialog.Builder(view.getContext()).setTitle("Warning!")
                                            .setMessage("Cannot be empty!")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            }).show();
                                        }
                                        else if(addType(newType) == false){
                                            new AlertDialog.Builder(view.getContext()).setTitle("Warning!")
                                            .setMessage("The type already exists!")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            }).show();
                                        } else {
                                            showType.setText(newType);
                                            mType = newType;
                                        }
                                    }
                                }).show();
                    }
                });
                singleChoiceDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (yourChoice[0] != -1) {
                            showType.setText(types[yourChoice[0]]);
                            mType = showType.getText().toString();
                        }
                    }
                });
                singleChoiceDialog.show();
            }
        });

        showTime = findViewById(R.id.tv_reminder_time);
        dateButton = findViewById(R.id.reminder_date);
        mDate = mReminder.getDate();
        showTime.setText(mReminder.getDate().toString());
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTimeFragment datetimefragment = DateTimeFragment.newInstance(mReminder.getDate());
                datetimefragment.show(getSupportFragmentManager(), DATETIME_DIALOG);
            }
        });

        checkoff = findViewById(R.id.reminder_checkoff);
        checkoff.setChecked(mReminder.isCheckoff());
        checkoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        setReminder = findViewById(R.id.btn_set);
        setReminder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mReminder.setName(nameField.getText().toString());
                mReminder.setType(mType);
                mReminder.setDate(mDate);
                mReminder.setCheckoff(checkoff.isChecked());
                updateReminder(mReminder);
                Toast.makeText(AddReminderActivity.this, "Reminder has been updated", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
    }

    @Override
    public void onClickComplete(Date date){
        Toast.makeText(this, "Time updated!", Toast.LENGTH_SHORT).show();
        showTime.setText(date.toString());
        mDate = date;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_reminder:
                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Are you sure you want to delete this reminder?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteReminder_db(mReminderId);
                        finish();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Reminder getReminder(UUID r_id){
        Reminder r = new Reminder(mListId);

        String rawquery = "select * from reminders where r_id = " + "'" + r_id.toString() + "'" +";";
        Cursor cursor = mDB.rawQuery(rawquery,null);
        while(cursor.moveToNext()){
            UUID id =  UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("r_id")));
            UUID list_id = UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("rl_id")));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("r_name"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("r_type"));
            Date date = new Date(cursor.getLong(cursor.getColumnIndexOrThrow("r_date")));
            Boolean check_off = false;
            if(cursor.getInt(cursor.getColumnIndexOrThrow("r_checkoff")) == 1){
                check_off = true;
            }
            r = new Reminder(id, list_id, name, type, date, check_off);
        }
        cursor.close();
        return r;
    }

    public void deleteReminder_db(UUID r_id){
        String whereClause = "r_id" + "=?";
        String whereArgs[] = new String[]{r_id.toString()};
        mDB.delete("reminders", whereClause, whereArgs);
    }

    public void updateReminder(Reminder r){
        ContentValues values = new ContentValues();
        values.put("r_name", r.getName());
        values.put("r_type", r.getType());
        values.put("r_date", r.getDate().getTime());
        values.put("r_checkoff", (r.isCheckoff())? 1 : 0);
        String whereClause = "r_id" + "=?";
        String whereArgs[] = new String[]{r.getId().toString()};
        mDB.update("reminders", values, whereClause, whereArgs);
    }

    public String[] getTypes(){
        List<String> types = new ArrayList<>();

        String rawquery = "select * from types;";
        Cursor cursor = mDB.rawQuery(rawquery,null);
        while(cursor.moveToNext()){
            String t_name = cursor.getString(cursor.getColumnIndexOrThrow("t_name"));
            types.add(t_name);
        }
        cursor.close();

        String[] type_arr = new String[types.size()];
        for(int i = 0; i < types.size(); i++){
            type_arr[i] = types.get(i);
        }
        return type_arr;
    }

    public boolean addType(String newType){
        String rawquery = "select * from types where t_name = " + "'" + newType + "';";
        Cursor cursor = mDB.rawQuery(rawquery, null);
        if(cursor.getCount() != 0){
            return false;
        } else {
            ContentValues values = new ContentValues();
            values.put("t_name", newType);
            mDB.insert("types", null, values);
            return true;
        }
    }
}