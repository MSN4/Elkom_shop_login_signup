package com.javaapp.loginsignupxlm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminLogin extends AppCompatActivity {

    private FirebaseAuth admin;
    private FirebaseFirestore fStore;
    private EditText adminEmail, adminPassword;

    private Button loginBtn;
    protected static Boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_login);

        adminEmail =findViewById(R.id.edit_adminEmail);
        adminPassword =findViewById(R.id.edit_adminPassword);
        loginBtn=findViewById(R.id.adminloginButton);

        admin =FirebaseAuth.getInstance();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= adminEmail.getText().toString();
                String password= adminPassword.getText().toString();
                //email is not empty and matches pattern
                if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    //password is not empty
                    if(!password.isEmpty()){
                        //if the user is admin

                            admin.signInWithEmailAndPassword(email, password)
                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            Toast.makeText(AdminLogin.this, "Login Successful! " + authResult.getUser().getUid(),
                                                    Toast.LENGTH_LONG).show();
                                            checkUserAccessLevel(authResult.getUser().getUid());
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AdminLogin.this, "Login Failed!",
                                                    Toast.LENGTH_LONG).show();
                                        }

                                    });
                        }
                    else
                        adminPassword.setError("Password cannot be empty!");
                }
                else if (email.isEmpty())
                    adminEmail.setError("Email cannot be empty!");
                else
                    adminEmail.setError("Please enter a valid email!");

            }
        });


    }

    protected void checkUserAccessLevel(String uid) {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("AdminLogin", "onSuccess: "+ documentSnapshot.getData());
                if(documentSnapshot.getString("isAdmin")!=null){
                   startActivity(new Intent(AdminLogin.this, AdminHome.class));
                    isAdmin=true;
                   finish();

                }
                else {
                    Toast.makeText(AdminLogin.this, "Not an Admin!", Toast.LENGTH_LONG).show();
                    isAdmin=false;
                }
            }
        });




    }

}