package com.example.myjavaapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MypageFriendDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private int previous = 0;
    private ArrayList<MypageFriendItemData> itemList = new ArrayList<>();
    private ArrayList<String> frUidList = new ArrayList<>();
    private ArrayList<MypageFriendItemData> searchItemList = new ArrayList<>();
    private MypageFriendDialog.OnMyfriendDialogListener myfriendDialogListener;

    private MypageFriendAdapter frAdapter;
    private MypageFriendSearchAdapter frSearAdapter;
    private UserMedia userData = new UserMedia();
    private int friendPosition = 0;

    public MypageFriendDialog(@NonNull Context context, UserMedia userData) {
        super(context);
        this.context = context;
        this.userData = userData;
    }

    public interface OnMyfriendDialogListener{
        void onItemSelected(MypageFriendItemData friendData);
    }

    TextView myFriend, searchMyFriend;
    ConstraintLayout myFriendLayout, searchMyFriendLayout, searchLayout;
    EditText searchText;
    View searchButton, cancelButton;
    RecyclerView recyclerView;

    public void setMyFriendDialogListener(MypageFriendDialog.OnMyfriendDialogListener onMypageFriendDialogListener){
        this.myfriendDialogListener = onMypageFriendDialogListener;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_myfriendcommunity);

        searchText = findViewById(R.id.myfriendcommunitySearchText);
        searchButton = findViewById(R.id.myfriendcommunitySearchIcon);
        cancelButton = findViewById(R.id.myfriendcommunitySearchCancelButtton);
        recyclerView = findViewById(R.id.myfriendcommunityRecyclerView);
        myFriend = findViewById(R.id.myFriendShowText);
        searchMyFriend = findViewById(R.id.myFriendSearchText);

        myFriendLayout = findViewById(R.id.myfriendShowLayout);
        searchMyFriendLayout = findViewById(R.id.myfriendShow2Layout);
        searchLayout = findViewById(R.id.myFriendSearchLayout);

        cancelButton.setVisibility(View.INVISIBLE);

        myFriend.setTextColor(Color.parseColor("#6496FF"));
        searchMyFriend.setTextColor(Color.parseColor("#1C1C1C"));
        searchLayout.setVisibility(View.GONE);

        cancelButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        myFriendLayout.setOnClickListener(this);
        searchMyFriendLayout.setOnClickListener(this);

        itemList.clear();
        searchItemList.clear();
        frUidList.clear();
        previous = 0;

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        frAdapter = new MypageFriendAdapter(itemList, context);
        frAdapter.setOnDeleteClickListener(new MypageFriendAdapter.OnListDeleteSelected() {
            @Override
            public void onDeleteSelected(View v, int position) {
                View delete = v.findViewById(R.id.myfriendDeleteIcon);
                MypageFriendItemData data = itemList.get(position);

                deleteMyFriend(data, position);

            }
        });

        frSearAdapter = new MypageFriendSearchAdapter(searchItemList, context);
        frSearAdapter.setOnAddClickListener(new MypageFriendSearchAdapter.OnListAddSelected() {
            @Override
            public void onAddSelected(View v, int position) {
                View add = v.findViewById(R.id.myfriendAddIcon);
                MypageFriendItemData data = searchItemList.get(position);

                addMyFriend(data, position);

            }
        });

        recyclerView.setAdapter(frAdapter);

        getFirebaseToMyFriendCommunity();
    }

    @Override
    public void onClick(View v) {
        if( v == searchButton ){
            String search = searchText.getText().toString();
            cancelButton.setVisibility(View.VISIBLE);

            int count = searchItemList.size();
            searchItemList.clear();
            frSearAdapter.notifyItemRangeRemoved(0, count);

            getSearchFirebaseToCommunity(search);
        }
        if(v == cancelButton){
            cancelButton.setVisibility(View.INVISIBLE);

            int count = searchItemList.size();
            searchItemList.clear();
            frSearAdapter.notifyItemRangeRemoved(0, count);
            getFirebaseToCommunity();
        }
        if (v == myFriendLayout){
            if(previous == 1){
                previous = 0;
                myFriend.setTextColor(Color.parseColor("#6496FF"));
                searchMyFriend.setTextColor(Color.parseColor("#1C1C1C"));
                searchLayout.setVisibility(View.GONE);

                int size = itemList.size();
                itemList.clear();
                frUidList.clear();
                frAdapter.notifyItemRangeRemoved(0, size);

                recyclerView.setAdapter(frAdapter);
//                recyclerView.swapAdapter(frSearAdapter, true);

                getFirebaseToMyFriendCommunity();
            }
        }
        if(v == searchMyFriendLayout){
            if(previous == 0){
                previous = 1;

                myFriend.setTextColor(Color.parseColor("#1C1C1C"));
                searchMyFriend.setTextColor(Color.parseColor("#6496FF"));
                searchLayout.setVisibility(View.VISIBLE);

                int size = searchItemList.size();
                searchItemList.clear();
                frSearAdapter.notifyItemRangeRemoved(0, size);
//                recyclerView.swapAdapter(frSearAdapter, true);

                recyclerView.setAdapter(frSearAdapter);
                getFirebaseToCommunity();
            }
        }
    }

    private void getFirebaseToMyFriendCommunity() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(userData.getUid())
                .collection("friend")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                String uid = userData.getUid();

                                String frUid = document.getId();
                                frUidList.add(frUid);
                            }


                            db.collection("users")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Map<String, Object> map = document.getData();

                                                    String uid = userData.getUid(); //내 아이디

                                                    String frUid = document.getId(); //다른 얘 아이디;

                                                    String name= (String) map.get("name");
                                                    String email = (String) map.get("email");
                                                    String image = (String) map.get("image");

                                                    if(frUidList.contains(frUid)){
                                                        //내친구 정보 가져오기
                                                        MypageFriendItemData data = new MypageFriendItemData();
                                                        data.setMyUid(uid);
                                                        data.setFrName(name);
                                                        data.setFrUid(frUid);
                                                        data.setFrEmail(email);
                                                        data.setFrImageUrl(image);

                                                        itemList.add(data);
                                                    }

                                                }

                                                frAdapter.notifyItemRangeInserted(0, itemList.size());

                                                Log.d("Firesotre", "successs");
                                            } else {
                                                Log.d("Firesotre", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });




                            Log.d("Firesotre", "successs");
                        } else {
                            Log.d("Firesotre", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void getFirebaseToCommunity(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
        .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();

                                String uid = userData.getUid(); //내 아이디

                                String frUid = document.getId(); //다른 얘 아이디;
                                String name= (String) map.get("name");
                                Log.i("check", name);
                                String email = (String) map.get("email");
                                String image = (String) map.get("image");

                                if(!uid.equals(frUid)){
                                    //혹시라도 내 아이디인 경우 스킵
                                    MypageFriendItemData data = new MypageFriendItemData();
                                    data.setMyUid(uid);
                                    data.setFrName(name);
                                    data.setFrUid(frUid);
                                    data.setFrEmail(email);
                                    data.setFrImageUrl(image);
                                    if(!frUidList.contains(frUid)){
                                        searchItemList.add(data);
                                    }

                                }

                            }
                            frSearAdapter.notifyItemRangeInserted(0, searchItemList.size());

                            Log.d("Firesotre", "successs");
                        } else {
                            Log.d("Firesotre", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getSearchFirebaseToCommunity(String name){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference cr = db.collection("users");

        Query query = cr.whereEqualTo("name", name);

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("Firesotre", "successs");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Firesotre", "successs22");
                                Map<String, Object> map = document.getData();
                                String uid = userData.getUid();

                                String frUid = document.getId();
                                String name= (String) map.get("name");
                                String email = (String) map.get("email");
                                String image = (String) map.get("image");

                                if(!uid.equals(frUid)){

                                    MypageFriendItemData data = new MypageFriendItemData();
                                    data.setMyUid(uid);
                                    data.setFrName(name);
                                    data.setFrUid(frUid);
                                    data.setFrEmail(email);
                                    data.setFrImageUrl(image);
                                    if(!frUidList.contains(frUid)){
                                        searchItemList.add(data);
                                    }
                                }

                            }
                            frSearAdapter.notifyItemRangeInserted(0, searchItemList.size());

                            Log.d("Firesotre", "successs");
                        } else {
                            Log.d("Firesotre", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void addMyFriend(MypageFriendItemData data, int position){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> hashmap = new HashMap<>();

        hashmap.put("uid", userData.getUid());
        hashmap.put("frid", data.getFrUid());
        hashmap.put("name", data.getFrName());
        hashmap.put("email", data.getFrEmail());
        hashmap.put("image", data.getFrImageUrl());

        db.collection("users").document(userData.getUid())
                .collection("friend").document(data.getFrUid())
                .set(hashmap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        frUidList.add(data.getFrUid());
                        itemList.add(data);
                        frAdapter.notifyItemInserted(itemList.size()-1);
                        searchItemList.remove(position);
                        frSearAdapter.notifyItemRemoved(position);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    public void deleteMyFriend(MypageFriendItemData data, int position){
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("users").document(userData.getUid())
                .collection("friend").document(data.getFrUid())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        frUidList.remove(data.getFrUid());
                        itemList.remove(position);
                        frAdapter.notifyItemRemoved(position);
                        searchItemList.add(data);
                        frSearAdapter.notifyItemInserted(searchItemList.size()-1);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        }
}

