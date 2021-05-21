package com.example.oms_2;

import android.os.Bundle;

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
 * This class retrieves and displays all offers made by tutors on the logged in student's bid,
 * based on whether the student is viewing open or close bids.
 */
public class StudViewOffers extends AppCompatActivity {

    private ArrayList<CardItem> aList;
    private RecyclerView xRecyclerView;
    private StudViewOffersAdapter xAdapter;
    private RecyclerView.LayoutManager xLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stud_view_offers_open_recyclerview);

        if (StudentLoggedIn.viewWhichOffers.equals("viewOffersOpen")){
            fillViewOffersList("open");
        }
        else if (StudentLoggedIn.viewWhichOffers.equals("viewOffersClose")){
            fillViewOffersList("close");
        }
    }

    /**
     * Fills up the offer cards' with the appropriate tutor's offer details.
     * @param content
     */
    public void fillViewOffersList(String content){
        aList = new ArrayList<>();
        String studID = LoginPage.getStudId();

        RequestQueue mQueue = Volley.newRequestQueue(this);
        String bidUrl = rootUrl + "/bid";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, bidUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i=0; i<response.length() ; i++){
                                JSONObject eachbid = response.getJSONObject(i);
                                JSONObject initiator = eachbid.getJSONObject("initiator");
                                String thatinitiatorId = initiator.getString("id");
                                String dateClosedDown = eachbid.getString("dateClosedDown");

                                if (thatinitiatorId.equals(studID)){
                                    //get that student's bid id
                                    String thatbidId = eachbid.getString("id");
                                    //now loop thg messages using the bid id to display on cards

                                    RequestQueue nQueue = Volley.newRequestQueue(StudViewOffers.this);
                                    String msgUrl = rootUrl + "/message";

                                    JsonArrayRequest reqX = new JsonArrayRequest(Request.Method.GET, msgUrl, null,
                                            new Response.Listener<JSONArray>() {
                                                @Override
                                                public void onResponse(JSONArray response) {
                                                    try {

                                                        for (int j=0; j<response.length() ; j++){
                                                            JSONObject eachMsgObj = response.getJSONObject(j);
                                                            String isMatchBidId = eachMsgObj.getString("bidId");
                                                            if (isMatchBidId.equals(thatbidId) && eachMsgObj.getString("content").equals(content)
                                                                    && eachMsgObj.getJSONObject("additionalInfo").getString("compLvl").contains("SubjectID")){
                                                                JSONObject posterX = eachMsgObj.getJSONObject("poster");
                                                                String posterId = posterX.getString("id");
                                                                String posterGName = posterX.getString("givenName");
                                                                String posterFName = posterX.getString("familyName");
                                                                String tutFullName = posterGName + " " + posterFName;
                                                                JSONObject addInfoX = eachMsgObj.getJSONObject("additionalInfo");
                                                                String rateX = addInfoX.getString("rate");
                                                                String hourX = addInfoX.getString("hour");
                                                                String sessionX = addInfoX.getString("session");
                                                                String moreInfoX = addInfoX.getString("moreInfo");
                                                                String qualifsX = addInfoX.getString("qualifs");
                                                                String compLvlX = addInfoX.getString("compLvl");

                                                                //student's view offers won't display bids that were closed down after contract signed for it
                                                                //will only display bids that have not been signed
                                                                if (dateClosedDown.equals("null")) {
                                                                    aList.add(new CardItem("BidId:" + isMatchBidId,
                                                                            "Tutor: " + tutFullName,
                                                                            "Rate per week: RM " + rateX,
                                                                            "Hours per session: " + hourX + ", Sessions per week: " + sessionX,
                                                                            "More Info: " + moreInfoX,
                                                                            "Tutor's Qualifications: " + qualifsX,
                                                                            "Tutor's Competency: " + compLvlX,
                                                                            "SELECT TUTOR:" + posterId));
                                                                }
                                                            }
                                                        }
                                                        setaList(aList);
                                                        buildTheRecyclerView();

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
                                    nQueue.add(reqX);
                                }
                            }
//                            buildTheRecyclerView();

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
     * Builds up the recycler view to display the cards.
     */
    public void buildTheRecyclerView(){
        xRecyclerView = findViewById(R.id.studViewOfferRecyclerView);
        xRecyclerView.setHasFixedSize(true);
        xLayoutManager = new LinearLayoutManager(this);
        xAdapter = new StudViewOffersAdapter(getaList(), R.layout.open_bid_card_item_card);

        xRecyclerView.setLayoutManager(xLayoutManager);
        xRecyclerView.setAdapter(xAdapter);
    }

    //getter and setter
    public ArrayList<CardItem> getaList() { return aList; }
    public void setaList(ArrayList<CardItem> aList) { this.aList = aList; }
}
