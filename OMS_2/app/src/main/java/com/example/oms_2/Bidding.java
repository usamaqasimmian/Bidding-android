package com.example.oms_2;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import static com.example.oms_2.OMSConstants.myApiKey;
import static com.example.oms_2.OMSConstants.rootUrl;

/**
 * This class respond to either success or failure of a student creating a bid.
 * If successfull, the created bid is posted using the POST /bid endpoint.
 */
public class Bidding extends AppCompatActivity{

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    TextView bidMessage;
    String userID;
    Button studentBiddingForm;
    Button home;


    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid_success);
        bidMessage = findViewById(R.id.bidMessage);
        studentBiddingForm = findViewById(R.id.backToBiddingForm);
        home = findViewById(R.id.StudentLoggedIn);
        home.setOnClickListener(v -> {
            Intent activityChangeIntent = new Intent(Bidding.this, StudentLoggedIn.class);
            Bidding.this.startActivity(activityChangeIntent);
        });
        studentBiddingForm.setOnClickListener(v -> {
            Intent activityChangeIntent = new Intent(Bidding.this, StudBiddingForm.class);
            Bidding.this.startActivity(activityChangeIntent);
        });
        bid();

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void bid() {

            String initiatorId = LoginPage.getStudId(); //working

            String type = StudBiddingForm.getBidType(); //the bid type

            LocalDateTime dateCreated = LocalDateTime.now(); //time 2021-04-17T07:20:17.918
            String date = dateCreated + "Z";

            String subjectId = StudBiddingForm.getTheSubjId(); //subject id

            String infoTagQ = "tutorQualification",
                    infoTagS = "numOfSess",
                    infoTagR = "ratePerSess",
                    infoTagT = "timeOfSess",
                    infoTagD = "daysOfSess";

            String infoRQ = StudBiddingForm.getThequalif(),
                    infoRS = StudBiddingForm.getThesession(),
                    infoRR = StudBiddingForm.getTherate(),
                    infoRT = StudBiddingForm.getThetime(),
                    infoRD = StudBiddingForm.getThedays();

            String usersUrl = rootUrl + "/bid";
            String json =
                    "{" +
                            "\"type\":\"" + type + "\"," +
                            "\"initiatorId\":\"" + initiatorId + "\"," +
                            "\"dateCreated\":\"" + date + "\"," +
                            "\"subjectId\":\"" + subjectId + "\"," +
                            "\"additionalInfo\":" + "{" + "\"" +
                            infoTagQ + "\":" + "\"" + infoRQ + "\"," + "\"" +
                            infoTagS + "\":" + "\"" + infoRS + "\"," + "\"" +
                            infoTagR + "\":" + "\"" + infoRR + "\"," + "\"" +
                            infoTagT + "\":" + "\"" + infoRT + "\"," + "\"" +
                            infoTagD + "\":" + "\"" + infoRD + "\"" +
                            "}" +
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
                            String bidID = "id";
                            try {
                                JSONArray  array = new JSONArray(Objects.requireNonNull(response.body()).string());
                                JSONObject row = array.getJSONObject(0);
                                bidID = row.getString("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                                bidMessage.setText("Request Successful");
                                setTimer(bidID);



                        });
                    } else {
                        new Handler(Looper.getMainLooper()).post(() -> bidMessage.setText("Invalid Bid Created Please Try again"));
                    }
                }
            });
        }


    private void setTimer(String bidID){
        AlarmManager processTimer = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, TimeOutBid.class);
        intent.putExtra("bidID", bidID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,  intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 30); //Minutes
        processTimer.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

}

