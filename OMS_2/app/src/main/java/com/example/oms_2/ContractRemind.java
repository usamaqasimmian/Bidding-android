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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.oms_2.OMSConstants.myApiKey;
import static com.example.oms_2.OMSConstants.rootUrl;

/**
 * This class is used to set a reminder if a contract is soon to expire as soon as the user logs in.
 */
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
                String finalDateMonthTime = dateMonthTime.substring(0,dateMonthTime.length()-14);
                System.out.println(dateMonthTime);
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
                                redirectUser();
                                if (studentID.equals(userID) || tutorID.equals(userID)){
                                    String expiryDate = row.getString("dateCreated");
                                    expiryDate = expiryDate.substring(0,expiryDate.length()-14);
                                    @SuppressLint("SimpleDateFormat") Date futureDate = new SimpleDateFormat("yyyy-MM-dd").parse(finalDateMonthTime);
                                    @SuppressLint("SimpleDateFormat") Date parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(expiryDate);
                                    if ((parsedDate.compareTo(futureDate)) < 1){
                                        new Handler(Looper.getMainLooper()).post(() -> Toasty.warning(getApplicationContext(), "One or More Contracts are about to expire", Toast.LENGTH_SHORT, true).show());

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

    //menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        Intent intt = getIntent();
        String login = intt.getStringExtra("login");
        if (id == R.id.action_Logout) {
            Intent intent = new Intent(com.example.oms_2.ContractRemind.this, LoginPage.class);
            ContractRemind.this.startActivity(intent);
        } else if (id == R.id.homepage && login.equals("Student")) {
            Intent intent = new Intent(com.example.oms_2.ContractRemind.this, StudentLoggedIn.class);
            ContractRemind.this.startActivity(intent);
        } else if (id == R.id.homepage && login.equals("Tutor")) {
            Intent intent = new Intent(com.example.oms_2.ContractRemind.this, TutorLoggedIn.class);
            ContractRemind.this.startActivity(intent);
        }
        return true;
    }
}

