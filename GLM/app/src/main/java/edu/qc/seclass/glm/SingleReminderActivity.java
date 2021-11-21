package edu.qc.seclass.glm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ImageButton;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.Switch;
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

public class SingleReminderActivity extends AppCompatActivity implements DateTimeFragment.ConfirmListener {

    private UUID mReminderId;

    private Reminder mReminder;
    private String mName;
    private String mType;
    private String mRepeat;
    private Date mDate;
    private Boolean mCheckoff;

    private CheckBox checkoff;
    private TextView showType;
    private ImageButton typeButton;
    private EditText nameField;
    private ImageButton clearButton;
    private TextView showTime;
    private Button dateButton;
    private TextView showRepeat;
    private ImageButton repeatButton;
    private Button setReminder;

    private DataBaseManager mDBM;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_reminder);

        mDBM = DataBaseManager.get(this);

        mReminderId = (UUID) getIntent().getSerializableExtra("reminder_id");
        mReminder = mDBM.getReminder(mReminderId);

        mName = mReminder.getName();
        mType = mReminder.getType();
        mDate = mReminder.getDate();
        mRepeat = mReminder.getRepeat();
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
        clearButton = findViewById(R.id.button_clear_name);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameField.setText("");
            }
        });

        showType = findViewById(R.id.text_type);
        showType.setText(mReminder.getType());
        typeButton = findViewById(R.id.button_type);
        typeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int[] yourChoice = {-1};
                String types[] = mDBM.getTypes();
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
                                        else if(mDBM.addType(newType) == false){
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
                datetimefragment.show(getSupportFragmentManager(), "datetime_dialog");
            }
        });

        showRepeat = findViewById(R.id.reminder_repeat);
        repeatButton = findViewById(R.id.button_repeat);
        showRepeat.setText(mRepeat);
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int[] yourChoice = {-1};
                String repeats[] = {"Never", "Every Day", "Every Week", "Every 2 Weeks", "Every Month", "Every Year"};
                AlertDialog.Builder singleChoiceDialog =  new AlertDialog.Builder(view.getContext());
                singleChoiceDialog.setTitle("Set Repeat:");
                singleChoiceDialog.setSingleChoiceItems(repeats, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yourChoice[0] = which;
                    }
                });
                singleChoiceDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (yourChoice[0] != -1) {
                            showRepeat.setText(repeats[yourChoice[0]]);
                            mRepeat = showRepeat.getText().toString();
                        }
                    }
                });
                singleChoiceDialog.show();
            }
        });

        checkoff = findViewById(R.id.reminder_checkoff);
        checkoff.setChecked(mReminder.isCheckoff());
        checkoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCheckoff = b;
            }
        });

        setReminder = findViewById(R.id.btn_set);
        setReminder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mName.equals("")){
                    new AlertDialog.Builder(v.getContext()).setTitle("Warning!")
                    .setMessage("Name cannot be empty!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                } else {
                    mReminder.setName(mName);
                    mReminder.setType(mType);
                    mReminder.setRepeat(mRepeat);
                    mReminder.setDate(mDate);
                    mReminder.setCheckoff(mCheckoff);
                    mDBM.updateReminder(mReminder);
                    if(!mCheckoff){
                        startAlarm(mDate);
                    }
                    //Toast.makeText(SingleReminderActivity.this, "Reminder has been updated", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        });
    }

    private void startAlarm(Date d) {
        if(d.getTime() <= System.currentTimeMillis()){

        } else {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
            intent.putExtra("reminder_id", mReminderId);
            int c = mDBM.getAlarmCount();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, c, intent, 0);
            mDBM.updateAlarmCount(c);

            switch (mRepeat) {
                case "Never":
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, d.getTime(), pendingIntent);
                    Toast.makeText(this, "Alarm is set.", Toast.LENGTH_SHORT).show();
                    break;

                case "Every Day":
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, d.getTime(), AlarmManager.INTERVAL_DAY, pendingIntent);
                    Toast.makeText(this, "Alarm will be repeated daily.", Toast.LENGTH_SHORT).show();
                    break;

                case "Every Week":
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, d.getTime(), AlarmManager.INTERVAL_DAY*7, pendingIntent);
                    Toast.makeText(this, "Alarm will be repeated weekly.", Toast.LENGTH_SHORT).show();
                    break;

                case "Every 2 Week":
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, d.getTime(), AlarmManager.INTERVAL_DAY*14, pendingIntent);
                    Toast.makeText(this, "Alarm will be repeated every 2 week.", Toast.LENGTH_SHORT).show();
                    break;

                case "Every Month":
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, d.getTime(), AlarmManager.INTERVAL_DAY*30, pendingIntent);
                    Toast.makeText(this, "Alarm will be repeated monthly.", Toast.LENGTH_SHORT).show();
                    break;

                case "Every Year":
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, d.getTime(), AlarmManager.INTERVAL_DAY*365, pendingIntent);
                    Toast.makeText(this, "Alarm will be repeated yearly.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    }

    @Override
    public void onClickComplete(Date date){
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
                        mDBM.deleteReminder(mReminderId);
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