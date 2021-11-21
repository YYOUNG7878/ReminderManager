package edu.qc.seclass.glm;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.ViewHolder> {

    private DataBaseManager mRLM;

    private List<Reminder> mReminders;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Reminder reminder;

        private TextView nameTextView;
        private TextView dateTextView;
        private TextView typeTextView;
        private CheckBox checkoffView;
        private ImageButton deleteButton;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            nameTextView = view.findViewById(R.id.reminder_name_title);
            dateTextView = view.findViewById(R.id.reminder_date_title);
            checkoffView = view.findViewById(R.id.reminder_checkoff_title);
            typeTextView = view.findViewById(R.id.reminder_type_title);
            deleteButton = view.findViewById(R.id.delete_reminder_in_list);
        }

        @Override
        public void onClick(View v){
            Intent intent = new Intent();
            intent.setClass(v.getContext(), SingleReminderActivity.class);
            intent.putExtra("reminder_id", reminder.getId());
            v.getContext().startActivity(intent);
        }

    }

    public RemindersAdapter(List<Reminder> rs, Context context){
        mReminders = rs;
        mRLM = DataBaseManager.get(context);
    }

    public void updateReminderList (List<Reminder> rs){
        mReminders = rs;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.reminder_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Reminder reminder = mReminders.get(position);

        holder.reminder = reminder;
        holder.nameTextView.setText(reminder.getName());
        holder.typeTextView.setText(reminder.getType());
        holder.dateTextView.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(reminder.getDate()));
        holder.checkoffView.setChecked(reminder.isCheckoff());
        holder.checkoffView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                reminder.setCheckoff(b);
                mRLM.updateCheckOff(reminder);
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog_2 = new AlertDialog.Builder(view.getContext());
                dialog_2.setTitle("Are you sure you want to delete this reminder?");
                dialog_2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UUID id = reminder.getId();
                        Iterator<Reminder> iter = mReminders.iterator();
                        while(iter.hasNext()){
                            Reminder item = iter.next();
                            if(item.getId().equals(id)){
                                iter.remove();
                            }
                        }
                        mRLM.deleteReminder(id);
                        notifyDataSetChanged();
                    }
                });
                dialog_2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog_2.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mReminders.size();
    }

}



