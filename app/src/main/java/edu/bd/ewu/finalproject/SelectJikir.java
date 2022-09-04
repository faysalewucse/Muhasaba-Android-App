package edu.bd.ewu.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectJikir extends AppCompatActivity {

    ListView listView;
    TextView add_selected;
    DatabaseReference jikirList = FirebaseDatabase.getInstance().getReference("JikirList");
    ArrayList<JikirData> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_jikir);

        Intent i = getIntent();
        ArrayList<String> ids = i.getStringArrayListExtra("ids");
        Log.d("TAG", ids.toString());
        listView = findViewById(R.id.select_jikirs_listview);
        add_selected = findViewById(R.id.add_selected);
        jikirList.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                long length = snapshot.getChildrenCount();

                for (long i = 1; i <= length; i++) {
                    String id = snapshot.child(String.valueOf(i)).child("id").getValue().toString();
                    String name = snapshot.child(String.valueOf(i)).child("name").getValue().toString();
                    String meaning = snapshot.child(String.valueOf(i)).child("meaning").getValue().toString();
                    String benefit = snapshot.child(String.valueOf(i)).child("benefit").getValue().toString();

                    if(!ids.contains(id)){
                        JikirData jikirData = new JikirData(id, name, meaning, benefit, "0", "100");
                        data.add(jikirData);
                    }

                }
                SelectJikirAdapter adapter=new SelectJikirAdapter(getApplicationContext(), data, add_selected);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}