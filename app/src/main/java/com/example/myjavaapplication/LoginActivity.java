package com.example.myjavaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private GoogleSignInOptions gso;
    private GoogleSignInAccount gsa;
    private GoogleSignInClient gsc;
    private boolean professional;
    private boolean GOOGLE;
    private final int RC_SIGN_IN = 9001;
    private final boolean BASIC_MEMBER = false;
    private final boolean PROFESSIONAL = true;


    private CheckBox proCheckBox, memCheckBox;
    private EditText idText, pwText;
    private Button loginButton, googleButton;
    private TextView loginJoin, loginFindOrPw;

    private String email = "", password = "";
    private UserMedia userData;
    private ArrayList<PetMedia> petDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        email = "";
        password = "";
        professional = BASIC_MEMBER;
        GOOGLE = false;
        userData = new UserMedia();
        petDataList = new ArrayList<>();

        idText = findViewById(R.id.loginId);
        pwText = findViewById(R.id.loginPassword);
        googleButton = findViewById(R.id.loginSnsButton);
        loginButton = findViewById(R.id.loginButton);
        proCheckBox = findViewById(R.id.professCheckBox);
        memCheckBox = findViewById(R.id.basicCheckBox);
        loginJoin = findViewById(R.id.loginJoin);
        loginFindOrPw = findViewById(R.id.loginFindIdOrPw);

        auth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        loginButton.setOnClickListener(this);
        googleButton.setOnClickListener(this);
        proCheckBox.setOnClickListener(this);
        memCheckBox.setOnClickListener(this);
        loginJoin.setOnClickListener(this);
        loginFindOrPw.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v == proCheckBox){
            professional = PROFESSIONAL;
            onCheckBox(professional);
        }
        if(v == memCheckBox){
            professional = BASIC_MEMBER;
            onCheckBox(professional);
        }
        if (v == loginButton) {
            email = idText.getText().toString();
            password = pwText.getText().toString();
            if(email.length() > 0 && password.length() >= 6){
                auth.signInWithEmailAndPassword(idText.getText().toString(), pwText.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user = auth.getCurrentUser();
                                    if(user.isEmailVerified()){
                                        getDataToFirestore(user.getUid());
                                    }
                                    else{
                                        Toast.makeText(LoginActivity.this, "이메일 인증 중입니다. 이메일을 인증을 진행해주세요.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "아이디 또는 패스워드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else{
                Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }

        }
        if(v == googleButton) {
            gsa = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
            if(gsa != null){
                Toast.makeText(this, "구글 로그인은 일반 회원으로만 로그인할 수 있습니다.", Toast.LENGTH_SHORT).show();
                GOOGLE = true;
                memCheckBox.setChecked(true);
                proCheckBox.setChecked(false);
                professional = BASIC_MEMBER;
                getDataToFirestore(gsa.getId());
            }
            else{
                memCheckBox.setChecked(true);
                proCheckBox.setChecked(false);
                professional = BASIC_MEMBER;
                Toast.makeText(this, "구글 로그인은 일반 회원으로만 로그인할 수 있습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = gsc.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        }
        if(v == loginJoin){
            Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
            startActivity(intent);
        }
        if(v == loginFindOrPw){

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount gsia = completedTask.getResult(ApiException.class);

            if (gsia != null) {
                firebaseAuthWithGoogle(gsia.getIdToken());
                String id = gsia.getId();
                String personName = gsia.getDisplayName();
                String email = gsia.getEmail();
                Uri personPhoto = gsia.getPhotoUrl();

                setDataToFirestore(id, email, personName, "", BASIC_MEMBER, personPhoto.toString());
            }
        } catch (ApiException e) {
            Log.e("google", "signInResult:failed code=" + e.getStatusCode());
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("google", "signInWithCredential:success");
                        Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = auth.getCurrentUser();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("google", "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getDataToFirestore(String id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userData = documentSnapshot.toObject(UserMedia.class);
            }
        });

        db.collection("users")
                .document(id)
                .collection("pet")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PetMedia petMedia = new PetMedia();
                                Map<String, Object> map = new HashMap<>();

                                map = document.getData();
                                petMedia = setPetMeida(map, i);

                                petDataList.add(petMedia);
                                Log.i("check", petMedia.getPetId()+": "+i+", "+petMedia.getPetName()+", "+petMedia.getPetKind());
                                i++;
                            }
                            Log.i("check", String.valueOf(petDataList.size()));
                            if(userData.isPro() == professional){
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("userData", userData);
                                intent.putExtra("petDataList", petDataList);
                                startActivity(intent);
                            }
                            else{
                                if(professional == BASIC_MEMBER && userData.isPro() == PROFESSIONAL){
                                    Toast.makeText(LoginActivity.this, "의사 계정 "+userData.getName()+"님 일반 회원 서비스로 접속합니다.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("userData", userData);
                                    intent.putExtra("petDataList", petDataList);
                                    startActivity(intent);
                                }
                                if(professional == PROFESSIONAL && userData.isPro() == BASIC_MEMBER){
                                    Toast.makeText(LoginActivity.this, "의사 계정이 아닙니다. 일반 회원으로 접속해주세요.", Toast.LENGTH_LONG).show();
                                    auth.signOut();
                                }
                            }
                        } else {
                            Log.d("Firebase", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public PetMedia setPetMeida(Map<String, Object> map, int position){
        PetMedia petMedia = new PetMedia();
        petMedia.setuId(map.get("uid").toString());
        petMedia.setPetId(position);
        petMedia.setPetBirth(map.get("birth").toString());
        petMedia.setPetFeat(map.get("feat").toString());
        petMedia.setPetFeed(map.get("feed").toString());
        petMedia.setPetFeedCalorie((Long) map.get("feedcal"));
        petMedia.setImage(map.get("image").toString());
        petMedia.setPetKind((Long) map.get("kind"));
        petMedia.setPetName(map.get("name").toString());
        petMedia.setPetSex((Long) map.get("sex"));
        petMedia.setPetWeight((Long) map.get("weight"));
        petMedia.setPetLike((HashMap<String, Boolean>) map.get("petLike"));
        petMedia.setPetVaccine((HashMap<String, Boolean>) map.get("petVaccine"));
        return petMedia;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        petDataList.clear();
        if(GOOGLE == true){
            onCheckBox(professional);
            GOOGLE = false;
        }
        else{
            userData = new UserMedia();
            idText.setText(email);
            pwText.setText(password);
            onCheckBox(professional);
            GOOGLE = false;
        }
    }

    void onCheckBox(boolean mem){
        if(mem == PROFESSIONAL){
            memCheckBox.setChecked(false);
            proCheckBox.setChecked(true);
        }
        else if(mem == BASIC_MEMBER){
            memCheckBox.setChecked(true);
            proCheckBox.setChecked(false);
        }
    }


    public void setDataToFirestore(String id, String email, String name, String number, Boolean professional, String image){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", id);
        hashMap.put("email", email);
        hashMap.put("name", name);
        hashMap.put("number", number);
        hashMap.put("pro", professional);
        hashMap.put("image", image);
        hashMap.put("count", 0);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(id)
                .set(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firebase", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firebase", "Error writing document", e);
                    }
                });
    }

}

