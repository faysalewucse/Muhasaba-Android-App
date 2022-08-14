package edu.bd.ewu.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pref = getSharedPreferences("USER_INFO", MODE_PRIVATE);
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
}