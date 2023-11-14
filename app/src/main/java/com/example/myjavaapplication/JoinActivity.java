package com.example.myjavaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

public class JoinActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth auth;
    EditText userName;
    EditText phoneNumber;
    EditText emailId;
    EditText password;
    EditText passwordCheck;
    Button joinButton;
    ToggleButton toggleButton;

    String name;
    String number;
    String email;
    String pw;
    String pwCheck;
    boolean professional;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_page);

        userName = findViewById(R.id.userName);
        phoneNumber = findViewById(R.id.userPhoneNumber);
        emailId = findViewById(R.id.userEmail);
        password = findViewById(R.id.userPassword);
        passwordCheck = findViewById(R.id.userPwCheck);
        joinButton = findViewById(R.id.joinButton);
        toggleButton = findViewById(R.id.proMemToggle);


        joinButton.setOnClickListener(this);
        toggleButton.setOnClickListener(this);

        toggleButton.setChecked(false);
        professional = false;

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if(v == joinButton){
            name = userName.getText().toString();
            number = phoneNumber.getText().toString();
            email = emailId.getText().toString();
            pw = password.getText().toString();
            pwCheck = passwordCheck.getText().toString();
            if(name.equals("")){
                Log.i("join", "username");
                Toast.makeText(this, "성함을 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else if (number.equals("")) {
                Toast.makeText(this, "전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else if (email.equals("")) {
                Toast.makeText(this, "이메일  입력해주세요.", Toast.LENGTH_SHORT).show();
            } else if (pw.equals("")) {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else if (pw.length() < 6) {
                Toast.makeText(this, "비밀번호는 6자 이상이여야 합니다.", Toast.LENGTH_SHORT).show();
            } else if (pwCheck.equals("")) {
                Toast.makeText(this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
            else{
                if(pw.equals(pwCheck)){
                    auth.createUserWithEmailAndPassword(email, pw)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        signIn(email, pw);
                                    }
                                    else{
                                        Toast.makeText(JoinActivity.this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    finish();
                }
                else{
                    Toast.makeText(this, "비밀번호가 다릅니다. 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if(v== toggleButton){
            if(professional == false){
                professional = true;
                toggleButton.setChecked(true);
            }
            else{
                professional = false;
                toggleButton.setChecked(false);
            }
        }
    }
    public void signIn(String em, String pa){
        auth.signInWithEmailAndPassword(em, pa)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> resultTask) {
                        if(resultTask.isSuccessful()){
                            Log.d("click", "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> sendtask) {
                                    if(sendtask.isSuccessful()){
                                        Log.w("click", "signInWithEmail:success");
                                        Toast.makeText(JoinActivity.this, "Authentication succeed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            Log.w("click", "signInWithEmail:failure", resultTask.getException());
                            Toast.makeText(JoinActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}