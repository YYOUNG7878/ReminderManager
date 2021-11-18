package edu.qc.seclass.glm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import edu.qc.seclass.glm.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTimeFragment extends DialogFragment {

    private DatePicker mDatepicker;
    private TimePicker mTimepicker;

    public static DateTimeFragment newInstance(Date date){
        Bundle bundle = new Bundle();
        bundle.putSerializable("date", date);

        DateTimeFragment fragment = new DateTimeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable("date");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.date_time, null);

        mDatepicker = (DatePicker) v.findViewById(R.id.date_picker);
        mDatepicker.init(year, month, day, null);
        mTimepicker = (TimePicker) v.findViewById(R.id.time_picker);
        mTimepicker.setCurrentHour(hour);
        mTimepicker.setCurrentMinute(minute);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                //.setTitle(R.string.reminder_date_label)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        int year = mDatepicker.getYear();
                        int month = mDatepicker.getMonth();
                        int day = mDatepicker.getDayOfMonth();
                        int hour = mTimepicker.getCurrentHour();
                        int minute = mTimepicker.getCurrentMinute();

                        Date date = new GregorianCalendar(year, month, day, hour, minute).getTime();
                        ConfirmListener confirmListener = (ConfirmListener) getActivity();
                        confirmListener.onClickComplete(date);
                    }
                })
                .create();
    }

    public interface ConfirmListener{
        void onClickComplete(Date date);
    }

}
