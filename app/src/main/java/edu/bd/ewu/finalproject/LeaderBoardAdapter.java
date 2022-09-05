package edu.bd.ewu.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LeaderBoardAdapter extends ArrayAdapter<LeaderBoardData> {
    private final Context context;
    MakeEnglishtoBangla metob = new MakeEnglishtoBangla();
    DatabaseReference userDataRef = FirebaseDatabase.getInstance().getReference("USERS");

    public LeaderBoardAdapter(@NonNull Context context, ArrayList<LeaderBoardData> allData) {
        super(context, R.layout.leaderboard_template, allData);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.leaderboard_template, parent, false);
        }

        TextView pos = convertView.findViewById(R.id.position_number);
        TextView name = convertView.findViewById(R.id.leaderborad_name);
        TextView email = convertView.findViewById(R.id.leaderborad_email);
        TextView points = convertView.findViewById(R.id.leaderboard_points);

        pos.setText(position+1+" .");
        userDataRef.child(getItem(position).getUid()).child("User Information").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("username").getValue().toString());
                email.setText(snapshot.child("email").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        points.setText(metob.make(getItem(position).getPoints())+" পয়েন্টস");
        return convertView;
    }
}
