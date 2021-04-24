package com.example.oms_2;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

//show all tutors with their subjects
public class ViewAllTutors extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private BidCardItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_tutors);
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);  //A RecyclerView.LayoutManager implementation which provides similar functionality to ListView.
        recyclerView.setLayoutManager(layoutManager);
        makeRequest();
    }

    private void makeRequest() {
        String competency = rootUrl + "/competency";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(competency)
                .header("Authorization", myApiKey)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    ViewAllTutors.this.runOnUiThread(() -> {

                        try {
                            JSONArray array = new JSONArray(Objects.requireNonNull(response.body()).string());
                            ArrayList<TutorViewItems> dataItems = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject row = array.getJSONObject(i);
                                JSONObject owner = row.getJSONObject("owner");
                                if (owner.getBoolean("isTutor")) {
                                    String name = owner.getString("givenName") + " " + owner.getString("familyName");
                                    String id = owner.getString("id");
                                    JSONObject subject = row.getJSONObject("subject");
                                    String subjName = subject.getString("name");
                                    String subjDescr = subject.getString("description");
                                    String subjectId = subject.getString("id");
                                    String level = (String.valueOf(row.getInt("level")));
                                    TutorViewItems tutorViewItems = new TutorViewItems(name, "Teaching: "+subjName+" - "+subjDescr, "Competency: "+level, id, subjectId);
                                    dataItems.add(tutorViewItems);

                                    adapter = new RecyclerAdapter(dataItems);
                                    // assign the adapter to the recycler view
                                    recyclerView.setAdapter(adapter);
                                }
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
}



