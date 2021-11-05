package com.example.glm;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> implements ItemTouchHelperAdapter{

    private static final String EXTRA_REMINDERLIST_ID = "reminderlist_id";
    private static final String EXTRA_REMINDER_ID = "reminder_id";

    private List<Reminder> mReminders;
    private ViewHolder mViewholder;
    private ReminderList reminderlist;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected TextView nameTextView;
        protected TextView dateTextView;
        protected CheckBox checkoffView;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            nameTextView = (TextView) view.findViewById(R.id.reminder_name_title);
            dateTextView = (TextView) view.findViewById(R.id.reminder_date_title);
            checkoffView = (CheckBox) view.findViewById(R.id.reminder_checkoff_title);
        }

        private Reminder reminder;

        /*
        public void bind(Reminder r){
            reminder = r;
            nameTextView.setText(reminder.getName());
            dateTextView.setText(reminder.getDate().toString());
            checkoffView.setChecked(reminder.isCheckoff());
            checkoffView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    reminder.setCheckoff(b);
                }
            });
        }

         */


        @Override
        public void onClick(View v){
            Intent intent = new Intent();
            intent.setClass(v.getContext(), ReminderActivity.class);
            intent.putExtra(EXTRA_REMINDERLIST_ID, reminder.getList_id());
            intent.putExtra(EXTRA_REMINDER_ID, reminder.getId());
            v.getContext().startActivity(intent);
        }

    }

    public ReminderAdapter (ReminderList rs){
        mReminders = rs.getReminders();
    }

    public void updateReminderList (ReminderList rs){
        mReminders = rs.getReminders();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.reminder_list, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mViewholder = holder;
        Reminder reminder = mReminders.get(position);

        mViewholder.reminder = reminder;
        mViewholder.nameTextView.setText(reminder.getName());
        mViewholder.dateTextView.setText(reminder.getDate().toString());
        mViewholder.checkoffView.setChecked(reminder.isCheckoff());
        mViewholder.checkoffView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                reminder.setCheckoff(b);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mReminders.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition){
        Collections.swap(mReminders, fromPosition, toPosition);
        notifyDataSetChanged();
    }

    @Override
    public void onItemDismiss(int position){

    }

}



