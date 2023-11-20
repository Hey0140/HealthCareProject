package com.example.myjavaapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PetVaccineSelectedDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private HashMap<String, Boolean> vaccineList = new HashMap<>();
    private ArrayList<PetVaccineData> itemList = new ArrayList<>();
    private OnVaccineDialogListener vaccineDialogListener;
    public interface OnVaccineDialogListener{
        void onVaccineSelected(HashMap<String, Boolean> data);
    }

    Button endButton;

    public void setVaccineDialogListener(OnVaccineDialogListener onVaccineDialogListener){
        this.vaccineDialogListener = onVaccineDialogListener;
    }

    public PetVaccineSelectedDialog(@NonNull Context context, HashMap<String, Boolean> hashMap) {
        super(context);
        this.context = context;
        this.vaccineList = hashMap;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_petvaccine_select);
        endButton = findViewById(R.id.vaccineEndButton);
        endButton.setOnClickListener(this);

        itemList.clear();
        setItemList();
//        lastAddData();

        RecyclerView recyclerView = findViewById(R.id.petVaccineRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        PetVaccineAdapter adapter = new PetVaccineAdapter(itemList);
        adapter.setOnItemClickListener(new PetVaccineAdapter.OnListItemSelected() {
            @Override
            public void onItemSelected(View v, int position, int vg) {
                if(vg == Code.ViewType.BASIC){
                    CheckBox check = (CheckBox) v;
                    String name = itemList.get(position).getVaccineName();
                    if(check.isChecked()){
                        vaccineList.put(name, true);
                    }
                    else{
                        vaccineList.put(name, false);
                    }
                }
//                if(vg == Code.ViewType.ADD){
//                    VaccineAddDialog vaccineAddDialog = new VaccineAddDialog(getContext());
//                    vaccineAddDialog.setVaccineAddDialogListener(new VaccineAddDialog.OnVaccineAddDialogListener() {
//                        @Override
//                        public void onVaccineAddCompleted(String data) {
//                            addListData(false, data);
//                        }
//                    });
//                    vaccineAddDialog.show();
//                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if( v == endButton){
            vaccineDialogListener.onVaccineSelected(vaccineList);
            dismiss();
        }
    }


    private void setItemList(){
        for(Map.Entry<String, Boolean> hashMap : vaccineList.entrySet()){
            PetVaccineData pvd = new PetVaccineData();
            String name = hashMap.getKey();
            boolean check = hashMap.getValue();
            pvd.setCheck(check);
            pvd.setVaccineName(name);
            pvd.setViewtype(Code.ViewType.BASIC);
            itemList.add(pvd);
        }
    }

//    private void lastAddData() {
//        PetVaccineData pvd = new PetVaccineData();
//        pvd.setViewtype(Code.ViewType.ADD);
//        itemList.add(pvd);
//    }
//
//    private void addListData(boolean check, String name){
//        int size = itemList.size();
//        if(size > 0){
//            PetVaccineData pvd = new PetVaccineData();
//            pvd.setCheck(check);
//            pvd.setVaccineName(name);
//            pvd.setViewtype(Code.ViewType.BASIC);
//            itemList.add(size-1, pvd);
//        }
//    }

}
