package com.example.oms_2;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.LocalDateTime; // import the LocalDateTime class
import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static com.example.oms_2.OMSConstants.rootUrl;

public class biddingAPI extends AppCompatActivity{
    public static final String myApiKey = "";
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    TextView bidMessage;


    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid_success);
        String userID;
        bidMessage = findViewById(R.id.bidMessage);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                userID= null;
            } else {
                userID= extras.getString("UserID");
            }
        } else {
            userID= (String) savedInstanceState.getSerializable("UserID");
        }
        bid(userID);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void bid(String initiatorId) {
        String type = "Open";
        LocalDateTime dateCreated = LocalDateTime.now();
        String date = dateCreated + "Z";
        String subjectId = "148e0af0-699b-4c1f-9e49-4de8816d121e";
        String additionalInfoTag = "Hours";
        String additionalInfoRequest = "Flexible Hours required";
        String usersUrl = rootUrl + "/bid";
        String json =
        "{" +
                "\"type\":\"" + type + "\"," +
                "\"initiatorId\":\"" + initiatorId + "\"," +
                "\"dateCreated\":\"" + date + "\"," +
                "\"subjectId\":\"" + subjectId + "\"," +
                "\"additionalInfo\":"   +   "{" + "\"" + additionalInfoTag + "\":" + "\""+ additionalInfoRequest + "\"" + "}" +

                "}";
        RequestBody body = RequestBody.create(json, JSON);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(usersUrl)
                .header("Authorization", myApiKey)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {

                if (response.isSuccessful()) {

                    biddingAPI.this.runOnUiThread(() -> {
                        System.out.println(Objects.requireNonNull(response.body()).toString());
                        bidMessage.setText("Bid Successful");
                    });
                }
                else{
                    System.out.println(response);
                }
            }
        });
    }

}

