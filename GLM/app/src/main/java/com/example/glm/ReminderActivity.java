package com.example.glm;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentResultListener;

import java.util.Date;
import java.util.UUID;

public class ReminderActivity extends AppCompatActivity implements DateTimeFragment.ConfirmListener{
    private static final String EXTRA_REMINDERLIST_ID = "reminderlist_id";
    private static final String EXTRA_REMINDER_ID = "reminder_id";
    private static final String DATETIME_DIALOG = "datetime_dialog";

    private Reminder mReminder;
    private EditText nameField;
    private EditText typeField;
    private Button dateButton;
    private CheckBox checkoff;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder);

        Intent i = getIntent();
        UUID reminderlistId = (UUID) getIntent().getSerializableExtra(EXTRA_REMINDERLIST_ID);
        UUID reminderId = (UUID) getIntent().getSerializableExtra(EXTRA_REMINDER_ID);
        mReminder = ReminderListManager.getList(reminderlistId).getReminder(reminderId);

        nameField = findViewById(R.id.reminder_name);
        nameField.setText(mReminder.getName());
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mReminder.setName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        typeField = findViewById(R.id.reminder_type);
        typeField.setText(mReminder.getType());
        typeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mReminder.setType(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        dateButton = findViewById(R.id.reminder_date);
        dateButton.setText(mReminder.getDate().toString());
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
                mReminder.setCheckoff(b);
            }
        });
    }

    @Override
    public void onClickComplete(Date date){
        Toast.makeText(this, "Date Changed!", Toast.LENGTH_SHORT).show();
        dateButton.setText(date.toString());
        mReminder.setDate(date);
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
                //this.finish();
                //ReminderListManager.getList(mReminder.getList_id()).deleteReminder(mReminder.getId());
                //this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}