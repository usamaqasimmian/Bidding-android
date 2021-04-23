package com.example.oms_2;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.oms_2.OMSConstants.myApiKey;
import static com.example.oms_2.OMSConstants.rootUrl;

public class TimeOutBid extends BroadcastReceiver{
    String bidID;

    @Override
    public void onReceive(Context context, Intent intent) {
        bidID = intent.getStringExtra("bidID");
        Intent i = new Intent();
        i.setClassName("com.example.oms_2", "com.example.oms_2.LastBid");
        i.putExtra("bidID",bidID);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }



}