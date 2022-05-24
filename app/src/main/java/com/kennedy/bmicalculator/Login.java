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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private TextView register;
    private TextView forgotPassword;
    private EditText editTextEmail, editTextPassword;
    private Button logIn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        register=findViewById(R.id.register);
        register.setOnClickListener(this);

        forgotPassword=findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);

        logIn =findViewById(R.id.signIn);
        logIn.setOnClickListener(this);

        editTextEmail=findViewById(R.id.email);
        editTextPassword=findViewById(R.id.password);
        progressBar=findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();
    }
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.register:
                startActivity(new Intent(this,RegisterUser.class));
                break;
            case R.id.signIn:
                userLogin();
                break;
            case R.id.forgotPassword:
                startActivity(new Intent(this,ForgotPassword.class));

        }
    }
    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //Validating email field not tp accept an empty string
        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        //Make sure an email address has the right identifier is @symbol

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter a valid email!");
            editTextEmail.requestFocus();
            return;
        }
        //Check that password filed is not empty
        if (password.isEmpty()){
            editTextPassword.setError(("Password is required"));
            editTextPassword.requestFocus();
            return;
        }
        //Check password length to be 6 or more characters
        if (password.length()<6){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        //Access user details from Firebase to authenticate login process
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //Create an object FirebaseUser class to get data on user trying to Login
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()){
                        //redirect to user profile
                        startActivity(new Intent(Login.this,Splash.class));
                    }else {
                        user.sendEmailVerification();
                        Toast.makeText(Login.this,"Check your email to verify your account",Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }else{
                    Toast.makeText(Login.this,"Failed to login! Please check your credentials",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}