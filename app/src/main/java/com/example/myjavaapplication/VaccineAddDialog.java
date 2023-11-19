package com.example.myjavaapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class VaccineAddDialog extends Dialog implements View.OnClickListener {

    private Context context;
    EditText vaccineAdd;
    Button vaccineAddButton;

    private VaccineAddDialog.OnVaccineAddDialogListener vaccineAddDialogListener;

    public void setVaccineAddDialogListener(VaccineAddDialog.OnVaccineAddDialogListener onVaccineAddDialogListener){
        this.vaccineAddDialogListener = onVaccineAddDialogListener;
    }

    public interface OnVaccineAddDialogListener{
        void onVaccineAddCompleted(String data);
    }

    public VaccineAddDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_vaccineadd_select);

        vaccineAdd = findViewById(R.id.vaccineAddName);
        vaccineAddButton = findViewById(R.id.vaccineAddButton);

        vaccineAddButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == vaccineAddButton){
            vaccineAddDialogListener.onVaccineAddCompleted(vaccineAdd.getText().toString());
            dismiss();
        }
    }

}