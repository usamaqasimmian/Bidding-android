package com.example.oms_2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.annotation.WorkerThread;
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
        String allContracts = rootUrl + "/contract";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(allContracts)
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
                                System.out.println("Hello");
                                JSONObject row = array.getJSONObject(i);
                                JSONObject student = row.getJSONObject("firstParty");
                                String studentID = student.getString("id");
                                JSONObject tutor = row.getJSONObject("secondParty");
                                String tutorID = tutor.getString("id");
                                redirectUser();
                                if (studentID.equals(userID) || tutorID.equals(userID)){
                                    String expiryDate = row.getString("dateCreated");
                                    @SuppressLint("SimpleDateFormat") Date futureDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").parse(dateMonthTime);
                                    @SuppressLint("SimpleDateFormat") Date parsedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").parse(expiryDate);
                                    if ((parsedDate.compareTo(futureDate)) < 1){
                                        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getApplicationContext(),"One or More Contracts are about to expire",Toast.LENGTH_SHORT).show());
                                    }
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



    public void redirectUser(){
        Intent intent = getIntent();
        String login = intent.getStringExtra("login");
        switch (login) {
            case "Both": {
                Intent newIntent = new Intent(ContractRemind.this, StudentTutorBoth.class);
                ContractRemind.this.startActivity(newIntent);
                break;
            }
            case "Student": {
                Intent newIntent = new Intent(ContractRemind.this, StudentLoggedIn.class);
                ContractRemind.this.startActivity(newIntent);
                break;
            }
            case "Tutor": {
                Intent newIntent = new Intent(ContractRemind.this, TutorLoggedIn.class);
                ContractRemind.this.startActivity(newIntent);
                break;
            }
        }
    }
    }
