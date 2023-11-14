package com.example.myjavaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private GoogleSignInOptions gso;
    private GoogleSignInAccount gsa;
    private GoogleSignInClient gsc;
    private final int RC_SIGN_IN = 9001;

    EditText idText, pwText;
    Button loginButton, googleButton;

    String email = "";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        email = "";
        password = "";

        idText = findViewById(R.id.loginId);
        pwText = findViewById(R.id.loginPassword);
        googleButton = findViewById(R.id.loginSnsButton);
        loginButton = findViewById(R.id.loginButton);

        auth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        loginButton.setOnClickListener(this);
        googleButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == loginButton) {
            email = idText.getText().toString();
            password = pwText.getText().toString();
            if(email.length() > 0 && password.length() >= 6){
                auth.signInWithEmailAndPassword(idText.getText().toString(), pwText.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "아이디 또는 패스워드가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
            else{
                Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();
            }

        }
        if(v == googleButton) {
            gsa = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
            if(gsa != null){
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
            else{
                Intent intent = gsc.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
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

                String personName = gsia.getDisplayName();
                String personGivenName = gsia.getGivenName();
                String personFamilyName = gsia.getFamilyName();
                String personEmail = gsia.getEmail();
                String personId = gsia.getId();
                Uri personPhoto = gsia.getPhotoUrl();

                Log.d("google", "handleSignInResult:personName "+personName);
                Log.d("google", "handleSignInResult:personGivenName "+personGivenName);
                Log.d("google", "handleSignInResult:personEmail "+personEmail);
                Log.d("google", "handleSignInResult:personId "+personId);
                Log.d("google", "handleSignInResult:personFamilyName "+personFamilyName);
                Log.d("google", "handleSignInResult:personPhoto "+personPhoto);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        idText.setText(email);
        pwText.setText(password);
    }
}