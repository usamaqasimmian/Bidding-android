package com.example.oms_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.oms_2.OMSConstants.myApiKey;
import static com.example.oms_2.OMSConstants.rootUrl;

/**
 * This class displays all open requests (made by student) for the tutor.
 */
public class TutorViewRequests extends AppCompatActivity {

    private ArrayList<CardItem> mListOfBids;
    private RecyclerView mRecyclerView;
    private BidCardItemAdapter mAdapter;                //bridge between data (exampleList) and RecyclerView
    private RecyclerView.LayoutManager mLayoutManager;  //aligns every single item in the list

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid_card_item_recyclerview);

        if (TutorLoggedIn.bidOnBids.equals("bidOnOpenBids")) {
            populateList("open");
        }
        else if (TutorLoggedIn.bidOnBids.equals("bidOnCloseBids")){
            populateList("close");
        }
    }

    /**
     * Fills up the request cards.
     * @param type
     */
    public void populateList(String type){
        mListOfBids = new ArrayList<>();

        RequestQueue mQueue = Volley.newRequestQueue(this);
        String bidUrl = rootUrl + "/bid";
        //json objects bounded by {} are found inside the json array bounded by []
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, bidUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                //retrieve subject(name+desc) and additional info(contain the other details)
                                JSONObject eachObject = response.getJSONObject(i);
                                if ( (eachObject.getString("type").toLowerCase().equals(type)) && (eachObject.getJSONObject("additionalInfo").length() == 5)){    //get open bids only
                                    JSONObject subject = eachObject.getJSONObject("subject");
                                    String subjectName = subject.getString("name");
                                    String subjectDesc = subject.getString("description");
                                    String resSubj = subjectName + ": " + subjectDesc;

                                    JSONObject addInfo = eachObject.getJSONObject("additionalInfo");
                                    String tutorQualification = addInfo.getString("tutorQualification");
                                    String numOfSess = addInfo.getString("numOfSess");
                                    String ratePerSess = addInfo.getString("ratePerSess");
                                    String timeOfSess = addInfo.getString("timeOfSess");
                                    String daysOfSess = addInfo.getString("daysOfSess");

                                    String bidID = eachObject.getString("id");

                                    String dateClosedDown = eachObject.getString("dateClosedDown");

                                    //only display requests that have not been closed down (where dateClosedDown is null)
                                    if (dateClosedDown.equals("null")) {
                                        mListOfBids.add(new CardItem("Request " + String.valueOf(i),
                                                "Subject: " + resSubj,
                                                "Tutor's Qualification: " + tutorQualification,
                                                "No. of sessions: " + numOfSess,
                                                "Rate: RM " + ratePerSess,
                                                "Time: " + timeOfSess,
                                                "Days: " + daysOfSess,
                                                "Click here to offer:\n" + bidID));
                                    }

                                }
                            }
                            setmListOfBids(mListOfBids);
                            buildRecyclerView();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", myApiKey);
                return headers;
            }
        };

        mQueue.add(request);
    }

    /**
     * Builds up the recycler view for the cards.
     */
    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);                //increases performance of app when we know that the recycler view won't change in size
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new BidCardItemAdapter(getmListOfBids(), R.layout.open_bid_card_item_card);         //this list gets passed to adapter which gets passed to viewholder

        mRecyclerView.setLayoutManager(mLayoutManager);     //pass the layoutmanager to recyclerview
        mRecyclerView.setAdapter(mAdapter);                 //pass adapter to recyclerview
    }

    //getters and setters
    public ArrayList<CardItem> getmListOfBids(){ return mListOfBids; }
    public void setmListOfBids(ArrayList<CardItem> l){ this.mListOfBids = l; }

    //menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id == R.id.action_Logout) {
            Intent intent = new Intent(com.example.oms_2.TutorViewRequests.this, LoginPage.class);
            TutorViewRequests.this.startActivity(intent);
        } else if (id == R.id.homepage) {
            Intent intent = new Intent(com.example.oms_2.TutorViewRequests.this, TutorLoggedIn.class);
            TutorViewRequests.this.startActivity(intent);
        }
        return true;
    }
}
