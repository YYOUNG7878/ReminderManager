package edu.qc.seclass.glm;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.qc.seclass.glm.R;

import java.util.List;

public class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.ViewHolder> {

    private List<ReminderList> mReminderlists;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nameTextView;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            nameTextView = (TextView) view.findViewById(R.id.reminder_list_name_title);
        }

        private ReminderList mReminderlist;

        public void bind(ReminderList reminderlist){
            mReminderlist = reminderlist;
            nameTextView.setText(mReminderlist.getName());
        }

        @Override
        public void onClick(View v){
           Intent intent = new Intent(v.getContext(), ReminderListActivity.class);
           intent.putExtra("reminderlist_id", mReminderlist.getId());
           intent.putExtra("reminderlist_name", mReminderlist.getName());
           v.getContext().startActivity(intent);
        }

    }

    public ReminderListAdapter (ReminderListManager rlm){
        mReminderlists = rlm.getReminderLists();
    }

    public void updateReminderListManager (ReminderListManager rlm){
        mReminderlists = rlm.getReminderLists();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.reminder_list_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReminderList reminderlist = mReminderlists.get(position);
        holder.bind(reminderlist);
    }

    @Override
    public int getItemCount() {
        return mReminderlists.size();
    }

}