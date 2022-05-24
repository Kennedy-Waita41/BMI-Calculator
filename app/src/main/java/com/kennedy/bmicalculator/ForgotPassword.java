package com.kennedy.bmicalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText emailEditText;
    private Button resetPasswordButton;
    private ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().hide();
        emailEditText =findViewById(R.id.email);
        resetPasswordButton=findViewById(R.id.resetPassword);
        progressBar=findViewById(R.id.progressBar);

        auth= FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }
    private void resetPassword(){
        String email = emailEditText.getText().toString().trim();

        //Validate user Email input to make sure fields are not empty or take in appropriate data
        if (email.isEmpty()){
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Please provide valid email");
            emailEditText.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Check your email and reset your password!",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    //Redirect to login
                    startActivity(new Intent(ForgotPassword.this,Login.class));

                }else{
                    Toast.makeText(ForgotPassword.this,"Try again!, something wrong happened",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    //Redirect to login
                    startActivity(new Intent(ForgotPassword.this,Login.class));
                }
            }
        });

    }
}