package edu.qc.seclass.glm;


import android.annotation.SuppressLint;
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

import com.example.glm.R;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> implements ItemTouchHelperAdapter{

    private static final String EXTRA_REMINDERLIST_ID = "reminderlist_id";
    private static final String EXTRA_REMINDER_ID = "reminder_id";

    private List<Reminder> mReminders;
    private ViewHolder mViewholder;
    private ReminderList reminderlist;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected TextView nameTextView;
        protected TextView dateTextView;
        protected TextView typeSpinnerView;
        protected CheckBox checkoffView;
        protected ImageButton deleteButton;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            nameTextView = (TextView) view.findViewById(R.id.reminder_name_title);
            dateTextView = (TextView) view.findViewById(R.id.reminder_date_title);
            checkoffView = (CheckBox) view.findViewById(R.id.reminder_checkoff_title);
            typeSpinnerView = (TextView) view.findViewById(R.id.reminder_type_title);
            deleteButton = (ImageButton) view.findViewById(R.id.delete_reminder_inlist);
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
            intent.setClass(v.getContext(), AddReminderActivity.class);
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
                .inflate(R.layout.reminder_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //mViewholder = holder;
        Reminder reminder = mReminders.get(position);

        holder.reminder = reminder;
        holder.nameTextView.setText(reminder.getName());
        holder.typeSpinnerView.setText(reminder.getType());
        holder.dateTextView.setText(reminder.getDate().toString());
        holder.checkoffView.setChecked(reminder.isCheckoff());
        holder.checkoffView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                reminder.setCheckoff(b);
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
                        ReminderListManager.getList(reminder.getList_id()).deleteReminder(id);
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

        /*
        mViewholder.reminder = reminder;
        mViewholder.nameTextView.setText(reminder.getName());
        mViewholder.typeSpinnerView.setText(reminder.getType());
        mViewholder.dateTextView.setText(reminder.getDate().toString());
        mViewholder.checkoffView.setChecked(reminder.isCheckoff());
        mViewholder.checkoffView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                reminder.setCheckoff(b);
            }
        });


         */
    }

    @Override
    public int getItemCount() {
        return mReminders.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition){
        Collections.swap(mReminders, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position){
        UUID id = mReminders.get(position).getId();
        ReminderListManager.getList(mReminders.get(position).getList_id()).deleteReminder(id);
        notifyItemRemoved(position);
    }

}



