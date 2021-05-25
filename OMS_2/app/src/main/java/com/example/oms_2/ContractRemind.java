package com.example.oms_2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.oms_2.OMSConstants.myApiKey;
import static com.example.oms_2.OMSConstants.rootUrl;

public class ContractRemind extends AppCompatActivity {
    private static final String CHANNEL_ID = "REMIND" ;
    String userID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         userID = LoginPage.getStudId();
         checkAllContracts();
    }

    public void checkAllContracts(){
        String getAllUsers = rootUrl + "/contract";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getAllUsers)
                .header("Authorization", myApiKey)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                LocalDateTime dateCreated = LocalDateTime.now();
                LocalDateTime dateMonth = dateCreated.plusDays(30);
                String dateMonthTime = dateMonth + "Z";
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        try {
                            JSONArray array = new JSONArray(Objects.requireNonNull(response.body()).string());
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject row = array.getJSONObject(i);
                                JSONObject student = row.getJSONObject("firstParty");
                                String studentID = student.getString("id");
                                JSONObject tutor = row.getJSONObject("secondParty");
                                String tutorID = tutor.getString("id");
                                if (studentID.equals(userID) || tutorID.equals(userID)){
                                    String expiryDate = row.getString("dateCreated");
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                                    Date parsedDate = format.parse(expiryDate);
                                    Date futureDate = format.parse(dateMonthTime);

                                    if ((parsedDate.compareTo(futureDate)) < 1){
                                        // if contract expiry date is 1 month or less than a notification will be made
                                        makeNotification();
                                    }

                                    makeNotification();
                                }
                            }
                            redirectUser();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }).start();
                }
            }
        });

    }

    public void makeNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        redirectUser();
    }

    public void redirectUser(){
        Intent intent = getIntent();
        String login = intent.getStringExtra("login");
        if (login.equals("Both")) {
            Intent newIntent = new Intent(ContractRemind.this, StudentTutorBoth.class);
            ContractRemind.this.startActivity(newIntent);
        }
        else if (login.equals("Student")){
            Intent newIntent = new Intent(ContractRemind.this, StudentLoggedIn.class);
            ContractRemind.this.startActivity(newIntent);
        }
        else {
            Intent newIntent = new Intent(ContractRemind.this, TutorLoggedIn.class);
            ContractRemind.this.startActivity(newIntent);
        }
    }
    }
