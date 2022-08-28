package edu.bd.ewu.finalproject;

import android.content.Context;
import android.content.Intent;
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

public class SelectJikirAdapter extends ArrayAdapter<JikirData> {

    private final Context context;
    ArrayList<JikirData> selectedJikirs = new ArrayList<>();
    TextView add_selected_btn;
    String uid= FirebaseAuth.getInstance().getUid();

    public SelectJikirAdapter( Context context, ArrayList<JikirData> allJikirs, TextView add_selected) {
        super(context, R.layout.all_jikirs_list_item_template, allJikirs);
        this.context = context;
        this.add_selected_btn = add_selected;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.all_jikirs_list_item_template, parent, false);
        }
        if(position%2==0){
            convertView.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg_rev));
        }

        String id = getItem(position).getId();
        String name = getItem(position).getName();
        String meaning = getItem(position).getMeaning();
        String benefit = getItem(position).getBenefit();

        TextView jikir_name = convertView.findViewById(R.id.select_jikirs_listview_listitem_jikir_text);
        CheckBox checkBox = convertView.findViewById(R.id.select_jikirs_listview_listitem_checkbox);
        jikir_name.setText(name);

        checkBox.setOnClickListener(v -> {
            JikirData jikirData = new JikirData(id, name, meaning, benefit);
            if(checkBox.isChecked())
            {
                selectedJikirs.add(jikirData);
            }
            else {
                if(!selectedJikirs.isEmpty()){
                    selectedJikirs.remove(jikirData);
                }
            }
            //Log.d("TAG", selectedJikirs.get(0).benefit);
//                System.out.println("SJ:"+selectedJikirs);
        });

        add_selected_btn.setOnClickListener(v -> {
            for (int i = 0; i < selectedJikirs.size();i++){
                //Log.d("TAG", selectedJikirs.get(i).benefit);
                FirebaseDatabase.getInstance().getReference("USERS").child(uid).child("Jikirs").
                        child(java.time.LocalDate.now().toString()).child(selectedJikirs.get(i).id).setValue(selectedJikirs.get(i));
            }
            context.startActivity(new Intent(context, MainActivity.class));
        });

        return convertView;
    }
}
