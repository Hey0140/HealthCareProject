package com.example.myjavaapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class PetmanageFragment extends Fragment implements View.OnClickListener{
    MainActivity mainActivity;

    private Button petStatusButton;
    private Button petChangeButton;
    private TextView name;
    private View monStatusBox;

    public PetmanageFragment() {
        // Required empty public constructor
    }

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_petmanage, container, false);

        petStatusButton = view.findViewById(R.id.petManagementStatusButton);
        name = view.findViewById(R.id.petManagementName);
        monStatusBox = view.findViewById(R.id.petManagementDayMonBox);
        petChangeButton = view.findViewById(R.id.petManagementChangeButton);

        name.setText("초코");

        petChangeButton.setOnClickListener(this);
        petStatusButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == petChangeButton){

        }
        if(v == petStatusButton){
            mainActivity.onChangeToPetStatusFragment();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

}