package com.example.oms_2;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimeOutBid extends BroadcastReceiver {
    String userID;
    String bidID;

    @Override
    public void onReceive(Context context, Intent intent) {
        userID = intent.getStringExtra("userID");
        bidID = intent.getStringExtra("bidID");

    }

}
