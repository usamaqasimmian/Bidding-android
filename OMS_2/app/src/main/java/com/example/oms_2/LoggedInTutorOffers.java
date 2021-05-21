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
 * This class displays all the offers made by logged-in tutor to the logged-in tutor,
 * allowing him/her to pick which one he/she wants to edit/update.
 */
public class LoggedInTutorOffers extends AppCompatActivity {

    private ArrayList<CardItem> pListOfOfferTut;
    private RecyclerView pRecyclerView;
    private LoggedInTutorOffersAdapter pAdapter;
    private RecyclerView.LayoutManager pLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_in_tutor_offers_recyclerview);
        fillUpCardsToBeUpdated();
    }

    /**
     * This method fills up the cards to view the logged-in tutor's offers
     * when tutor is directed to the page to update/edit their offers.
     * Editting offers is applicable only for open bids/requests.
     */
    public void fillUpCardsToBeUpdated(){
        pListOfOfferTut = new ArrayList<>();
        String loggedInTutorId = LoginPage.getTutorId();

        RequestQueue mQueue = Volley.newRequestQueue(this);
        String msgUrl = rootUrl + "/message";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, msgUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject each = response.getJSONObject(i);

                                if (each.getJSONObject("additionalInfo").length() == 6 && each.getString("content").equals("open")){

                                    JSONObject poster = each.getJSONObject("poster");
                                    String posterId = poster.getString("id");   //used to compare with logged-in tutor
                                    String contentType = each.getString("content");

                                    if (loggedInTutorId.equals(posterId)) {
                                        String msgId = each.getString("id");    //for title and clickable text
                                        String tGName = poster.getString("givenName");
                                        String tFName = poster.getString("familyName");
                                        JSONObject adInfo = each.getJSONObject("additionalInfo");
                                        String rate = adInfo.getString("rate");
                                        String hour = adInfo.getString("hour");
                                        String session = adInfo.getString("session");
                                        String moreInfo = adInfo.getString("moreInfo");
                                        String qualifs = adInfo.getString("qualifs");
                                        String compLvl = adInfo.getString("compLvl");

                                        String bidId = each.getString("bidId");

                                        //call GET /bid endpoint to find if the bid has been closed down (contract signed)
                                        RequestQueue nQueue = Volley.newRequestQueue(LoggedInTutorOffers.this);
                                        String bidUrl = rootUrl + "/bid";

                                        JsonArrayRequest nrequest = new JsonArrayRequest(Request.Method.GET, bidUrl, null,
                                                new Response.Listener<JSONArray>() {
                                                    @Override
                                                    public void onResponse(JSONArray response) {
                                                        try {
                                                            for (int k = 0; k < response.length(); k++) {
                                                                JSONObject eachBidObj = response.getJSONObject(k);
                                                                String bidIdHere = eachBidObj.getString("id");
                                                                if (bidId.equals(bidIdHere)){
                                                                    String getDateCD = eachBidObj.getString("dateClosedDown");
                                                                    //only display if bid is not closed down
                                                                    if (getDateCD.equals("null")){
                                                                        pListOfOfferTut.add(new CardItem("OfferID:"+msgId,
                                                                                "Type: "+contentType,
                                                                                "Tutor givenName: "+tGName,
                                                                                "Tutor familyName: "+tFName,
                                                                                "Rate per week: RM "+rate,
                                                                                "Hours per session: "+hour,
                                                                                "Sessions per week: "+session,
                                                                                "More info: "+moreInfo,
                                                                                "Tutor's Qualifications: "+qualifs,
                                                                                "Tutor's Competency: "+compLvl,
                                                                                "UPDATE OFFER:"+msgId));
                                                                        break; //because there's only gonna be one particular bid so no need to loop thru all
                                                                    }
                                                                }
                                                            }

                                                            setpListOfOfferTut(pListOfOfferTut);
                                                            buildThisRecyclerView();

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
                                        nQueue.add(nrequest);
                                    }
                                }
                            }
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
    public void buildThisRecyclerView(){
        pRecyclerView = findViewById(R.id.loggedInTutorOffersHere);
        pRecyclerView.setHasFixedSize(true);
        pLayoutManager = new LinearLayoutManager(this);
        pAdapter = new LoggedInTutorOffersAdapter(getpListOfOfferTut(), R.layout.logged_in_tutor_offers_cardview);

        pRecyclerView.setLayoutManager(pLayoutManager);
        pRecyclerView.setAdapter(pAdapter);
    }

    //getter and setter
    public ArrayList<CardItem> getpListOfOfferTut(){ return pListOfOfferTut; }
    public void setpListOfOfferTut(ArrayList<CardItem> plist){ this.pListOfOfferTut = plist; }
}
