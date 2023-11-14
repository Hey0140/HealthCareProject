package com.example.myjavaapplication;

import android.view.ContentInfo;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

public class MypetInfoViewHolder  extends  RecyclerView.ViewHolder{
    TextView name;
    ImageView image;
    public MypetInfoViewHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.myPetinfoName);
        image = itemView.findViewById(R.id.myPetInfoProfile);

    }
}
