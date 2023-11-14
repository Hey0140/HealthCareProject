package com.example.myjavaapplication;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MypetInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<MyPetInfoData> itemList;

    MypetInfoAdapter(ArrayList<MyPetInfoData> list){
        itemList = list;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(viewType == Code.ViewType.BASIC){
            v = inflater.inflate(R.layout.mypet_info_item_layout, parent, false);
            return new MypetInfoViewHolder(v);
        }
        else {
            v = inflater.inflate(R.layout.mypet_info_lastitem_layout, parent, false);
            return new MypetInfoAddViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MypetInfoViewHolder){
            MyPetInfoData item = itemList.get(position);

            ((MypetInfoViewHolder) holder).image.setImageResource(item.getImageId());
            ((MypetInfoViewHolder) holder).name.setText(item.getName());
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position){
        return itemList.get(position).getViewType();
    }
}
