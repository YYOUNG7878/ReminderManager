package edu.qc.seclass.glm;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ViewHolder> {

    private List<Reminder> mReminders;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Reminder reminder;

        private TextView nameTextView;
        private TextView dateTextView;
        private TextView typeTextView;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            nameTextView = view.findViewById(R.id.reminder_name_title_search);
            dateTextView = view.findViewById(R.id.reminder_date_title_search);
            typeTextView = view.findViewById(R.id.reminder_type_title_search);
        }

        @Override
        public void onClick(View v){
            Intent intent = new Intent();
            intent.setClass(v.getContext(), SingleReminderActivity.class);
            intent.putExtra("reminder_id", reminder.getId());
            v.getContext().startActivity(intent);
        }

    }

    public SearchItemAdapter(List<Reminder> rs){
        mReminders = rs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_list_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reminder reminder = mReminders.get(position);

        holder.reminder = reminder;
        holder.nameTextView.setText(reminder.getName());
        holder.typeTextView.setText(reminder.getType());
        holder.dateTextView.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(reminder.getDate()));
    }

    @Override
    public int getItemCount() {
        return mReminders.size();
    }
}
