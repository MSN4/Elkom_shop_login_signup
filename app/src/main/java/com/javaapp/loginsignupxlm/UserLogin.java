package com.javaapp.loginsignupxlm;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

    //open the signup HomePage
    createAccountTxt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(UserLogin.this,SignUp.class));
        }
    });
    //show forgot password HomePage
    forgotPasswordTxt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(UserLogin.this,ForgotPassword.class));
        }
    });
    //show admin login HomePage
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


                                   startActivity(new Intent(UserLogin.this,HomePage.class));
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

   //forgot password
        forgotPasswordTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(UserLogin.this);
                View forgotDialog =getLayoutInflater().inflate(R.layout.dialog_forgot,null);
                EditText emailBox=forgotDialog.findViewById(R.id.emailBox);

                builder.setView(forgotDialog);
                AlertDialog dialog= builder.create();


                forgotDialog.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userEmail = emailBox.getText().toString();
                        if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(UserLogin.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(UserLogin.this, "Check your email", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(UserLogin.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                forgotDialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();

            }
        });

    }

    //check if user is already logged in, if logged in open home page
    //if not, then open login
    //this onStart method checks if the user is already logged in whether it is an admin or
    // a typical user , and it opens the suitable homepage based on the role level

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference userRef = db.collection("users").document(userId);
                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Boolean isAdmin = document.getBoolean("isAdmin");
                                if (isAdmin != null && isAdmin) {
                                    Toast.makeText(UserLogin.this, "Admin is already Logged In!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(UserLogin.this, AdminHome.class));
                                } else {
                                    Toast.makeText(UserLogin.this, "Already Logged In!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(UserLogin.this, HomePage.class));
                                }
                            } else {
                                Toast.makeText(UserLogin.this, "User document does not exist!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(UserLogin.this, "Failed to fetch user data!", Toast.LENGTH_SHORT).show();
                        }
                        finish(); // close login activity
                    }
                });
            }
        }
    }



}