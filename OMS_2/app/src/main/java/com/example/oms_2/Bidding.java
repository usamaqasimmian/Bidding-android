package com.example.oms_2;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime; // import the LocalDateTime class
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static com.example.oms_2.OMSConstants.rootUrl;

public class Bidding extends AppCompatActivity{
    public static final String myApiKey = "cPCG87WPdz8gmWTnqzbhCFGrTDHrrp";
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    TextView bidMessage;
    String userID;


    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid_success);
        bidMessage = findViewById(R.id.bidMessage);
        bid();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void bid() {
        Intent intent = getIntent();
        String initiatorId = LoginPage.getStudId(); //working
        String type = intent.getStringExtra(StudBiddingForm.EXTRA_TOGGLE); //the type
        LocalDateTime dateCreated = LocalDateTime.now(); //time 2021-04-17T07:20:17.918
        String date = dateCreated + "Z";
        String subjectId = StudBiddingForm.getTheSubjId(); //subject id
        String additionalInfoTag = "AdditionalInfo";
        String additionalInfoRequest = "Flexible Hours required"; //any additional information
        String usersUrl = rootUrl + "/bid";
        String json =
        "{" +
                "\"type\":\"" + type + "\"," +
                "\"initiatorId\":\"" + initiatorId + "\"," +
                "\"dateCreated\":\"" + date + "\"," +
                "\"subjectId\":\"" + subjectId + "\"," +
                "\"additionalInfo\":"   +   "{" + "\"" + additionalInfoTag + "\":" + "\""+ additionalInfoRequest + "\"" + "}" +

                "}";
        System.out.println(json);
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

                    Bidding.this.runOnUiThread(() -> {
                        System.out.println(Objects.requireNonNull(response.body()).toString());
                        bidMessage.setText("Bid Successful");
                        setTimer();
                    });
                }
                else{
                    System.out.println(response);
                }
            }
        });
    }

    private void setTimer(){
        AlarmManager processTimer = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, TimeOutBid.class);
        intent.putExtra("userID", userID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,  intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 30); //Minutes
        processTimer.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

}

