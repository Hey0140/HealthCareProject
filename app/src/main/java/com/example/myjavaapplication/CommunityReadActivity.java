package com.example.myjavaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommunityReadActivity extends AppCompatActivity implements View.OnClickListener {
    TextView title, body;
    EditText commentbody;
    Button commentButton;
    ImageView image;
    View heartButton;
    boolean isHeartCommunity = false;
    CommentItemAdapter adapter;
    ArrayList<CommentItemData> itemList = new ArrayList<>();
    ArrayList<CommentMedia> commentDataList = new ArrayList<>();
    private CommunityMedia communityData = new CommunityMedia();
    private UserMedia userData = new UserMedia();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_read_page);

        title = findViewById(R.id.communityShowTitle);
        body = findViewById(R.id.communityShowBodyText);
        image = findViewById(R.id.communityShowImageView);
        commentbody = findViewById(R.id.communityCommentAddText);
        commentButton = findViewById(R.id.communityCommentAddButton);
        heartButton = findViewById(R.id.communityShowLoveButton);

        commentButton.setOnClickListener(this);
        heartButton.setOnClickListener(this);

        Intent intent = getIntent();
        communityData = (CommunityMedia) intent.getSerializableExtra("communityData");
        userData = (UserMedia) intent.getSerializableExtra("userData");

        image.setVisibility(View.GONE);

        title.setText(communityData.getTitle());
        body.setText(communityData.getBody());

        if(communityData.isHeart()){
            isHeartCommunity = true;
            heartButton.setBackgroundResource(R.drawable.filled_favorite_icon);
        } else{
            isHeartCommunity = false;
            heartButton.setBackgroundResource(R.drawable.not_fill_favorite_icon);
        }

        if(!communityData.getImageUri().equals("")){
            getImage(communityData);
        }else{
            image.setVisibility(View.GONE);
        }

        itemList.clear();
        commentDataList.clear();

        RecyclerView recyclerView = findViewById(R.id.communityCommentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(CommunityReadActivity.this, LinearLayoutManager.VERTICAL, false));
        adapter = new CommentItemAdapter(itemList, this);
        recyclerView.setAdapter(adapter);

        getFirebaseComment();
    }

    @Override
    public void onClick(View v) {
        if(v == commentButton){
            String bodyTitle = commentbody.getText().toString();
            commentbody.setText("");
            CommentItemData data = new CommentItemData();
            data.setName(userData.getName());
            data.setBody(bodyTitle);
            data.setComId(communityData.getComid());
            data.setUserId(userData.getUid());

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            String timeStamp = format.format(now);

            data.setTimeStamp(timeStamp);

            CommentMedia comment = new CommentMedia();
            comment.setUserId(userData.getUid());
            comment.setComId(communityData.getComid());
            comment.setName(userData.getName());
            comment.setBody(bodyTitle);
            comment.setTimeStamp(timeStamp);
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeStamp2 = format2.format(now);

            comment.setTimeStampCheck(timeStamp2);
            onSetCommentToFirebase(comment, data);
        }
        if(v == heartButton){
            if(isHeartCommunity){
                isHeartCommunity = false;
                communityData.setHeart(false);
                heartButton.setBackgroundResource(R.drawable.not_fill_favorite_icon);
            }
            else{
                isHeartCommunity = true;
                communityData.setHeart(true);
                heartButton.setBackgroundResource(R.drawable.filled_favorite_icon);
            }
        }
    }

    public void getImage(CommunityMedia com){

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://pet-healthcare-45a41.appspot.com");
        StorageReference storageReference = storage.getReference().child("images/").child("community/").child(com.getComid());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //이미지 로드 성공시

                Glide.with(CommunityReadActivity.this)
                        .load(uri)
                        .into(image);
                image.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //이미지 로드 실패시
            }
        });
    }

    public void getItemList() {
        for(int i = 0; i < commentDataList.size(); i++){
            CommentItemData data = new CommentItemData();
            CommentMedia com = commentDataList.get(i);

            data.setName(com.getName());
            data.setBody(com.getBody());
            data.setTimeStamp(com.getTimeStamp());
            itemList.add(data);
        }
        adapter.notifyItemRangeInserted(0, itemList.size());
    }

    public void getFirebaseComment(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("community").document(communityData.getComid()).collection("comment")
                .orderBy("timeStampCheck", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CommentMedia com = new CommentMedia();

                                Map<String, Object> map = document.getData();

                                com.setComId( (String) map.get("comId"));
                                com.setUserId((String) map.get("uid"));
                                com.setCommentId(document.getId());
                                com.setName((String) map.get("name"));
                                com.setBody((String) map.get("body"));
                                com.setTimeStamp((String) map.get("timeStamp"));
                                com.setTimeStampCheck((String) map.get("timeStampCheck"));

                                commentDataList.add(com);
                            }
                            Log.d("Firesotre", "successs");
                            getItemList();
                        } else {
                            Log.d("Firesotre", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void onSetCommentToFirebase(CommentMedia com, CommentItemData data) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comId", com.getComId());
        hashMap.put("uid", com.getUserId());
        hashMap.put("name", com.getName());
        hashMap.put("body", com.getBody());
        hashMap.put("timeStamp", com.getTimeStamp());
        hashMap.put("timeStampCheck", com.getTimeStampCheck());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("community").document(com.getComId())
                .collection("comment")
                .add(hashMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        itemList.add(data);
                        adapter.notifyItemInserted(itemList.size()-1);
                        commentDataList.add(com);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

}