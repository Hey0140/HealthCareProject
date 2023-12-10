package com.example.myjavaapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NameChangeDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private OnChangeDialogListener changeDialogListener;
    private EditText text;
    private Button button;



    public interface OnChangeDialogListener{
        void onChangeSelected(String data);
    }

    public void setChangeDialogListener(OnChangeDialogListener changeDialogListener) {
        this.changeDialogListener = changeDialogListener;
    }

    public NameChangeDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setname);

        text = findViewById(R.id.nameChangeText);
        button = findViewById(R.id.nameChangeButton);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == button){
            String newName = text.getText().toString();
            changeDialogListener.onChangeSelected(newName);
            dismiss();
        }
    }
}
