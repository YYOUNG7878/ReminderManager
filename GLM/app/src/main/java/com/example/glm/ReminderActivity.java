package com.example.glm;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class ReminderActivity extends AppCompatActivity {
    private static final String EXTRA_REMINDERLIST_ID = "reminderlist_id";
    private static final String EXTRA_REMINDER_ID = "reminder_id";

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
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mReminder.setName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        typeField = findViewById(R.id.reminder_type);
        typeField.setText(mReminder.getType());
        typeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mReminder.setType(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        dateButton = findViewById(R.id.reminder_date);
        dateButton.setText(mReminder.getDate().toString());
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

}
