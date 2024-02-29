package com.tomal.attandenceapp.adapters.usersAttandence;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tomal.attandenceapp.R;

import java.util.ArrayList;

public class UserAttandenceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_EMPTY = 0;
    private final int VIEW_TYPE_ITEM = 1;
    private ArrayList<UserAttandenceAdapterModel> arrayList = new ArrayList<>();
    public UserAttandenceAdapter(ArrayList<UserAttandenceAdapterModel> arrayList) {
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_data, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_data_empty, parent, false);
            return new EmptyStateViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            UserAttandenceAdapterModel data = arrayList.get(position);
            ((ItemViewHolder) holder).bind(data);
        }
    }
    @Override
    public int getItemViewType(int position) {
        return arrayList.isEmpty() ? VIEW_TYPE_EMPTY : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return arrayList.isEmpty() ? 1 : arrayList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView date, time, name;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
        }

        public void bind(UserAttandenceAdapterModel model) {
            date.setText(String.format("Date: %s", model.getDate()));
            time.setText(String.format("Time: %s", model.getTime()));
            name.setText(String.format("Name: %s", model.getName()));

        }
    }

    private static class EmptyStateViewHolder extends RecyclerView.ViewHolder {
        public EmptyStateViewHolder(View view) {
            super(view);
        }
    }
}
