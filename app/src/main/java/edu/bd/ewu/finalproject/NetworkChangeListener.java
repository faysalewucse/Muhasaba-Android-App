package edu.bd.ewu.finalproject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        CheckInternetConnection checkInternetConnection = new CheckInternetConnection(context);
        if (!checkInternetConnection.isConnectingToInternet()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View layout_dialogue = LayoutInflater.from(context).inflate(R.layout.connection_alert_dialogue_design, null);
            builder.setView(layout_dialogue);

            AppCompatButton retry = layout_dialogue.findViewById(R.id.retry_btn);

            //Show Dialogue
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(false);

            dialog.getWindow().setGravity(Gravity.CENTER);

            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    onReceive(context, intent);
                }
            });
        }
    }
}
