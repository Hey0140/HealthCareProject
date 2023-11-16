package com.example.myjavaapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MypetInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<MyPetInfoData> itemList;
    private OnListItemSelected mListener;
    public interface OnListItemSelected {
        void onItemSelected(View v, int position, int vg);
    }
    MypetInfoAdapter(ArrayList<MyPetInfoData> list){
        this.itemList = list;
    }
    public void setOnItemClickListener(OnListItemSelected listener){
        this.mListener = listener;
    }

    public class MypetInfoViewHolder  extends  RecyclerView.ViewHolder{
        TextView name;
        ImageView image;
        public MypetInfoViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.myPetInfoName);
            image = itemView.findViewById(R.id.myPetInfoProfile);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemSelected(view, position, Code.ViewType.BASIC);
                        }
                    }
                }
            });
        }
    }

    public class MypetInfoAddViewHolder  extends  RecyclerView.ViewHolder{
        public MypetInfoAddViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemSelected(view, position, Code.ViewType.ADD);
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
