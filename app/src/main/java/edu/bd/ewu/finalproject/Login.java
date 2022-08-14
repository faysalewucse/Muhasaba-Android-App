package edu.bd.ewu.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class Login extends AppCompatActivity {

    private TextView register;
    private Button login;
    private EditText LogIn_email,LogIn_pass;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        register=findViewById(R.id.login_signUp);
        login=findViewById(R.id.logInBtn);
        LogIn_email=findViewById(R.id.login_email);
        LogIn_pass=findViewById(R.id.login_pass);
        progressBar=findViewById(R.id.LogIn_progressBar);
        progressBar.setVisibility(View.GONE);

        login.setOnClickListener(v -> userLogIn());
        register.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),SignUp.class);
            startActivity(intent);
            finish();
        });
    }
    private void userLogIn() {
        String email=LogIn_email.getText().toString().trim();
        String pass=LogIn_pass.getText().toString().trim();

        if(email.isEmpty())
        {
            LogIn_email.setError("ইমেইল বক্সটি খালি রাখা যাবেনা");
            LogIn_email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            LogIn_email.setError("একটি সঠিক ইমেইল প্রদান করুন");
            LogIn_email.requestFocus();
            return;
        }
        if(pass.isEmpty())
        {
            LogIn_pass.setError("পাসওয়ার্ড বক্স টি খালি রাখা যাবেনা");
            LogIn_pass.requestFocus();
            return;
        }
        //progressBar.setVisibility(View.VISIBLE);
        if(!chekUserAuthentication(email, pass).equals("")){
            MyDatabase myDatabase = new MyDatabase(this);
            UserInfo userInfo = myDatabase.getValueByKey(chekUserAuthentication(email,pass));
            SharedPreferences pref = getSharedPreferences("USER_INFO", MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = pref.edit();

            prefEditor.putString("USER_KEY", userInfo.user_key);
            prefEditor.putString("NAME", userInfo.username);
            prefEditor.putString("EMAIL", userInfo.email);
            prefEditor.putString("IMAGE", "");

            prefEditor.apply();
            progressBar.setVisibility(View.INVISIBLE);
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        if(pref.getString("USER_KEY", null) != null){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

    String chekUserAuthentication(String email, String pass){
        MyDatabase db = new MyDatabase(this);
        Cursor c = db.getAllKeyValues();
        String email_ ="", password = "";
        while(c.moveToNext()){
            email_ = c.getString(1);
            password = c.getString(2);

            if(email_.equals(email) && password.equals(pass)) return c.getString(3);
        }
        return "";
    }
}