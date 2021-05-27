package com.example.oms_2;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.BaseAdapter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.oms_2.OMSConstants.myApiKey;
import static com.example.oms_2.OMSConstants.rootUrl;

public class ViewContracts extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    String userID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_contracts);
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);  //A RecyclerView.LayoutManager implementation which provides similar functionality to ListView.
        recyclerView.setLayoutManager(layoutManager);
        userID = LoginPage.getStudId();
        makeRequest();

    }

    private void makeRequest() {
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

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        try {
                            JSONArray array = new JSONArray(Objects.requireNonNull(response.body()).string());
                            ArrayList<ContractViewItems> dataItems = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject row = array.getJSONObject(i);
                                JSONObject student = row.getJSONObject("secondParty");
                                String studentID = student.getString("id");
                                JSONObject tutor = row.getJSONObject("firstParty");
                                String tutorID = tutor.getString("id");
                                if (studentID.equals(userID) || tutorID.equals(userID)){
                                    String signed = row.getString("dateSigned");
                                    String expiryDate = row.getString("expiryDate");
                                    String payment = row.getJSONObject("paymentInfo").toString();
                                    String lesson = row.getJSONObject("lessonInfo").toString();
                                    ContractViewItems contractViewItems = new ContractViewItems("Student ID: " + studentID,"Tutor ID: " + tutorID, "Date Signed: " + signed, "Date Expired: "+ expiryDate,
                                            "Payment Info: " + payment,"Lesson Info: " + lesson);
                                    dataItems.add(contractViewItems);
                                    adapter = new ContractsRec(dataItems);
                                    new Handler(Looper.getMainLooper()).post(() -> recyclerView.setAdapter(adapter));


                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }).start();
                }
            }
        });
    }



}
