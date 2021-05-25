package com.example.oms_2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
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
 * This class is used for the student to sign the contract and hence closing the bid-request.
 */
public class Contract extends AppCompatActivity {

    private String tutorId;
    private String subjectId;
    private String studentId;
    private String bidId;
    private LocalDateTime dateCreated;
    private TextView heading;
    private int months;

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contract);
        Intent intent = getIntent();
        tutorId = intent.getStringExtra("tutorid");
        subjectId = intent.getStringExtra("subjectid");
        bidId = intent.getStringExtra("bidId");             //got the bidId
        studentId = LoginPage.getStudId();
        dateCreated = LocalDateTime.now(); //time 2021-04-30T07:20:17.918
        Button signContract = findViewById(R.id.signContract);
        Button three = findViewById(R.id.three_months);
        Button six = findViewById(R.id.six_months);
        Button twelve = findViewById(R.id.twelve_months);
        Button twenty_four = findViewById(R.id.twenty_four_months);

        heading = findViewById(R.id.heading);

        signContract.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                months = 6;
                contract_create();
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                months = 3;
                contract_create();
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                months = 6;
                contract_create();
            }
        });

        twelve.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                months = 12;
                contract_create();
            }
        });

        twenty_four.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                months = 24;
                contract_create();
            }
        });

    }

    /**
     * This method will post to the json array using the POST /contract endpoint.
     * It is executed when student signs a contract with a tutor.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void contract_create() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, months);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(calendar.getTime());
        String expiry = date + "T23:59:59.999Z";
        String startDate = dateCreated + "Z";
        String usersUrl = rootUrl + "/contract";
        String additionalInfoTag = "Special Request";
        String additionalInfoRequest = "";
        String paymentInfoTag = "Payment Method";
        String paymentInfo = "";
        String lessonInfoTag = "Course Books";
        String lessonInfo = "";
        String json =
                "{" +
                        "\"firstPartyId\":\"" + studentId + "\"," +
                        "\"secondPartyId\":\"" + tutorId + "\"," +
                        "\"subjectId\":\"" + subjectId + "\"," +
                        "\"dateCreated\":\"" + startDate + "\"," +
                        "\"expiryDate\":\"" + expiry + "\"," +
                        "\"paymentInfo\":"   +   "{" + "\"" + paymentInfoTag + "\":" + "\""+ paymentInfo + "\"" + "}," +
                        "\"lessonInfo\":"  +   "{" + "\"" + lessonInfoTag + "\":" + "\""+ lessonInfo + "\"" + "}," +
                        "\"additionalInfo\":"   +   "{" + "\"" + additionalInfoTag + "\":" + "\""+ additionalInfoRequest + "\"" + "}" +
                        "}";
        RequestBody body = RequestBody.create(json, JSON);
        System.out.println(json);
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

            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {

                if (response.isSuccessful()) {

                    new Thread(() -> {
                        try {
                            JSONObject row = new JSONObject(Objects.requireNonNull(response.body()).string());
                            String contractID = row.getString("id");
//                            SignContract.setVisibility(View.GONE);
//                            expiryDate.setVisibility(View.GONE);
//                            heading.setText("Contract Created!");
                            contractSign(contractID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


//                    });
                    }).start();
                }
            }
        });

    }

    /**
     * This method will post to the json array using the POST /contract/sign endpoint.
     * It is executed when student signs a contract with a tutor.
     * @param ID
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void contractSign(String ID){
        System.out.println(ID);
        LocalDateTime startDate = dateCreated.plusDays(1);
        String date = startDate + "Z";
        String usersUrl = rootUrl + "/contract/" + ID + "/sign";
        String json = "{\"dateSigned\":\"" + date + "\"}";
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
                    Contract.this.runOnUiThread(() -> {
                        heading.setText("Contract Signed!");
                        closeBid();

                    });
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void closeBid(){
        LocalDateTime theDateClosedDown = LocalDateTime.now();
        String dateCD = theDateClosedDown + "Z";
        String jsonString = "{\"dateClosedDown\":\"" + dateCD + "\"}";
        String getAllUsers = rootUrl + "/bid/" + bidId +"/close-down";
        RequestBody body = RequestBody.create(jsonString, JSON);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getAllUsers)
                .header("Authorization", myApiKey)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    Contract.this.runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(),"Request has been closed",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(com.example.oms_2.Contract.this, StudentLoggedIn.class);
                        Contract.this.startActivity(intent);
                    });
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id == R.id.action_Logout) {
            Intent intent = new Intent(com.example.oms_2.Contract.this, LoginPage.class);
            Contract.this.startActivity(intent);
        }
        else if (id == R.id.view_all_tutors) {
            Intent intent = new Intent(com.example.oms_2.Contract.this, ViewAllTutors.class);
            Contract.this.startActivity(intent);
        }
        return true;
    }

}
