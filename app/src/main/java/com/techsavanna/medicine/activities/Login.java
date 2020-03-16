package com.techsavanna.medicine.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.techsavanna.medicine.R;

import java.util.Objects;

public class Login extends AppCompatActivity {
    private EditText LoginEmail, LoginPassword;
    private  String upemail, uppassword;
    Button logbtn;
    TextView tv_register;
    FirebaseAuth mAuth;
    ProgressBar progressbarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        LoginEmail = findViewById(R.id.LoginName);
        LoginPassword = findViewById(R.id.LoginPassword);
        progressbarLogin = findViewById(R.id.progressbarLogin);

        mAuth = FirebaseAuth.getInstance();


        tv_register = findViewById(R.id.RegisterLogin);
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        logbtn = findViewById(R.id.LoginButton);
        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

                LoginPassword.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });
    }

    public void login(){
        initialize();
        if(!validate()) {

        }
        else {
            onLoginSuccess();
        }
    }

    public void onLoginSuccess(){
        progressbarLogin.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(upemail, uppassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressbarLogin.setVisibility(View.GONE);
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                }
                else {
                    progressbarLogin.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    public boolean validate(){
        boolean valid = true;
        if(upemail.isEmpty()||!android.util.Patterns.EMAIL_ADDRESS.matcher(upemail).matches()){
            LoginEmail.setError("Please Enter Email Address");
            valid = false;
        }
        if(uppassword.isEmpty()){
            LoginPassword.setError("Please Enter Password");
            valid = false;
        }
        return valid;
    }
    public void initialize(){
        upemail = LoginEmail.getText().toString().trim();
        uppassword = LoginPassword.getText().toString().trim();
    }
}


