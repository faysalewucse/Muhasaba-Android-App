package edu.bd.ewu.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class LeaderBoard extends AppCompatActivity {

    DatabaseReference leaderBoardDataRef;
    String uid = FirebaseAuth.getInstance().getUid();
    ListView leaderBoardListView;
    ArrayList<LeaderBoardData> allData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        leaderBoardListView = findViewById(R.id.leaderboard_listview);
    }

    @Override
    protected void onStart() {
        super.onStart();
        leaderBoardDataRef = FirebaseDatabase.getInstance().getReference("LEADERBOARD");
        leaderBoardDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    LeaderBoardData lbd = new LeaderBoardData(data.getKey(), data.child("points").getValue().toString());
                    allData.add(lbd);
                }

                for (int i = 0; i <allData.size(); i++) {
                    for (int j = i+1; j <allData.size(); j++) {
                        int pointsI = Integer.parseInt(allData.get(i).points);
                        int pointsJ = Integer.parseInt(allData.get(j).points);
                        LeaderBoardData temp;
                        if(pointsI < pointsJ) {      //swap elements if not in order
                            temp = allData.get(i);
                            allData.set(i, allData.get(j));
                            allData.set(j, temp);
                        }
                    }
                }
                LeaderBoardAdapter lAdapter = new LeaderBoardAdapter(LeaderBoard.this, allData);
                leaderBoardListView.setAdapter(lAdapter);
//                lAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}