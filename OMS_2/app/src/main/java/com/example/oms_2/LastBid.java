package com.example.oms_2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.oms_2.OMSConstants.myApiKey;
import static com.example.oms_2.OMSConstants.rootUrl;

public class LastBid extends AppCompatActivity {
    String bidID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            bidID = extras.getString("bidID");
        }
        System.out.println(bidID);
        checkLastBid(bidID);
    }
//accpets the last bid after 30 minutes
    private void checkLastBid(String id){
        String getAllUsers = rootUrl + "/bid/" + id;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getAllUsers)
                .header("Authorization", myApiKey)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    LastBid.this.runOnUiThread(() -> {
                        try {
                                JSONObject row = new JSONObject(Objects.requireNonNull(response.body()).string());
                            JSONObject subject = row.getJSONObject("subject");
                                String subjectID = subject.getString("id");
                                try{
                                    JSONArray messages = row.getJSONArray("messages");
                                    int last = messages.length() - 1;
                                    JSONObject lastBid = messages.getJSONObject(last);
                                    JSONObject poster = lastBid.getJSONObject("poster");
                                    String TutorID = poster.getString("id");
                                    Intent intent = new Intent(LastBid.this, Contract.class);
                                    intent.putExtra("tutorid", TutorID);
                                    intent.putExtra("subjectid", subjectID);
                                    LastBid.this.startActivity(intent);

                                } catch (JSONException e) {
                                    bidClose(bidID);
                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });


    }
    //if no bid in 30 minutes request will be closed
    private void bidClose(String id){
        String getAllUsers = rootUrl + "/bid/" + id +"/close-down";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getAllUsers)
                .header("Authorization", myApiKey)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    LastBid.this.runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(),"Request Closed No Bid after 30 Minutes",Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

    }
    }
