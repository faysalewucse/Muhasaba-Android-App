package edu.bd.ewu.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    ExpandableListView history_list;
    ExpandableListAdapter expandableListAdapter;
    TextView no_history;
    ProgressBar history_pb;
    String uid= FirebaseAuth.getInstance().getUid();
    DatabaseReference usersJikirs;
    ArrayList<String> dates = new ArrayList<>();
    ArrayList<HistoryData> allHistory = new ArrayList<>();

    long length;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        history_list = findViewById(R.id.history_listview);
        no_history = findViewById(R.id.no_history_yet);
        history_pb = findViewById(R.id.history_progress_bar);

        usersJikirs = FirebaseDatabase.getInstance().getReference("USERS");

    }

    @Override
    protected void onStart() {
        super.onStart();
        //fetching user current jikirs
        usersJikirs.child(uid).child("Jikirs")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        length = snapshot.getChildrenCount();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            ArrayList<JikirData> data = new ArrayList<>();
                            for (DataSnapshot dataSnapshot: ds.getChildren()){
                                String id = dataSnapshot.child("id").getValue().toString();
                                String name = dataSnapshot.child("name").getValue().toString();
                                String meaning = dataSnapshot.child("meaning").getValue().toString();
                                String benefit = dataSnapshot.child("benefit").getValue().toString();
                                String count = dataSnapshot.child("count").getValue().toString();
                                String target = dataSnapshot.child("target").getValue().toString();
                                JikirData jikirData = new JikirData(id, name, meaning, benefit, count, target);
                                data.add(jikirData);
                                dates.add(ds.getKey());
                            }
                            HistoryData hd = new HistoryData(ds.getKey(), data);
                            allHistory.add(hd);
                        }
                        if(length == 0){
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("YES GONE");
                            history_pb.setVisibility(View.GONE);
                            no_history.setVisibility(View.VISIBLE);
                        }
                        else{
                            history_pb.setVisibility(View.GONE);
                            no_history.setVisibility(View.GONE);
                            history_list.setVisibility(View.VISIBLE);
                        }
                        //HistoryAdapter hAdapter = new HistoryAdapter(History.this, allHistory);
                        expandableListAdapter = new HistoryAdapter(History.this, allHistory, dates);
                        history_list.setAdapter(expandableListAdapter);
                        history_list.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                            int lastExpandedPosition = -1;
                            @Override
                            public void onGroupExpand(int groupPosition) {
                                if(lastExpandedPosition != -1 && groupPosition != lastExpandedPosition){
                                    history_list.collapseGroup(lastExpandedPosition);
                                }
                                lastExpandedPosition = groupPosition;
                            }
                        });
                        history_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                            @Override
                            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                                String selected = expandableListAdapter.getChild(groupPosition, childPosition).toString();
                                Toast.makeText(History.this, "Selected: "+selected, Toast.LENGTH_SHORT).show();
                                return true;
                            }
                        });
//                        history_list.setOnItemClickListener((parent, view, position, id) -> {
//                            cAdapter.seletedItemId = position;
//                            cAdapter.notifyDataSetChanged();
//                        });
                        System.out.println(allHistory.get(0).date);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }
}