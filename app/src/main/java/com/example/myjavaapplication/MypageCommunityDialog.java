package com.example.myjavaapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.auth.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MypageCommunityDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private ArrayList<CommunityItemData> itemList = new ArrayList<>();
    private ArrayList<String> likeList = new ArrayList<>();
    private OnMypageCommunityDialogListener mypageCommunityDialogListener;
    private MypageCommunityAdapter adapter;
    private UserMedia userData = new UserMedia();
    private int communityPosition = 0;

    public MypageCommunityDialog(@NonNull Context context, UserMedia userData) {
        super(context);
        this.context = context;
        this.userData = userData;
    }

    public interface OnMypageCommunityDialogListener{
        void onItemSelected(CommunityMedia communityMedia);
    }

    EditText searchText;
    View searchButton, cancelButton;

    public void setMypageCommunityDialogListener(MypageCommunityDialog.OnMypageCommunityDialogListener onMypageCommunityDialogListener){
        this.mypageCommunityDialogListener = onMypageCommunityDialogListener;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_mycommunity);

        searchText = findViewById(R.id.mycommunitySearchText);
        searchButton = findViewById(R.id.mycommunitySearchIcon);
        cancelButton = findViewById(R.id.mycommunitySearchCancelButtton);

        cancelButton.setVisibility(View.INVISIBLE);

        cancelButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);

        itemList.clear();

        RecyclerView recyclerView = findViewById(R.id.mycommunityRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        adapter = new MypageCommunityAdapter(itemList, getContext());
        adapter.setOnHeartClickListener(new MypageCommunityAdapter.OnListHeartSelected() {
            @Override
            public void onHeartSelected(View v, int position, int vg) {
                if(vg == Code.ViewType.TEXT){
                    View heart = v.findViewById(R.id.communityHeartButton4);
                    CommunityItemData data = itemList.get(position);
                    if(data.isHeart()){
                        itemList.get(position).setHeart(false);
                        heart.setBackgroundResource(R.drawable.not_fill_favorite_icon);
                        CommunityItemData co = itemList.get(position);
                        itemList.get(position).setHeart(false);

                        setFalseHeartOnFirebase(itemList.get(position).getComId(), co, position);
                    }
                    else{
                        itemList.get(position).setHeart(true);
                        heart.setBackgroundResource(R.drawable.filled_favorite_icon);
                        CommunityItemData co = itemList.get(position);;
                        itemList.get(position).setHeart(true);
                        communityPosition = position;

                        setTrueHeartOnFirebase(itemList.get(position).getComId(), co, position);
                    }
                }
                if(vg == Code.ViewType.IMAGE){
                    View heart = v.findViewById(R.id.communityHeartButton3);
                    CommunityItemData data = itemList.get(position);
                    if(data.isHeart()){
                        itemList.get(position).setHeart(false);
                        heart.setBackgroundResource(R.drawable.not_fill_favorite_icon);
                        CommunityItemData co = itemList.get(position);
                        communityPosition = position;

                        setFalseHeartOnFirebase(itemList.get(position).getComId(), co, position);
                    }
                    else{
                        itemList.get(position).setHeart(true);
                        heart.setBackgroundResource(R.drawable.filled_favorite_icon);
                        CommunityItemData co = itemList.get(position);
                        communityPosition = position;

                        setTrueHeartOnFirebase(itemList.get(position).getComId(), co, position);
                    }
                }

            }
        });
        adapter.setOnItemClickListener(new MypageCommunityAdapter.OnListItemSelected() {
            @Override
            public void onItemSelected(View v, int position, int vg) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("확인창")
                        .setMessage("삭제하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                CommunityItemData data = itemList.get(position);
                                deleteCommunity(data.getComId(), data, position);
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
        });
        recyclerView.setAdapter(adapter);

        getFirebaseToCommunity();
    }

    @Override
    public void onClick(View v) {
        if( v == searchButton ){
            String search = searchText.getText().toString();
            cancelButton.setVisibility(View.VISIBLE);

            int count = itemList.size();
            itemList.clear();
            adapter.notifyItemRangeRemoved(0, count);
            getSearchFirebaseToCommunity(search);
        }
        if(v == cancelButton){
            cancelButton.setVisibility(View.INVISIBLE);

            int count = itemList.size();
            itemList.clear();
            adapter.notifyItemRangeRemoved(0, count);
            getFirebaseToCommunity();
        }
    }


    private void getFirebaseToCommunity(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference cr = db.collection("community");

        Query query = cr.whereEqualTo("uid", userData.getUid());
        query.orderBy("timeStamp", Query.Direction.DESCENDING );
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> map = document.getData();
                        String uid = (String) map.get("uid");
                        String comId = document.getId();
                        String title = (String) map.get("title");
                        String body = (String) map.get("body");
                        String image = (String) map.get("image");
                        boolean heart = (Boolean) map.get("heart");
                        long heartNumber = (Long) map.get("heartNumber");
                        long commentNumber = (Long) map.get("commentNumber");

                        if(!image.equals("")){
                            CommunityItemData data = new CommunityItemData(Code.ViewType.IMAGE, image);
                            data.setTitle(title);
                            data.setBody(body);
                            data.setComId(comId);
                            data.setHeartNumber(heartNumber);
                            data.setCommentNumber(commentNumber);
                            data.setImageUri(image);
                            itemList.add(data);
                        }
                        else{
                            CommunityItemData data = new CommunityItemData(Code.ViewType.TEXT, "");
                            data.setTitle(title);
                            data.setBody(body);
                            data.setComId(comId);
                            data.setHeartNumber(heartNumber);
                            data.setCommentNumber(commentNumber);
                            itemList.add(data);
                        }
                    }
                    adapter.notifyItemRangeInserted(0, itemList.size());

                    getLikeDataToFirebase();
                    Log.d("Firesotre", "successs");
                } else {
                    Log.d("Firesotre", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void getLikeDataToFirebase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("communityUser").document(userData.getUid())
                .collection("like")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String comId = document.getId();
                                likeList.add(comId);
                            }


                            for(int i = 0; i < itemList.size(); i++){
                                if(likeList.contains(itemList.get(i).getComId())){
                                    itemList.get(i).setHeart(true);
                                    adapter.notifyItemChanged(i);
                                }
                                else{
                                    itemList.get(i).setHeart(false);
                                    adapter.notifyItemChanged(i);
                                }
                            }
                        } else {
                            Log.d("Firesotre", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getSearchFirebaseToCommunity(String title){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference cr = db.collection("community");

        Query query = cr.whereEqualTo("title", title);
        query.whereEqualTo("uid", userData.getUid());
        query.orderBy("timeStamp", Query.Direction.DESCENDING );

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("Firesotre", "successs");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Firesotre", "successs22");
                                Map<String, Object> map = document.getData();
                                String uid = (String) map.get("uid");
                                String comId = document.getId();
                                String title = (String) map.get("title");
                                String body = (String) map.get("body");
                                String image = (String) map.get("image");
                                boolean heart = (Boolean) map.get("heart");
                                long heartNumber = (Long) map.get("heartNumber");
                                long commentNumber = (Long) map.get("commentNumber");

                                if(!image.equals("")){
                                    CommunityItemData data = new CommunityItemData(Code.ViewType.IMAGE, image);
                                    data.setTitle(title);
                                    data.setBody(body);
                                    data.setComId(comId);
                                    data.setHeartNumber(heartNumber);
                                    data.setCommentNumber(commentNumber);
                                    data.setImageUri(image);
                                    itemList.add(data);
                                }
                                else{
                                    CommunityItemData data = new CommunityItemData(Code.ViewType.TEXT, "");
                                    data.setTitle(title);
                                    data.setBody(body);
                                    data.setComId(comId);
                                    data.setHeartNumber(heartNumber);
                                    data.setCommentNumber(commentNumber);
                                    itemList.add(data);
                                }
                            }
                            adapter.notifyItemRangeInserted(0, itemList.size());

                            getLikeDataToFirebase();
                            Log.d("Firesotre", "successs");
                        } else {
                            Log.d("Firesotre", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    public void setTrueHeartOnFirebase(String comId, CommunityItemData com, int position){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        HashMap<String, Object> hashMap = new HashMap<>();
        String uid = userData.getUid();
        hashMap.put("uid", uid);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String time = sdf1.format(now);

        hashMap.put("timeStamp", time);
        hashMap.put("title", com.getTitle());
        hashMap.put("body", com.getBody());
        hashMap.put("image", com.getImageUri());
        hashMap.put("heart", com.isHeart());
        hashMap.put("heartNumber", com.getHeartNumber());
        hashMap.put("commentNumber", com.getCommentNumber());
        hashMap.put("comId", comId);


        db.collection("communityUser").document(uid)
                .collection("like").document(comId)
                .set(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "DocumentSnapshot successfully written!");
                        DocumentReference dr = db.collection("community").document(comId);
                        dr.update("heartNumber", com.getHeartNumber() + 1)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        long count = com.getHeartNumber() + 1;
                                        itemList.get(position).setHeartNumber(count);
                                        adapter.notifyItemChanged(position);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firestore", "Error writing document", e);

                    }
                });

    }
    public void setFalseHeartOnFirebase(String comId, CommunityItemData com, int position){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("communityUser").document(userData.getUid())
                .collection("like").document(comId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        DocumentReference dr = db.collection("community").document(comId);
                        dr.update("heartNumber", com.getHeartNumber() - 1)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        long count = com.getHeartNumber() - 1;
                                        itemList.get(position).setHeartNumber(count);
                                        adapter.notifyItemChanged(position);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void deleteCommunity(String comId, CommunityItemData data, int position){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        boolean isHeart = data.isHeart();

        db.collection("community").document(comId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        if(isHeart){
                            db.collection("communityUser").document(userData.getUid())
                                    .collection("like").document(comId)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            itemList.remove(position);
                                            adapter.notifyItemRemoved(position);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }
                        else{
                            itemList.remove(position);
                            adapter.notifyItemRemoved(position);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

}
