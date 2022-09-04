package edu.bd.ewu.finalproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Objects;

public class TodayJikirsAdapter extends ArrayAdapter<JikirData> {

    private final Context context;
    String uid= FirebaseAuth.getInstance().getUid();
    TextView jname, jikir_meaning, count;
    int seletedItemId = 0;
    DatabaseReference user_jikirs_history;
    MakeEnglishtoBangla metob = new MakeEnglishtoBangla();

    public TodayJikirsAdapter(Context context, ArrayList<JikirData> allJikirs, TextView jikir_name,
                              TextView jikir_meaning, TextView count, DatabaseReference user_jikirs_history) {
        super(context, R.layout.todays_jikir_listview_template, allJikirs);
        this.context = context;
        this.jname = jikir_name;
        this.jikir_meaning = jikir_meaning;
        this.count = count;
        this.user_jikirs_history = user_jikirs_history;
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

        jikir_edit_btn.setOnClickListener(v->{
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
            View mView = layoutInflaterAndroid.inflate(R.layout.jikir_edit_dialog_template, null);
            AlertDialog.Builder alertDialogBuilderLabelEdit = new AlertDialog.Builder(context);
            alertDialogBuilderLabelEdit.setView(mView);

            EditText present_count = mView.findViewById(R.id.present_jikir_editBox);
            EditText target_count = mView.findViewById(R.id.target_jikir_editBox);

            present_count.setText(current_count);
            target_count.setText(target);

            alertDialogBuilderLabelEdit.setPositiveButton("Save", (dialogBox, id12) -> {
                DatabaseReference ref = user_jikirs_history.child(uid).child("Jikirs").child(java.time.LocalDate.now().toString())
                        .child(String.valueOf(getItem(position).getId()));

                        ref.child("count").setValue(String.valueOf(Integer.parseInt(metob.makeReverse(present_count.getText().toString()))));
                        ref.child("target").setValue(String.valueOf(Integer.parseInt(metob.makeReverse(target_count.getText().toString()))));
            });
            alertDialogBuilderLabelEdit.setNegativeButton("Cancel",
                    (dialogBox, id1) -> dialogBox.cancel());

            AlertDialog alertDialogAndroid = alertDialogBuilderLabelEdit.create();
            alertDialogAndroid.show();
        });
        jikir_name.setText(name);
        count_target.setText(metob.make(current_count+"/"+target));

        if(seletedItemId == position){
            String nameOfJikir = getItem(seletedItemId).getName();
            String meaningOfJikir = getItem(seletedItemId).getMeaning();
            String current = getItem(seletedItemId).getCount();

            jname.setText(nameOfJikir);
            count.setText(metob.make(current));
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
