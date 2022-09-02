package edu.bd.ewu.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toogle;
    String uid= FirebaseAuth.getInstance().getUid();
    DatabaseReference databaseReference, user_jikirs;
    ArrayList<JikirData> jikirData = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();
    TextView  nav_header_username, nav_header_usermail, plus_btn, jikir_name, jikir_meaning, count;
    CircleImageView nav_pro_pic;
    NavigationView navigationView;
    ListView todays_jikirs;
    LinearLayout jikir_list_layout, no_jikir_layout;
    long length;
    int selectedItemId;
    ProgressBar jikir_progress, jikir_listview_progress;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference("USERS");
        user_jikirs = FirebaseDatabase.getInstance().getReference("USERS");
        //fetching user personal information
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

        drawerLayout = findViewById(R.id.app_drawer);
        navigationView = findViewById(R.id.navigation);
        plus_btn = findViewById(R.id.plus_btn);
        jikir_list_layout = findViewById(R.id.jikir_list_layout);
        no_jikir_layout = findViewById(R.id.no_jikir);
        todays_jikirs = findViewById(R.id.todays_jikir_listview);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        jikir_progress = findViewById(R.id.jikir_progress);
        jikir_listview_progress = findViewById(R.id.jikir_listview_progress);

        View header = navigationView.getHeaderView(0);
        nav_header_username = header.findViewById(R.id.nav_header_username);
        nav_header_usermail = header.findViewById(R.id.nav_header_user_email);
        nav_pro_pic = header.findViewById(R.id.nav_header_propic);

        jikir_name =  findViewById(R.id.jikir);
        jikir_meaning =  findViewById(R.id.jikir_meaning);
        count =  findViewById(R.id.counter);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.tap_sound);
        jikir_name.setVisibility(View.GONE);
        jikir_meaning.setVisibility(View.GONE);
        jikir_progress.setVisibility(View.VISIBLE);
        jikir_listview_progress.setVisibility(View.VISIBLE);

        //fetching user current jikirs
        user_jikirs.child(uid).child("Jikirs").child(java.time.LocalDate.now().toString()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jikirData.clear();
                ids.clear();
                length = snapshot.getChildrenCount();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    String id = dataSnapshot.child("id").getValue().toString();
                    String name = dataSnapshot.child("name").getValue().toString();
                    String meaning = dataSnapshot.child("meaning").getValue().toString();
                    String benefit = dataSnapshot.child("benefit").getValue().toString();
                    String count = dataSnapshot.child("count").getValue().toString();
                    String target = dataSnapshot.child("target").getValue().toString();

                    ids.add(id);
                    JikirData data = new JikirData(id, name, meaning, benefit);
                    data.setCount(count);
                    data.setTarget(target);
                    jikirData.add(data);

                }

                if(length == 0){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    no_jikir_layout.setVisibility(View.VISIBLE);
                    jikir_progress.setVisibility(View.GONE);
                    jikir_listview_progress.setVisibility(View.GONE);
                    jikir_list_layout.setVisibility(View.GONE);
                }
                else{
                    jikir_progress.setVisibility(View.GONE);
                    jikir_listview_progress.setVisibility(View.GONE);
                    jikir_name.setVisibility(View.VISIBLE);
                    jikir_meaning.setVisibility(View.VISIBLE);
                    no_jikir_layout.setVisibility(View.GONE);
                    jikir_list_layout.setVisibility(View.VISIBLE);

                    selectedItemId = Integer.parseInt(jikirData.get(0).id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        TodayJikirsAdapter cAdaper = new TodayJikirsAdapter(getApplicationContext(),
                jikirData, jikir_name, jikir_meaning, count);
        todays_jikirs.setAdapter(cAdaper);
        todays_jikirs.setOnItemClickListener((parent, view, position, id) -> {

            cAdaper.seletedItemId = position;
            selectedItemId = Integer.parseInt(jikirData.get(position).id);
            cAdaper.notifyDataSetChanged();
        });

        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                System.out.println("CLICKED");
                MakeEnglishtoBangla metob = new MakeEnglishtoBangla();
                user_jikirs.child(uid).child("Jikirs").child(java.time.LocalDate.now().toString())
                        .child(String.valueOf(selectedItemId))
                        .child("count")
                        .setValue(String.valueOf(Integer.parseInt(metob.makeReverse((String) count.getText())) + 1));
            }
        });


        toogle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        plus_btn.setOnClickListener(v->{
            Intent i = new Intent(this, SelectJikir.class);
            i.putExtra("ids", ids);
            startActivity(i);
        });

        no_jikir_layout.setOnClickListener(v->{
            Intent i = new Intent(this, SelectJikir.class);
            i.putExtra("ids", ids);
            startActivity(i);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

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
        if(item.getItemId() == R.id.logout){
            showDialogue("আপনি কি শিওর যে লগ আউট করবেন?", "সত্যি?", "হ্যা", "বাদ দিন");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            showDialogue("আপনি কি শিওর যে লগ আউট করবেন?", "সত্যি?", "হ্যা", "বাদ দিন");
        }
        return true;
    }
}