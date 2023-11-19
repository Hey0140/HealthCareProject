package com.example.myjavaapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PetKindSelectedDialog extends Dialog implements View.OnClickListener {
    private Context context;
    View smallPet;
    View middlePet;
    View bigPet;
    private OnPetKindDialogListener kindDialogListener;

    public void setKindDialogListener(OnPetKindDialogListener onPetKindDialogListener){
        this.kindDialogListener = onPetKindDialogListener;
    }

    public PetKindSelectedDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_petkind_select);
        smallPet = findViewById(R.id.smallSizePet);
        middlePet = findViewById(R.id.middleSizePet);
        bigPet = findViewById(R.id.bigSizePet);

        smallPet.setOnClickListener(this);
        middlePet.setOnClickListener(this);
        bigPet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == smallPet){
            kindDialogListener.onPetKindSelected("소형견");
            dismiss();
        }
        if(v == middlePet){
            kindDialogListener.onPetKindSelected("중형견");
            dismiss();
        }
        if(v == bigPet){
            kindDialogListener.onPetKindSelected("대형견");
            dismiss();
        }
    }

    public interface OnPetKindDialogListener{
        void onPetKindSelected(String data);
    }
}
