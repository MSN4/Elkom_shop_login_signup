package com.javaapp.loginsignupxlm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserLogin extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText loginEmail, loginPassword;
    private TextView forgotPasswordTxt, createAccountTxt, adminLoginTxt;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_login);

    loginEmail=findViewById(R.id.geditEmail);
    loginPassword=findViewById(R.id.geditPass);
    createAccountTxt=findViewById(R.id.createAcc_Txt);
    adminLoginTxt=findViewById(R.id.adminLogin_Txt);
    forgotPasswordTxt=findViewById(R.id.forgotPass_Txt);
    loginBtn=findViewById(R.id.login_Btn);

    //open the signup fragment
    createAccountTxt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(UserLogin.this,SignUp.class));
        }
    });
    //show forgot password fragment
    forgotPasswordTxt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(UserLogin.this,ForgotPassword.class));
        }
    });
    //show admin login fragment
    adminLoginTxt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(UserLogin.this, AdminLogin.class));
        }
    });

    //login Process
    auth=FirebaseAuth.getInstance();
   loginBtn.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           String email=loginEmail.getText().toString();
           String password=loginPassword.getText().toString();
           if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
               if(!password.isEmpty()){
                   auth.signInWithEmailAndPassword(email,password)
                           .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                               @Override
                               public void onSuccess(AuthResult authResult) {
                                   Toast.makeText(UserLogin.this,"Login Successful!",
                                           Toast.LENGTH_LONG).show();
                                   startActivity(new Intent(UserLogin.this,MainActivity.class));
                                   finish();
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Toast.makeText(UserLogin.this, "Login Failed!",
                                           Toast.LENGTH_LONG).show();
                               }
                           });
               }
               else
                   loginPassword.setError("Password cannot be empty!");
           }
           else if (email.isEmpty())
               loginEmail.setError("Email cannot be empty!");
           else
               loginEmail.setError("Please enter a valid email!");

       }
   });




    }
}