package com.example.oms_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This class is used to set the time limit for the bids.
 */
public class TimeOutBid extends BroadcastReceiver{

    private String bidID;

    @Override
    public void onReceive(Context context, Intent intent) {
        bidID = intent.getStringExtra("bidID");
        Intent i = new Intent();
        i.setClassName("com.example.oms_2", "com.example.oms_2.LastBid");
        i.putExtra("bidID",bidID);
        System.out.println(bidID);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}