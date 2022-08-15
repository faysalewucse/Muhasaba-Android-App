package edu.bd.ewu.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SignUp extends AppCompatActivity {

    private EditText register_name,register_email,register_pass,register_confirm_pass;
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
        ProgressBar progressBar = findViewById(R.id.register_progressBar);

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
        if(checkDuplicateEmail(email)){
            register_email.setError("এই মেইল দিয়ে পুর্বে রেজিস্টার করা হয়েছিলো");
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
            Calendar calendar = Calendar.getInstance();
            String key = "#"+calendar.getTimeInMillis()+"#";

            MyDatabase db = new MyDatabase(this);
            db.registerUser(name, email,pass, key, "");
            db.close();

            startActivity(new Intent(getApplicationContext(), Login.class));
        }
        else
        {
            register_confirm_pass.setError("পাসওয়ার্ড মিলে নি। আবার চেক করুন");
            register_confirm_pass.requestFocus();
            return;
        }
    }

    boolean checkDuplicateEmail(String e){
        MyDatabase db = new MyDatabase(this);
        Cursor c = db.getAllKeyValues();
        String email ="";
        while(c.moveToNext()){
            email = c.getString(1);
            Log.d("TAG", "checkDuplicateEmail: "+email);
            if(email.equals(e)) return true;
        }
        return false;
    }
}