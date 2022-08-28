package edu.bd.ewu.finalproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TodayJikirsAdapter extends ArrayAdapter<JikirData> {

    private final Context context;
    String uid= FirebaseAuth.getInstance().getUid();

    public TodayJikirsAdapter(Context context, ArrayList<JikirData> allJikirs) {
        super(context, R.layout.todays_jikir_listview_template, allJikirs);
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todays_jikir_listview_template, parent, false);
        }

        View finalConvertView = convertView;
        String id = getItem(position).getId();
        String name = getItem(position).getName();
        String meaning = getItem(position).getMeaning();
        String benefit = getItem(position).getBenefit();

        TextView jikir_name = convertView.findViewById(R.id.today_jikirs_name);
//        convertView.setOnClickListener(v->{
//            finalConvertView.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_white_bg));
//            jikir_name.setTextColor(Color.parseColor("#000000"));
//        });

        jikir_name.setText(name);

        return convertView;
    }
}
