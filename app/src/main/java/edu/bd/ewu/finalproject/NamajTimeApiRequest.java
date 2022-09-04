package edu.bd.ewu.finalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;


import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NamajTimeApiRequest {
    Context context;
    DateTimeData dateTimeData;

    public NamajTimeApiRequest(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public DateTimeData httpRequest(){
        RequestQueue mQueue = Volley.newRequestQueue(context);
        String url = "https://api.aladhan.com/v1/calendar?latitude=23.7111512&longitude=90.4898135&method=2&month=9&year=2022";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        Date date = new Date();
                        String currentDate = formatter.format(date);
                        JSONArray jsonArray = response.getJSONArray("data");
                        int index = 0;
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject timingObject = jsonArray.getJSONObject(i);
                            JSONObject dates = timingObject.getJSONObject("date");
                            JSONObject currentDateObject = dates.getJSONObject("gregorian");
                            String apiDate = currentDateObject.getString("date");
                            if(apiDate.equals(currentDate)) {
                                index = i;
                                break;
                            }
                        }

                        JSONObject timingObject = jsonArray.getJSONObject(index);
                        JSONObject namajTimes = timingObject.getJSONObject("timings");
                        JSONObject dates = timingObject.getJSONObject("date");
                        JSONObject hijriObject = dates.getJSONObject("hijri");

                        String Fajr = namajTimes.getString("Fajr");
                        String Sunrise = namajTimes.getString("Sunrise");
                        String Dhuhr = namajTimes.getString("Dhuhr");
                        String Asr = namajTimes.getString("Asr");
                        String Sunset = namajTimes.getString("Sunset");
                        String Maghrib = namajTimes.getString("Maghrib");
                        String Isha = namajTimes.getString("Isha");
                        String hijri_date = hijriObject.getString("date");

                        dateTimeData = new DateTimeData(Fajr.split(" ")[0],
                                Sunrise.split(" ")[0], Dhuhr.split(" ")[0],
                                Asr.split(" ")[0], Maghrib.split(" ")[0],
                                Isha.split(" ")[0], Sunset.split(" ")[0], hijri_date);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> error.printStackTrace());
        mQueue.add(request);
        return dateTimeData;
    }
}

