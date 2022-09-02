package edu.bd.ewu.finalproject;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class TodayJikirsAdapter extends ArrayAdapter<JikirData> {

    private final Context context;
    String uid= FirebaseAuth.getInstance().getUid();
    TextView jname, jikir_meaning, count;
    int seletedItemId = 0;

    public TodayJikirsAdapter(Context context, ArrayList<JikirData> allJikirs, TextView jikir_name,
                              TextView jikir_meaning, TextView count) {
        super(context, R.layout.todays_jikir_listview_template, allJikirs);
        this.context = context;
        this.jname = jikir_name;
        this.jikir_meaning = jikir_meaning;
        this.count = count;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todays_jikir_listview_template, parent, false);
        }

        String id = getItem(position).getId();
        String name = getItem(position).getName();
        String meaning = getItem(position).getMeaning();
        String benefit = getItem(position).getBenefit();
        String current_count = getItem(position).getCount();
        String target = getItem(position).getTarget();

        TextView jikir_name = convertView.findViewById(R.id.today_jikirs_name);
        TextView count_target = convertView.findViewById(R.id.countTarget);
        ImageView notifi_icon = convertView.findViewById(R.id.notificationBtn);
        ImageView jikir_edit_btn = convertView.findViewById(R.id.jikirEditBtn);

        jikir_name.setText(name);
        MakeEnglishtoBangla metob = new MakeEnglishtoBangla();
        count_target.setText(metob.make(current_count+"/"+target));

        if(seletedItemId == position){
            String nameOfJikir = getItem(seletedItemId).getName();
            String meaningOfJikir = getItem(seletedItemId).getMeaning();
            String current = getItem(seletedItemId).getCount();

            jname.setText(nameOfJikir);
            count.setText(current);
            jikir_meaning.setText(meaningOfJikir);
            convertView.setBackgroundResource(R.drawable.rounded_green_bg);
            jikir_name.setTextColor(Color.parseColor("#FFFFFF"));
            count_target.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else{
            convertView.setBackgroundResource(R.drawable.rounded_white_bg);
            jikir_name.setTextColor(Color.parseColor("#000000"));
            count_target.setTextColor(Color.parseColor("#000000"));
        }
        return convertView;
    }
}
