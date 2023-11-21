package com.example.myjavaapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PetChangeDialog extends Dialog {
    private Context context;
    private ArrayList<String> list;
    private ArrayList<PetChangeData> itemList = new ArrayList<>();
    private OnChangeDialogListener changeDialogListener;
    private  String data;
    public interface OnChangeDialogListener{
        void onChangeSelected(String data);
    }

    public void setChangeDialogListener(OnChangeDialogListener changeDialogListener) {
        this.changeDialogListener = changeDialogListener;
    }

    public PetChangeDialog(@NonNull Context context, ArrayList<String>list) {
        super(context);
        this.context=context;
        this.list=list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_petchange);
        RecyclerView recyclerView = findViewById(R.id.Petchange_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        PetChangeAdapter adapter = new PetChangeAdapter(itemList);
        adapter.setOnItemClickListener(new PetChangeAdapter.OnListItemSelected() {
            @Override
            public void onItemSelected(View v, int position) {
                String name = itemList.get(position).getName();
                changeDialogListener.onChangeSelected(data);
                dismiss();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
