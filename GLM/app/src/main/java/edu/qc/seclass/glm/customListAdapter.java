package edu.qc.seclass.glm;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class customListAdapter extends RecyclerView.Adapter<customListAdapter.ViewHolder> {

    private List<ReminderList> mReminderLists;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nameTextView;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            nameTextView = (TextView) view.findViewById(R.id.type_list_name_title);
        }

        private ReminderList mReminderList;

        @Override
        public void onClick(View v){
           Intent intent = new Intent(v.getContext(), RemindersByListActivity.class);
           intent.putExtra("reminderlist_id", mReminderList.getId());
           intent.putExtra("reminderlist_name", mReminderList.getName());
           v.getContext().startActivity(intent);
        }

    }

    public customListAdapter(DataBaseManager rlm){
        mReminderLists = rlm.getReminderLists();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_list_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReminderList reminderlist = mReminderLists.get(position);
        holder.mReminderList = reminderlist;
        holder.nameTextView.setText(reminderlist.getName());

    }

    @Override
    public int getItemCount() {
        return mReminderLists.size();
    }

}