package com.javaapp.loginsignupxlm;

import android.net.Uri;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUp extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText usernameEditText,
            emailEditText,
            phoneEditText,
            passwordEditText,
            confirmPassword;

    ImageView imageView;
    FloatingActionButton button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth=FirebaseAuth.getInstance();
        usernameEditText = findViewById(R.id.username_EditText);
        emailEditText = findViewById(R.id.edit_TextEmail);
        phoneEditText = findViewById(R.id.edit_TextPhone);
        passwordEditText = findViewById(R.id.edit_TextPassword);
        confirmPassword = findViewById(R.id.ConfirmPassword_editText);
        imageView = findViewById(R.id.pfp_imageView);
        button = findViewById(R.id.floatingAction_btn);
        Button registerationButton = findViewById(R.id.createAccount_btn);

        registerationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkCredentials();

                if (usernameEditText.getError() == null &&
                        emailEditText.getError() == null &&
                        phoneEditText.getError() == null &&
                        passwordEditText.getError() == null &&
                        confirmPassword.getError() == null) {
                    String userName = usernameEditText.getText().toString().trim();
                    String email= emailEditText.getText().toString().trim();
                    String password= passwordEditText.getText().toString().trim();
                    String phoneNumber= phoneEditText.getText().toString().trim();
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUp.this, "SignUp Successful!",
                                        Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SignUp.this,
                                        MainActivity.class));
                            }
                            else Toast.makeText(SignUp.this, "Signup Failed!"+
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    //startActivity(new Intent(MainActivity.this, ));
                }
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(SignUp.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode , int resultCode , @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode , data);
        Uri uri = data .getData();
        imageView.setImageURI(uri);
    }
    private void checkCredentials() {
        String userName = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String passwordS = passwordEditText.getText().toString();
        String confirmPasswordS = confirmPassword.getText().toString();

        String emailPattern = "^[a-zA-Z0-9_]+@gmail\\.com$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        String validPrefix1 = "079";
        String validPrefix2 = "078";
        String validPrefix3 = "077";

        clearErrors(); // Clear any existing errors

        if (userName.isEmpty() || userName.length() < 5) {
            showError(usernameEditText, "Your Username is not valid!");
        }

        if (email.isEmpty() || !matcher.matches()) {
            showError(emailEditText, "Email is not valid!");
        }

        if (phone.length() != 10 || (!phone.startsWith(validPrefix1) && !phone.startsWith(validPrefix2) && !phone.startsWith(validPrefix3))) {
            showError(phoneEditText, "Phone number must be 10 numbers and start with a valid prefix!");
        }

        if (passwordS.isEmpty() || passwordS.length() < 7) {
            showError(passwordEditText, "Password must be more than 7 characters");
        }

        if (confirmPasswordS.isEmpty() || !confirmPasswordS.contentEquals(passwordS)) {
            showError(confirmPassword, "Password does not match!");
        }
    }

    private void clearErrors() {
        usernameEditText.setError(null);
        emailEditText.setError(null);
        phoneEditText.setError(null);
        passwordEditText.setError(null);
        confirmPassword.setError(null);
    }

    private void showError(EditText input, String errorMessage) {
        input.setError(errorMessage);
        input.requestFocus();
    }
}