package com.example.myjavaapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WalkRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<WalkRecordData> itemList = new ArrayList<>();
    WalkRecordAdapter(ArrayList<WalkRecordData> list) {
        this.itemList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.walk_item_layout, parent, false);
        return new WalkRecordAdapter.WalkRecordViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WalkRecordData item = itemList.get(position);

        Log.i("check112", item.getStartTime());
        Log.i("check112", item.getEndTime());
        Log.i("check112", item.getDuringTime());

        ((WalkRecordAdapter.WalkRecordViewHolder) holder).startTime.setText(item.getStartTime());
        ((WalkRecordAdapter.WalkRecordViewHolder) holder).endTime.setText(item.getEndTime());
        ((WalkRecordAdapter.WalkRecordViewHolder) holder).duringTime.setText(item.getDuringTime());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public class WalkRecordViewHolder extends RecyclerView.ViewHolder {
        TextView startTime;
        TextView endTime;
        TextView duringTime;

        public WalkRecordViewHolder(View itemView) {
            super(itemView);

            startTime = itemView.findViewById(R.id.walkStartTime);
            endTime = itemView.findViewById(R.id.walkEndTime);
            duringTime = itemView.findViewById(R.id.walkDuringTime);
        }
    }

}
