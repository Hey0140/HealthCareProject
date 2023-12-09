package com.example.myjavaapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

public class CommunityWriteActivity extends AppCompatActivity implements View.OnClickListener {
    private final static int REQUEST_IMAGE_CODE = 1020;
    private EditText title, body;
    private ImageView imageView;
    private Button save, photo;

    private UserMedia userData = new UserMedia();
    private CommunityMedia communityData = new CommunityMedia();
    private Uri imageUri;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_write_page);

        title = findViewById(R.id.communityWriteTitleText);
        body = findViewById(R.id.communityWriteBodyText);
        imageView = findViewById(R.id.communityWriteImageView);
        save = findViewById(R.id.communitySaveButton);
        photo = findViewById(R.id.communityPhotoButton);

        imageView.setOnClickListener(this);
        save.setOnClickListener(this);
        photo.setOnClickListener(this);

        imageView.setVisibility(View.GONE);

        Intent intent = getIntent();
        userData = (UserMedia) intent.getSerializableExtra("userData");
    }

    @Override
    public void onClick(View v) {
        if(v == imageView){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("확인창")
                    .setMessage("사진을 삭제하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            communityData.setImageUri("");
                            imageView.setVisibility(View.GONE);
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
        if(v == save){
            setCommunityData();
            onSetDataToFirebase(communityData);
        }
        if( v == photo){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, REQUEST_IMAGE_CODE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CODE) {
            if (resultCode == RESULT_OK) {
                imageUri = data.getData();
                try {

                    InputStream in = getContentResolver().openInputStream(data.getData());
                    image = BitmapFactory.decodeStream(in);
                    in.close();
                    imageView.setImageBitmap(image);
                    imageView.setVisibility(View.VISIBLE);

                    communityData.setImageUri(String.valueOf(imageUri));
                } catch (Exception e) {

                }
            }
        }
    }

    public void setCommunityData(){
        communityData.setUid(userData.getUid());
        communityData.setTitle(title.getText().toString());
        communityData.setBody(title.getText().toString());
        communityData.setCommentNumber(0);
        communityData.setHeartNumber(0);
    }

    private void onSetDataToFirebase(CommunityMedia com){
        HashMap<String, Object> hashMap = new HashMap<>();
        String uid = com.getUid();
        hashMap.put("uid", uid);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String time = sdf1.format(now);

        hashMap.put("timeStamp", time);
        hashMap.put("title", com.getTitle());
        hashMap.put("body", com.getBody());
        hashMap.put("image", com.getImageUri());
        hashMap.put("heart", false);
        hashMap.put("heartNumber", com.getHeartNumber());
        hashMap.put("commentNumber", com.getCommentNumber());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("community")
                .add(hashMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String comId = documentReference.getId();
                        communityData.setComid(comId);
                        Log.d("Firestore", documentReference.getId());

                        hashMap.put("comId", comId);

                        db.collection("communityUser").document(uid)
                                .collection("write").document(comId)
                                .set(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        CommunityFragment.isNew = true;
                                        if( !com.getImageUri().equals("")){
                                            Uri uri = Uri.parse(com.getImageUri());
                                            FirebaseStorage storage = FirebaseStorage.getInstance();
                                            String fileName = comId;
                                            StorageReference storageRef = storage.getReference().child("images/").child("community/").child(fileName);
                                            UploadTask uploadTask = storageRef.putFile(uri);

                                            Toast.makeText(CommunityWriteActivity.this, "업로드 중입니다. 잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
                                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    Log.i("Firebase Storage", "error");
                                                }
                                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    Log.i("Firebase Storage", "success");
                                                    finish();
                                                }
                                            });
                                        }
                                        else{
                                            finish();
                                        }

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
                        Log.w("Firestore", "Error adding document", e);
                    }
                });

    }
}