package edu.qc.seclass.glm;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class typeListAdapter extends RecyclerView.Adapter<typeListAdapter.ViewHolder>{

    private List<String> mTypeLists;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nameTextView;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            nameTextView = view.findViewById(R.id.type_list_name_title);
        }

        private String mTypeName;

        @Override
        public void onClick(View v){

            Intent intent = new Intent(v.getContext(), RemindersByTypeActivity.class);
            intent.putExtra("type_name", mTypeName);
            v.getContext().startActivity(intent);

        }

    }

    public typeListAdapter(DataBaseManager rlm){
        mTypeLists = rlm.getTypes_arrlist();
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
        String typeName = mTypeLists.get(position);
        holder.nameTextView.setText(typeName);
        holder.mTypeName = typeName;

    }

    @Override
    public int getItemCount() {
        return mTypeLists.size();
    }

}
