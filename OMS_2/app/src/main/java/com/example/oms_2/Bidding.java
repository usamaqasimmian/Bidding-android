package com.example.oms_2;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime; // import the LocalDateTime class
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.oms_2.OMSConstants.JSON;
import static com.example.oms_2.OMSConstants.myApiKey;
import static com.example.oms_2.OMSConstants.rootUrl;

/**
 * This class respond to either success or failure of a student creating a bid.
 * If successfull, the created bid is posted using the POST /bid endpoint.
 */
public class Bidding extends AppCompatActivity{

    private TextView bidMessage;

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid_success);
        bidMessage = findViewById(R.id.bidMessage);
        Button studentRequestForm = findViewById(R.id.backToRequestForm);

        Button home = findViewById(R.id.StudentLoggedIn);
        home.setOnClickListener(v -> {
            Intent activityChangeIntent = new Intent(Bidding.this, StudentLoggedIn.class);
            Bidding.this.startActivity(activityChangeIntent);
        });

        studentRequestForm.setOnClickListener(v -> {
            Intent activityChangeIntent = new Intent(Bidding.this, StudRequestForm.class);
            Bidding.this.startActivity(activityChangeIntent);
        });

        bid();
    }

    /**
     * This method is used to post to the POST /bid endpoint.
     * It will post all the details from the student request form to the json array.
     */
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void bid() {

        String initiatorId = LoginPage.getStudId(); //working

        String type = StudRequestForm.getBidType(); //the bid type

        LocalDateTime dateCreated = LocalDateTime.now(); //time 2021-04-17T07:20:17.918
        String date = dateCreated + "Z";

        String subjectId = StudRequestForm.getTheSubjId(); //subject id

        String infoTagQ = "tutorQualification",
                infoTagS = "numOfSess",
                infoTagR = "ratePerSess",
                infoTagT = "timeOfSess",
                infoTagD = "daysOfSess";

        String infoRQ = StudRequestForm.getThequalif(),
                infoRS = StudRequestForm.getThesession(),
                infoRR = StudRequestForm.getTherate(),
                infoRT = StudRequestForm.getThetime(),
                infoRD = StudRequestForm.getThedays();

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

                    new Thread(() -> {
                        String bidID = "id";
                        try {
                            JSONObject row = new JSONObject(Objects.requireNonNull(response.body()).string());
                            bidID = row.getString("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        bidMessage.setText("Request Successful");
                        setTimer(bidID);
                    }).start();
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> bidMessage.setText("Invalid Bid Created Please Try again"));
                }
            }
        });
    }

    /**
     * This method will set a timer for the bids after creation.
     * @param bidID String for bid id
     */
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

