package com.example.myjavaapplication;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MonthWalkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<MonthWalkMedia> itemList;
    private MonthWalkAdapter.OnListItemSelected mListener;
    public interface OnListItemSelected {
        void onItemSelected(View v, int position);
    }

    MonthWalkAdapter(ArrayList<MonthWalkMedia> list) {
        this.itemList = list;
    }

    public void setOnItemClickListener(MonthWalkAdapter.OnListItemSelected listener) {
        this.mListener = listener;
    }

    public class MonthWalkViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView text;

        public MonthWalkViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.dayWalkItemView);
            text = itemView.findViewById(R.id.dayWalkItemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemSelected(view, position);
                        }
                    }
                }
            });
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.daywalk_item_layout, parent, false);
        return new MonthWalkAdapter.MonthWalkViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MonthWalkMedia item = itemList.get(position);
        if( item.getType() == Code.ViewType.BITMAP){
            if(item.getImage() == null){

            } else {
                ((MonthWalkViewHolder) holder).text.setVisibility(View.INVISIBLE);
                ((MonthWalkViewHolder) holder).imageView.setImageBitmap(item.getBitmap());
            }
        }
        if( item.getType() == Code.ViewType.URI){
            if(item.getImage().equals("")){

            } else {
                ((MonthWalkViewHolder) holder).text.setVisibility(View.INVISIBLE);
                ((MonthWalkViewHolder) holder).imageView.setImageURI(Uri.parse(item.getImage()));
            }
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
