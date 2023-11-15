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
    private FirebaseAuth auth;
    EditText userName;
    EditText phoneNumber;
    EditText emailId;
    EditText password;
    EditText passwordCheck;
    Button joinButton;
    Button verifyButton;
    ToggleButton toggleButton;
    boolean isEmailVerified;
    boolean isSetEmail;

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
        verifyButton = findViewById(R.id.verifyButton);


        joinButton.setOnClickListener(this);
        toggleButton.setOnClickListener(this);
        verifyButton.setOnClickListener(this);

        verifyButton.setVisibility(View.GONE);
        toggleButton.setChecked(false);
        joinButton.setText("기입 완료");
        professional = false;
        isEmailVerified = false;
        isSetEmail = false;

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if(v == verifyButton && isSetEmail == true){
            verifyEmail();
        }
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
            else if (!pw.equals(pwCheck)){
                Toast.makeText(this, "비밀번호가 다릅니다. 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
            else{
                if(isEmailVerified == false && isSetEmail == false){
                    createUser(email, pw);
                }
                else if (isEmailVerified == true && isSetEmail == true){
                    finish();
                }
                else{
                    Toast.makeText(this, "이메일 인증을 성공한 뒤 시도해주세요.", Toast.LENGTH_SHORT).show();
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

    private void verifyEmail() {
        final FirebaseUser user = auth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this , new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(JoinActivity.this, "이메일 인증 메일이 보내졌습니다. 완료 버튼을 클릭해주세요.",
                                    Toast.LENGTH_SHORT).show();
                            verifyButton.setVisibility(View.GONE);
                            isEmailVerified = true;
                        } else {
                            Log.e("click", "sendEmailVerification", task.getException());
                            Toast.makeText(JoinActivity.this,
                                    "이메일 인증 메일이 전송되지 못했습니다. 다시 시도해주세요.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void createUser(String pa_email, String pa_password){
        auth.createUserWithEmailAndPassword(pa_email, pa_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(JoinActivity.this, "계정이 등록되었습니다. 이메일 인증을 추가로 완료해주세요.", Toast.LENGTH_SHORT).show();
                            verifyButton.setVisibility(View.VISIBLE);
                            joinButton.setText("완료");
                            isSetEmail = true;
                        }
                        else{
                            Toast.makeText(JoinActivity.this, "이미 존재하는 계정입니다. 이메일 인증을 진행하지 않았다면 버튼을 눌러 진행해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        userName.setText("");
        phoneNumber.setText("");
        emailId.setText("");
        password.setText("");
        passwordCheck.setText("");

        verifyButton.setVisibility(View.GONE);
        toggleButton.setChecked(false);
        professional = false;
        isEmailVerified = false;
        isSetEmail = false;

        joinButton.setText("기입 완료");
    }
}