package com.example.myjavaapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MypageFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth auth;

    private ArrayList<MyPetInfoData> list= new ArrayList<>();

    private Button logoutButton;
    private MypetInfoAdapter adapter;

    private UserMedia userData;
    private ArrayList<PetMedia> petDataList;
    private TextView profileName, profileEmail;
    private ImageView profileImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        logoutButton = view.findViewById(R.id.logoutButton);
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profileImage = view.findViewById(R.id.profileImage);

        logoutButton.setOnClickListener(this);

        petDataList = new ArrayList<>();

        userData = (UserMedia) getActivity().getIntent().getSerializableExtra("userData");
        petDataList = (ArrayList<PetMedia>) getActivity().getIntent().getSerializableExtra("petDataList");

        if(userData.getImage() != null){
            if(!userData.getImage().equals("")){
                profileImage.setImageURI(Uri.parse(userData.getImage()));
            }
        }
        profileName.setText(userData.getName());
        profileEmail.setText(userData.getEmail());

        list.clear();

        for (int i = 0; i < petDataList.size(); i++) {
            MyPetInfoData mpid = new MyPetInfoData();
            PetMedia petMedia = petDataList.get(i);
            mpid.setName(petMedia.getPetName());
            mpid.setImageId(R.drawable.mypetprofile_basic_icon);
            mpid.setViewType(Code.ViewType.BASIC);
            Log.i("check", petMedia.getPetName());
            list.add(mpid);
        }

        lastAddData();

        RecyclerView recyclerView = view.findViewById(R.id.myPetInfoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        adapter = new MypetInfoAdapter(list);
        adapter.setOnItemClickListener(new MypetInfoAdapter.OnListItemSelected() {
            @Override
            public void onItemSelected(View v, int position, int vg) {
                        if(vg == Code.ViewType.BASIC){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("확인창")
                                    .setMessage("삭제하시겠습니까?")
                                    .setCancelable(false)
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            PetMedia data = petDataList.get(position);
                                            onSetDeletePet(data, position);
                                        }
                                    })
                                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        if(vg == Code.ViewType.ADD){
                            Intent intent = new Intent(getActivity(), PetAddActivity.class);
                            intent.putExtra("userData", userData);
                            intent.putExtra("petDataList", petDataList);
                            startActivity(intent);
                        }
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void lastAddData(){
        MyPetInfoData mpid = new MyPetInfoData();
        mpid.setViewType(Code.ViewType.ADD);
        list.add(mpid);
    }

    @Override
    public void onClick(View v) {
        if( v == logoutButton){
            auth.signOut();
            Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    public void onSetDeletePet(PetMedia pet, int position){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String id = pet.getuId();
        String pid = String.valueOf(pet.getPetId());

        db.collection("users").document(id)
                .collection("pet").document(pid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "DocumentSnapshot successfully deleted!");
                        petDataList.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firestore", "Error deleting document", e);
                    }
                });

    }

}