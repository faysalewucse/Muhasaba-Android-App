package edu.bd.ewu.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
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
import com.google.firebase.database.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SignUp extends AppCompatActivity {

    private EditText register_name,register_email,register_pass,register_confirm_pass;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    private DatabaseReference userDataRef, leaderboardDataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        TextView signin = findViewById(R.id.register_login);
        Button register = findViewById(R.id.register_registerBtn);
        register_name=findViewById(R.id.register_username);
        register_email=findViewById(R.id.register_email);
        register_pass=findViewById(R.id.register_pass);
        register_confirm_pass=findViewById(R.id.register_confirm_pass);
        progressBar = findViewById(R.id.register_progressBar);

        userDataRef = FirebaseDatabase.getInstance().getReference("USERS");
        leaderboardDataRef = FirebaseDatabase.getInstance().getReference("LEADERBOARD");
        mAuth =FirebaseAuth.getInstance();

        progressBar.setVisibility(View.GONE);
        signin.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Login.class)));
        register.setOnClickListener(v -> userRegister());
    }
    private void userRegister() {

        String name=register_name.getText().toString().trim();
        String email=register_email.getText().toString().trim();
        String pass=register_pass.getText().toString().trim();
        String confirm_pass=register_confirm_pass.getText().toString().trim();

        if(name.isEmpty())
        {
            register_name.setError("নামের বক্স টি খালি রাখা যাবে না");
            register_name.requestFocus();
            return;
        }
        if(email.isEmpty())
        {
            register_email.setError("ইমেইল বক্স টি খালি রাখা যাবেনা");
            register_email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            register_email.setError("একটি সঠিক ইমেইল দিন");
            register_email.requestFocus();
            return;
        }
        if(pass.isEmpty())
        {
            register_pass.setError("পাসওয়ার্ড বক্স টি খালি রাখা যাবে না");
            register_pass.requestFocus();
            return;
        }
        if(pass.length()<8)
        {
            register_pass.setError("সর্বনিম্ন ৮ টি সংখ্যা বা লেটার থাকতে হবে");
            register_pass.requestFocus();
            return;
        }
        if(confirm_pass.isEmpty())
        {
            register_confirm_pass.setError("কনফার্ম পাসওয়ার্ড বক্স টি খালি রাখা যাবে না");
            register_confirm_pass.requestFocus();
            return;
        }
        if(pass.equals(confirm_pass))
        {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {


                        progressBar.setVisibility(View.GONE);
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = user.getUid();
                        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                        UserInfo registerData = new UserInfo(name, email, pass, "gs://hanafifiqh-12bee.appspot.com/user_profile.png");
                        userDataRef.child(uid).child("User Information").setValue(registerData);
                        userDataRef.child(uid).child("Settings").child("notify").setValue("true");
                        userDataRef.child(uid).child("Settings").child("notify_approval").setValue("true");
                        leaderboardDataRef.child(uid).child("points").setValue("0");

                        Toast.makeText(getApplicationContext(), "Signed Up Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();

                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "User is Already Registered", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        }
        else
        {
            register_confirm_pass.setError("পাসওয়ার্ড মিলে নি। আবার চেক করুন");
            register_confirm_pass.requestFocus();
            return;
        }
    }
}