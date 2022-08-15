package edu.bd.ewu.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RadioButton;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        drawerLayout = findViewById(R.id.app_drawer);

        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        toogle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String key = pref.getString("USER_KEY", null);
        String email = pref.getString("NAME", null);
        String name = pref.getString("EMAIL", null);

        Log.d("TAG", "onCreate: "+key+email+name);
//        MyDatabase db = new MyDatabase(this);
//        Cursor c = db.getAllKeyValues();
//        String key="",va ="";
//        while(c.moveToNext()){
//            key = c.getString(0);
//            va = c.getString(1);
//            System.out.println("Key: "+ key+" "+"Value: "+va+c.getString(2));
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //For Preventing Back Press State Remaining
        SharedPreferences pref = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        if(pref.getString("USER_KEY", null) == null){
            startActivity(new Intent(this, Login.class));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout){
            showDialogue("আপনি কি শিওর যে লগ আউট করবেন?", "সত্যি?", "হ্যা", "বাদ দিন");
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogue(String msg, String title, String btn1, String btn2){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle(title);

        builder.setCancelable(false)
                .setPositiveButton(btn1, (dialog, which) -> {
                    if(btn1.equals("হ্যা")){
                        SharedPreferences pref = getSharedPreferences("USER_INFO", MODE_PRIVATE);
                        SharedPreferences.Editor prefEditor = pref.edit();

                        prefEditor.putString("USER_KEY", null);
                        prefEditor.putString("NAME", null);
                        prefEditor.putString("EMAIL", null);
                        prefEditor.putString("IMAGE", null);
                        prefEditor.apply();

                        startActivity(new Intent(this, Login.class));
                    }
                    else {
                        dialog.cancel();
                    }
                })
                .setNegativeButton(btn2, (dialog, which) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Log.d("TAG", "onNavigationItemSelected: "+item.getItemId());
        return false;
    }
}