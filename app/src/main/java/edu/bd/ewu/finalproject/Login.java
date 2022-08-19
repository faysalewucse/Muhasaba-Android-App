package edu.bd.ewu.finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText LogIn_email, LogIn_pass;
    private Button login;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        TextView register = findViewById(R.id.login_signUp);
        TextView forgot_pass = findViewById(R.id.login_pass_forget);
        login = findViewById(R.id.logInBtn);
        LogIn_email = findViewById(R.id.login_email);
        LogIn_pass = findViewById(R.id.login_pass);
        progressBar = findViewById(R.id.LogIn_progressBar);
        progressBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(v -> userLogIn());
        register.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignUp.class);
            startActivity(intent);
            finish();
        });

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = new EditText(v.getContext());
                AlertDialog.Builder resetpassword = new AlertDialog.Builder(v.getContext());
                resetpassword.setTitle("Reset Password?");
                resetpassword.setMessage("Enter Your Email to Received Reset Link.");
                resetpassword.setIcon(R.drawable.key_icon);
                resetpassword.setView(email);
                resetpassword.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = email.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "Reset Link Sent to Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Error Occurred! Reset Link is Not Sent. Try Again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                resetpassword.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                resetpassword.create().show();
            }
        });
    }

    private void userLogIn() {
        String email = LogIn_email.getText().toString().trim();
        String pass = LogIn_pass.getText().toString().trim();

        if (email.isEmpty()) {
            LogIn_email.setError("ইমেইল বক্সটি খালি রাখা যাবেনা");
            LogIn_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            LogIn_email.setError("একটি সঠিক ইমেইল প্রদান করুন");
            LogIn_email.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            LogIn_pass.setError("পাসওয়ার্ড বক্স টি খালি রাখা যাবেন");
            LogIn_pass.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "সফলভাবে লগইন হয়েছে", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, "ইমেইল বা পাসওয়ার্ড ভুল হয়েছে", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }
}