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

    public PetVaccineSelectedDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_petvaccine_select);
        endButton = findViewById(R.id.vaccineEndButton);
        endButton.setOnClickListener(this);

        itemList.clear();
        setItemList();
        lastAddData();

        RecyclerView recyclerView = findViewById(R.id.petVaccineRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        PetVaccineAdapter adapter = new PetVaccineAdapter(itemList);
        adapter.setOnItemClickListener(new PetVaccineAdapter.OnListItemSelected() {
            @Override
            public void onItemSelected(View v, int position, int vg) {
                if(vg == Code.ViewType.BASIC){
                    CheckBox check = v.findViewById(R.id.vaccineCheck);
                    TextView text = v.findViewById(R.id.vaccineName);
                    if(check.isChecked()){
                        vaccineList.put(text.getText().toString(), true);
                    }
                    else{
                        vaccineList.put(text.getText().toString(), false);
                    }
                }
                if(vg == Code.ViewType.ADD){
                    VaccineAddDialog vaccineAddDialog = new VaccineAddDialog(getContext());
                    vaccineAddDialog.setVaccineAddDialogListener(new VaccineAddDialog.OnVaccineAddDialogListener() {
                        @Override
                        public void onVaccineAddCompleted(String data) {
                            addListData(false, data);
                        }
                    });
                    vaccineAddDialog.show();
                }
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
        for(int i = 1; i <=5; i++){
            String name = "종합백신 "+i+"차 (생후"+(i*2+4)+"주차)";
            PetVaccineData pvd = new PetVaccineData();
            pvd.setCheck(false);
            pvd.setVaccineName(name);
            pvd.setViewtype(Code.ViewType.BASIC);
            itemList.add(pvd);
            vaccineList.put(name, false);
        }
        for(int i = 1; i <=2; i++){
            String name = "코로나 장염 "+i+"차 (생후 "+(i*2+4)+"주차)";
            PetVaccineData pvd = new PetVaccineData();
            pvd.setCheck(false);
            pvd.setVaccineName(name);
            pvd.setViewtype(Code.ViewType.BASIC);
            itemList.add(pvd);
            vaccineList.put(name, false);
        }
        for(int i = 1; i <=2; i++){
            String name = "켄넬코프 "+i+"차 (생후 "+(i*2+8)+"주차)";
            PetVaccineData pvd = new PetVaccineData();
            pvd.setCheck(false);
            pvd.setVaccineName(name);
            pvd.setViewtype(Code.ViewType.BASIC);
            itemList.add(pvd);
            vaccineList.put(name, false);
        }
        PetVaccineData pvd = new PetVaccineData();
        pvd.setCheck(false);
        pvd.setVaccineName("광견병 1차 (생후 12주차)");
        pvd.setViewtype(Code.ViewType.BASIC);
        itemList.add(pvd);
        vaccineList.put("광견병 1차 (생후 12주차)", false);
        pvd = new PetVaccineData();
        pvd.setCheck(false);
        pvd.setVaccineName("광견병 2차 (생후 1년차)");
        pvd.setViewtype(Code.ViewType.BASIC);
        itemList.add(pvd);
        vaccineList.put("광견병 1차 (생후 12주차)", false);

        for(int i = 1; i <=2; i++){
            String name = "인플루엔자 "+i+"차 (생후 "+(i*2+12)+"주차)";
            pvd = new PetVaccineData();
            pvd.setCheck(false);
            pvd.setVaccineName(name);
            pvd.setViewtype(Code.ViewType.BASIC);
            itemList.add(pvd);
            vaccineList.put(name, false);
        }
    }
    private void lastAddData() {
        PetVaccineData pvd = new PetVaccineData();
        pvd.setViewtype(Code.ViewType.ADD);
        itemList.add(pvd);
    }

    private void addListData(boolean check, String name){
        int size = itemList.size();
        if(size > 0){
            PetVaccineData pvd = new PetVaccineData();
            pvd.setCheck(check);
            pvd.setVaccineName(name);
            pvd.setViewtype(Code.ViewType.BASIC);
            itemList.add(size-1, pvd);
        }
    }

}
