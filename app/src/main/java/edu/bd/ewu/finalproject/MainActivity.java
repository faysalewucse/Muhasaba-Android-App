package edu.bd.ewu.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toogle;
    String uid= FirebaseAuth.getInstance().getUid();
    DatabaseReference databaseReference;
    TextView  nav_header_username, nav_header_usermail;
    CircleImageView nav_pro_pic;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.app_drawer);
        navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        nav_header_username = header.findViewById(R.id.nav_header_username);
        nav_header_usermail = header.findViewById(R.id.nav_header_user_email);
        nav_pro_pic = header.findViewById(R.id.nav_header_propic);

        databaseReference = FirebaseDatabase.getInstance().getReference("USERS");
        databaseReference = FirebaseDatabase.getInstance().getReference("USERS");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @com.google.firebase.database.annotations.NotNull DataSnapshot snapshot) {

                String childname="User Information";
                String getname = snapshot.child(uid).child(childname).child("username").getValue().toString();
                String getemail = snapshot.child(uid).child(childname).child("email").getValue().toString();
                String getPic = snapshot.child(uid).child(childname).child("pro_pic").getValue().toString();

                nav_header_username.setText(getname);
                nav_header_usermail.setText(getemail);
                Picasso.get().load(getPic)
                        .placeholder(R.drawable.user_profile)
                        .into(nav_pro_pic);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        toogle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

//    @Override
//    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.option_menu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId() == R.id.logout){
//            showDialogue("আপনি কি শিওর যে লগ আউট করবেন?", "সত্যি?", "হ্যা", "বাদ দিন");
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void showDialogue(String msg, String title, String btn1, String btn2){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle(title);

        builder.setCancelable(false)
                .setPositiveButton(btn1, (dialog, which) -> {
                    if(btn1.equals("হ্যা")){
                        FirebaseAuth.getInstance().signOut();
                        finish();
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toogle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout){
            startActivity(new Intent(this, MyHistory.class));
        }
        return false;
    }
}