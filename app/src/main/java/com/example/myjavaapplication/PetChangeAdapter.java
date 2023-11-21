package com.example.myjavaapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PetChangeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<PetChangeData> itemList;
    private PetChangeAdapter.OnListItemSelected mListener;

    public interface OnListItemSelected {
        void onItemSelected(View v, int position);
    }

    PetChangeAdapter(ArrayList<PetChangeData> list) {
        this.itemList = list;
    }

    public void setOnItemClickListener(PetChangeAdapter.OnListItemSelected listener) {
        this.mListener = listener;
    }

    public class PetChangeViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public PetChangeViewHolder(View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.petchane_Name);

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
        v = inflater.inflate(R.layout.petchange_item_layout, parent, false);
        return new PetChangeAdapter.PetChangeViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PetChangeData item = itemList.get(position);

        ((PetChangeAdapter.PetChangeViewHolder) holder).name.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

