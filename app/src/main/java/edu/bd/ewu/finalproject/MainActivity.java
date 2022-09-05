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
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toogle;
    String uid= FirebaseAuth.getInstance().getUid();
    DatabaseReference databaseReference, user_jikirs;
    ArrayList<JikirData> jikirData = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();
    TextView  nav_header_username, nav_header_usermail, plus_btn, jikir_name, jikir_meaning, count, wakto, hijriDate,
    sunRiseTime, sunSetTime;
    CircleImageView nav_pro_pic;
    NavigationView navigationView;
    ListView todays_jikirs;
    LinearLayout jikir_list_layout, no_jikir_layout;
    long length;
    int selectedItemId;
    ProgressBar jikir_progress, jikir_listview_progress;
    ArrayList<String> completed_ids = new ArrayList<>();

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
        wakto = findViewById(R.id.wakto);
        hijriDate = findViewById(R.id.arabic_date);
        sunRiseTime = findViewById(R.id.sun_rise_time);
        sunSetTime = findViewById(R.id.sun_set_time);

        View header = navigationView.getHeaderView(0);
        nav_header_username = header.findViewById(R.id.nav_header_username);
        nav_header_usermail = header.findViewById(R.id.nav_header_user_email);
        nav_pro_pic = header.findViewById(R.id.nav_header_propic);

        jikir_name =  findViewById(R.id.jikir);
        jikir_meaning =  findViewById(R.id.jikir_meaning);
        count =  findViewById(R.id.counter);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.tap_sound);

        //Big Circle Click to Update Jikir Count Data
        count.setOnClickListener(v -> {
            mediaPlayer.start();
            MakeEnglishtoBangla metob = new MakeEnglishtoBangla();
            if(!completed_ids.contains(String.valueOf(selectedItemId))){
                user_jikirs.child(uid).child("Jikirs").child(java.time.LocalDate.now().toString())
                        .child(String.valueOf(selectedItemId))
                        .child("count")
                        .setValue(String.valueOf(Integer.parseInt(metob.makeReverse((String) count.getText())) + 1));
            }
            else{
                Toast.makeText(this, "আলহামদুলিল্লাহ আপনি এটি সম্পন্ন করে ফেলেছেন", Toast.LENGTH_SHORT).show();
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
        else if(item.getItemId() == R.id.history){
            startActivity(new Intent(this, History.class));
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        jikir_name.setVisibility(View.GONE);
        jikir_meaning.setVisibility(View.GONE);
        jikir_progress.setVisibility(View.VISIBLE);
        jikir_listview_progress.setVisibility(View.VISIBLE);

        //fetching user current jikirs
        user_jikirs.child(uid).child("Jikirs").child(java.time.LocalDate.now().toString())
                .addValueEventListener(new ValueEventListener() {
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
                            if (count.equals(target)) completed_ids.add(id);
                            JikirData data = new JikirData(id, name, meaning, benefit, count, target);
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
                        }
                        SharedPreferences pref = getSharedPreferences("HASITEMID", MODE_PRIVATE);
                        SharedPreferences.Editor prefEditor = pref.edit();

                        TodayJikirsAdapter cAdapter = new TodayJikirsAdapter(MainActivity.this,
                                jikirData, jikir_name, jikir_meaning, count, user_jikirs, completed_ids);
                        cAdapter.seletedItemId = pref.getInt("selectedId", 0);
                        todays_jikirs.setAdapter(cAdapter);

                        todays_jikirs.setOnItemClickListener((parent, view, position, id) -> {

                            cAdapter.seletedItemId = position;
                            prefEditor.putInt("selectedId", position);
                            prefEditor.apply();
                            selectedItemId = Integer.parseInt(jikirData.get(position).id);
                            System.out.println("IN Adapter: "+selectedItemId);
                            cAdapter.notifyDataSetChanged();
                        });

                        todays_jikirs.setOnItemLongClickListener((AdapterView.OnItemLongClickListener) (parent, view, position, id) -> {
                            Intent detailsIntent = new Intent(MainActivity.this, JikirDetails.class);
                            detailsIntent.putExtra("jikir_id", jikirData.get(position).getId());
                            detailsIntent.putExtra("jikir_name", jikirData.get(position).getName());
                            detailsIntent.putExtra("jikir_meaning", jikirData.get(position).getMeaning());
                            detailsIntent.putExtra("jikir_count", jikirData.get(position).getCount());
                            detailsIntent.putExtra("jikir_benefit", jikirData.get(position).getBenefit());
                            startActivity(detailsIntent);
                            return true;
                        });

                        if(!jikirData.isEmpty()){
                            selectedItemId = Integer.parseInt(jikirData.get(pref.getInt("selectedId", 0)).id);
                            prefEditor.apply();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "https://api.aladhan.com/v1/calendar?latitude=23.7111512&longitude=90.4898135&method=2&month=9&year=2022";
        @SuppressLint("SetTextI18n") JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        Date date = new Date();
                        String currentDate = formatter.format(date);
                        JSONArray jsonArray = response.getJSONArray("data");
                        int index = 0;
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject timingObject = jsonArray.getJSONObject(i);
                            JSONObject dates = timingObject.getJSONObject("date");
                            JSONObject currentDateObject = dates.getJSONObject("gregorian");
                            String apiDate = currentDateObject.getString("date");
                            if (apiDate.equals(currentDate)) {
                                index = i;
                                break;
                            }
                        }

                        JSONObject timingObject = jsonArray.getJSONObject(index);
                        JSONObject namajTimes = timingObject.getJSONObject("timings");
                        JSONObject dates = timingObject.getJSONObject("date");
                        JSONObject hijriObject = dates.getJSONObject("hijri");

                        String Fajr = namajTimes.getString("Fajr");
                        String Sunrise = namajTimes.getString("Sunrise");
                        String Dhuhr = namajTimes.getString("Dhuhr");
                        String Asr = namajTimes.getString("Asr");
                        String Sunset = namajTimes.getString("Sunset");
                        String Maghrib = namajTimes.getString("Maghrib");
                        String Isha = namajTimes.getString("Isha");
                        String hijri_date = hijriObject.getString("date");

                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
                        Date time = new Date();
                        String currentTime = timeFormatter.format(time);
                        LocalTime currentLocalTime = LocalTime.parse(currentTime);
                        LocalTime fajrLocalTime = LocalTime.parse(Fajr.split(" ")[0]);
                        LocalTime juhorLocalTime = LocalTime.parse(Dhuhr.split(" ")[0]);
                        LocalTime asrLocalTime = LocalTime.parse(Asr.split(" ")[0]);
                        LocalTime maghribLocalTime = LocalTime.parse(Maghrib.split(" ")[0]);
                        LocalTime ishaLocalTime = LocalTime.parse(Isha.split(" ")[0]);

                        MakeEnglishtoBangla metob = new MakeEnglishtoBangla();
                        sunRiseTime.setText(makeTwelveHour(Sunrise, 0) + " a.m");
                        sunSetTime.setText(makeTwelveHour(Sunset, 12) + " p.m");
                        hijriDate.setText(metob.make(hijri_date.split("-")[0])+" ই "+metob.makeHijriDate(hijri_date.split("-")[1])+" "+
                                metob.make(hijri_date.split("-")[2]) +" হিজরী");

                        if(currentLocalTime.compareTo(juhorLocalTime) < 0 && currentLocalTime.compareTo(ishaLocalTime) > 0){
                            wakto.setText("ফজরঃ "+makeTwelveHour(Fajr, 0)+"-"+makeTwelveHour(Dhuhr, 0));
                        }
                        else if(currentLocalTime.compareTo(asrLocalTime) < 0 && currentLocalTime.compareTo(fajrLocalTime) > 0){
                            wakto.setText("যুহরঃ "+makeTwelveHour(Dhuhr, 0)+"-"+makeTwelveHour(Asr, 11));
                        }
                        else if(currentLocalTime.compareTo(maghribLocalTime) < 0 && currentLocalTime.compareTo(juhorLocalTime) > 0){
                            wakto.setText("আসরঃ "+makeTwelveHour(Asr, 11)+"-"+makeTwelveHour(Maghrib, 12));
                        }
                        else if(currentLocalTime.compareTo(ishaLocalTime) < 0 && currentLocalTime.compareTo(asrLocalTime) > 0){
                            wakto.setText("মাগরিবঃ "+makeTwelveHour(Maghrib, 12)+"-"+makeTwelveHour(Isha, 12));
                        }
                        else if(currentLocalTime.compareTo(maghribLocalTime) > 0){
                            wakto.setText("ইশাঃ "+makeTwelveHour(Isha, 12)+"-"+makeTwelveHour(Fajr, 0));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> error.printStackTrace());
        mQueue.add(request);
    }
    public String makeTwelveHour(String namaj,int tobeSub){
        MakeEnglishtoBangla metob = new MakeEnglishtoBangla();
        String hour = String.valueOf(Integer.parseInt(namaj.split(" ")[0].split(":")[0]) - tobeSub);
        String min = namaj.split(" ")[0].split(":")[1];

        return metob.make(hour+":"+min);
    }
}