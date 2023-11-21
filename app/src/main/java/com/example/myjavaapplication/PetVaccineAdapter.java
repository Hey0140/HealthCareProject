package com.example.myjavaapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PetVaccineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    ArrayList<PetVaccineData> itemList;
    private PetVaccineAdapter.OnListItemSelected mListener;
    public interface OnListItemSelected {
        void onItemSelected(View v, int position, int vg);
    }
    PetVaccineAdapter(ArrayList<PetVaccineData> list){
        this.itemList = list;
    }

    public void setOnItemClickListener(PetVaccineAdapter.OnListItemSelected listener){
        this.mListener = listener;
    }

    public class PetVaccineViewHolder  extends  RecyclerView.ViewHolder{
        CheckBox vaccineCheck;
        TextView vaccineName;

        public PetVaccineViewHolder(View itemView) {
            super(itemView);

            vaccineCheck = itemView.findViewById(R.id.vaccineCheck);
            vaccineName = itemView.findViewById(R.id.vaccineName);

            vaccineCheck.setOnClickListener(new View.OnClickListener() {
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

    public class PetVaccineAddViewHolder  extends  RecyclerView.ViewHolder{
        public PetVaccineAddViewHolder(@NonNull View itemView) {
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
            v = inflater.inflate(R.layout.vaccine_item_layout, parent, false);
            return new PetVaccineAdapter.PetVaccineViewHolder(v);
        }
        else {
            v = inflater.inflate(R.layout.vaccine_lastitem_layout, parent, false);
            return new PetVaccineAdapter.PetVaccineAddViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PetVaccineAdapter.PetVaccineViewHolder){
            PetVaccineData item = itemList.get(position);


            ((PetVaccineAdapter.PetVaccineViewHolder) holder).vaccineCheck.setChecked(item.isCheck());
            ((PetVaccineAdapter.PetVaccineViewHolder) holder).vaccineName.setText(item.getVaccineName());
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position){
        return itemList.get(position).getViewtype();
    }
}
