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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.techsavanna.medicine.R;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private EditText et_email, et_password, et_cpassword, et_phone;
    private String email, password, cpassword, phone;
    Button regbtn;
    TextView txlog;
    ProgressBar progressbarRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        et_email = findViewById(R.id.RegisterEmail);
        et_password = findViewById(R.id.RegisterPassword);
        et_phone = findViewById(R.id.RegisterPhone);
        et_cpassword = findViewById(R.id.RegisterConfirm);
        txlog = findViewById(R.id.LoginHere);
        progressbarRegister = findViewById(R.id.progressbarRegister);
        mAuth = FirebaseAuth.getInstance();


        txlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
        regbtn = findViewById(R.id.RegisterButton);
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();

                et_cpassword.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });

    }

    public void register(){
        initialize();
        if(!validate()) {

        }
        else {
            onRegistrationSuccess();
        }
    }
    public void onRegistrationSuccess(){
        progressbarRegister.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser MfirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                            if (MfirebaseUser != null){
                                String userid = MfirebaseUser.getUid();
                                String email = MfirebaseUser.getEmail();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(MfirebaseUser.getUid());
                                HashMap map = new HashMap<>();

                                map.put("upphone", phone);
                                map.put("upuserid", userid);
                                map.put("upemail", email);

                                databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            progressbarRegister.setVisibility(View.GONE);

                                            Intent intent = new Intent(Register.this, Login.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();

                                        }

                                    }
                                });



                            }



                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getApplicationContext(), "User already registered", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }
    public boolean validate(){
        boolean valid = true;
        if(email.isEmpty()||!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError("Please Enter valid Email Address");
            valid = false;
        }
        if(phone.isEmpty()){
            et_phone.setError("Please Enter Username");
            valid = false;
        }
        if(password.isEmpty()){
            et_password.setError("Please Enter Password");
            valid = false;
        }
        if(cpassword.isEmpty()){
            et_cpassword.setError("Please confirm password");
            valid = false;
        }
        if(password.length()<6){
            et_password.setError("The password is too short");
            valid = false;
        }
        if(!cpassword.equals(password)){
            et_cpassword.setError("The password do not match");
            valid = false;
        }

        return valid;
    }



    public void initialize(){
        email = et_email.getText().toString().trim();
        password = et_password.getText().toString().trim();
        cpassword = et_cpassword.getText().toString().trim();
        phone = et_phone.getText().toString().trim();

    }
}
