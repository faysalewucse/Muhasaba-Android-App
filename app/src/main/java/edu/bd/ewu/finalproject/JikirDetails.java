package edu.bd.ewu.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JikirDetails extends AppCompatActivity {

    TextView jikir_name, jikir_meaning, jikir_count, jikir_benefit, todays_count, all_time_counts;
    MakeEnglishtoBangla metob = new MakeEnglishtoBangla();
    int total = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jikir_details);

        jikir_name = findViewById(R.id.jikir_details_name);
        jikir_meaning = findViewById(R.id.jikir_details_meaning);
        jikir_count = findViewById(R.id.jikir_details_count);
        jikir_benefit = findViewById(R.id.fojilotText);
        todays_count = findViewById(R.id.child_jikir_count1);
        all_time_counts = findViewById(R.id.child_jikir_count2);

        Intent i = getIntent();
        System.out.println();
        jikir_name.setText(i.getStringExtra("jikir_name"));
        jikir_meaning.setText(i.getStringExtra("jikir_meaning"));
        jikir_count.setText(metob.make(i.getStringExtra("jikir_count")));
        jikir_benefit.setText(i.getStringExtra("jikir_benefit"));
        todays_count.setText(metob.make(i.getStringExtra("jikir_count")) + " বার");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = getIntent();
        String uid= FirebaseAuth.getInstance().getUid();
        String jikir_id = i.getStringExtra("jikir_id");
        DatabaseReference usersJikirs = FirebaseDatabase.getInstance().getReference("USERS");
        usersJikirs.child(uid).child("Jikirs")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            for (DataSnapshot dataSnapshot: ds.getChildren()){
                                String id = dataSnapshot.child("id").getValue().toString();
                                if(id.equals(jikir_id)){
                                    total += Integer.parseInt(dataSnapshot.child("count").getValue().toString());
                                }
                            }
                        }
                        all_time_counts.setText(metob.make(String.valueOf(total)) + " বার");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }
}