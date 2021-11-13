package com.example.glm;

import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ArrayAdapter;
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

import java.util.Date;
import java.util.UUID;

public class AddReminderActivity extends AppCompatActivity implements DateTimeFragment.ConfirmListener{
    private static final String EXTRA_REMINDERLIST_ID = "reminderlist_id";
    private static final String EXTRA_REMINDER_ID = "reminder_id";
    private static final String DATETIME_DIALOG = "datetime_dialog";

    private Reminder mReminder;
    private CheckBox checkoff;
    private EditText nameField;
    private Spinner typeSpinner;
    private TextView showTime;
    private Date mDate;
    private Button dateButton;
    private String[] types = {"Appointment", "Event", "Meeting", "Task"};
    private Button setReminder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reminder);

        Intent i = getIntent();
        UUID reminderlistId = (UUID) getIntent().getSerializableExtra(EXTRA_REMINDERLIST_ID);
        UUID reminderId = (UUID) getIntent().getSerializableExtra(EXTRA_REMINDER_ID);
        mReminder = ReminderListManager.getList(reminderlistId).getReminder(reminderId);

        this.setTitle(mReminder.getName());

        nameField = findViewById(R.id.reminder_name);
        nameField.setText(mReminder.getName());
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        typeSpinner = findViewById(R.id.spinner_reminder_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ReminderListManager.types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

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
                mReminder.setDate(mDate);
                mReminder.setCheckoff(checkoff.isChecked());
                mReminder.setType(typeSpinner.getSelectedItem().toString());
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
                        ReminderListManager.getList(mReminder.getList_id()).deleteReminder(mReminder.getId());
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
}