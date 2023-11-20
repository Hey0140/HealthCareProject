package com.example.myjavaapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class PetStatusFragment extends Fragment implements View.OnClickListener {
    private MainActivity mainActivity;

    private View arrow, backButton;
    public PetStatusFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.petstatus_page, container, false);

        backButton = view.findViewById(R.id.petStatusBackButton);

        backButton.setOnClickListener(this);

        return view;
    }
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }


    @Override
    public void onClick(View v) {
        if(v == backButton){
            mainActivity.onChangeToPetManageFragment();
        }
    }

}