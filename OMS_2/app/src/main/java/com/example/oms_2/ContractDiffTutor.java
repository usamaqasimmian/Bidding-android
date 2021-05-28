package com.example.oms_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;
import java.util.Map;

import static com.example.oms_2.OMSConstants.myApiKey;
import static com.example.oms_2.OMSConstants.rootUrl;

/**
 * This class is used to display the tutors who are 2 levels above the one mentioned in the
 * contract. So the student can select a tutor from the dropdown list to sign and thus reusing
 * a previous contract. That is, same request/offer from contract A tutor A but now use it to sign with tutor B.
 */
public class ContractDiffTutor extends AppCompatActivity {

    private Spinner select_diff_tut_spinner;
    private Button confirm_diff_tutor;
    private static String subjectIDNow;
    private static String theSelectedTut;
    private List<String> dispTutList = new ArrayList<>();

    //getter and setter
    public List<String> getDispTutList(){ return dispTutList; }
    public void setDispTutList(List<String> dtl){ this.dispTutList = dtl; }
    public static String getSubjectIDNow(){ return subjectIDNow; }
    public void setSubjectIDNow(String subjidn){ this.subjectIDNow = subjidn; }
    public static String getTheSelectedTut(){ return theSelectedTut; }
    public void setTheSelectedTut(String tst){ this.theSelectedTut = tst; }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contract_diff_tutor);

        //retrieve subject id from ContractsRec
        Intent intt = getIntent();
        subjectIDNow = intt.getStringExtra("subjIDFromHere");
        setSubjectIDNow(subjectIDNow);
        displayTutorList(subjectIDNow);

    }

    /**
     * This method loops through the GET /subject?fields=competencies endpoint to compare
     * the selected contract's subject id, and then loops through the GET /competency endpoint
     * to find tutors who are at least two levels higher than the subject level.
     * And finally displays those tutors in a dropdown list.
     * @param sIDNow
     */
    public void displayTutorList(String sIDNow){
        //get the subject's competency level
        RequestQueue aQueue = Volley.newRequestQueue(this);
        String subjCompURL = rootUrl + "/subject?fields=competencies";
        JsonArrayRequest arequest = new JsonArrayRequest(Request.Method.GET, subjCompURL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i=0 ; i<response.length() ; i++){
                                JSONObject eachSub = response.getJSONObject(i);
                                String subjIdToCompare = eachSub.getString("id");
                                if (subjIdToCompare.equals(sIDNow)){
                                    //if there's a match of subject id, retrieve the competency
                                    JSONArray competencyArray = eachSub.getJSONArray("competencies");
                                    JSONObject compObj = competencyArray.getJSONObject(0);
                                    int subjlevel = compObj.getInt("level");

                                    //now that we have the subject level, let's look for tutor's levels
                                    RequestQueue bQueue = Volley.newRequestQueue(ContractDiffTutor.this);
                                    String compURL = rootUrl + "/competency";
                                    JsonArrayRequest brequest = new JsonArrayRequest(Request.Method.GET, compURL, null,
                                            new Response.Listener<JSONArray>() {
                                                @Override
                                                public void onResponse(JSONArray response) {
                                                    try {
                                                        for (int j=0 ; j<response.length() ; j++){
                                                            JSONObject eachComp = response.getJSONObject(j);
                                                            JSONObject owner = eachComp.getJSONObject("owner");
                                                            boolean isTutor = owner.getBoolean("isTutor");
                                                            //if a tutor is found
                                                            if (isTutor){
                                                                //retrieve tutor's level
                                                                int tutLevel = eachComp.getInt("level");

                                                                //if tutor level is 2 levels above subject level, display tutor in dropdown list
                                                                if (tutLevel >= subjlevel+2){
                                                                    String tutid = owner.getString("id");
                                                                    String tutgname = owner.getString("givenName");
                                                                    String tutfname = owner.getString("familyName");
                                                                    String element = tutgname + " " + tutfname;
                                                                    if (!getDispTutList().contains(element)){
                                                                        dispTutList.add(element);
                                                                    }
                                                                    setDispTutList(dispTutList);
                                                                    ArrayAdapter<String> aadap = spinnerAdapterStr(getDispTutList());
                                                                    select_diff_tut_spinner = findViewById(R.id.select_diff_tut_spinner);
                                                                    select_diff_tut_spinner.setAdapter(aadap);
                                                                    select_diff_tut_spinner.setOnItemSelectedListener(spinnerSelect);

                                                                }
                                                            }
                                                        }

                                                        String confTutFullName = select_diff_tut_spinner.getSelectedItem().toString();
                                                        searchTutorId(confTutFullName);

                                                    } catch (JSONException f) {
                                                        f.printStackTrace();
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
                                    bQueue.add(brequest);

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
        aQueue.add(arequest);
    }


    /**
     * This method returns a collection of objects specified by the list provided
     * in the argument so that it can be used for the spinner.
     * @param aList list of strings
     * @return a collection of objects
     */
    public ArrayAdapter<String> spinnerAdapterStr(List<String> aList){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, aList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
    }

    //attribute for upon clicking on dropdown subject list
    private final AdapterView.OnItemSelectedListener spinnerSelect = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String item = parent.getItemAtPosition(position).toString();
            Toast.makeText(parent.getContext(), item, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    /**
     * This method looks up the id of the provided tutor's name through the GET /user endpoint.
     * The tutor name comes from the selected tutor dropdown list.
     * @param fullname
     */
    public void searchTutorId(String fullname){
        RequestQueue myQueue = Volley.newRequestQueue(this);
        String userUrl = rootUrl + "/user";
        JsonArrayRequest myrequest = new JsonArrayRequest(Request.Method.GET, userUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int k=0 ; k<response.length() ; k++){
                                JSONObject eachUser = response.getJSONObject(k);
                                boolean isTutor = eachUser.getBoolean("isTutor");
                                if (isTutor){
                                    String gname = eachUser.getString("givenName");
                                    String fname = eachUser.getString("familyName");
                                    if (fullname.contains(gname) && fullname.contains(fname)){
                                        String idRetrieve = eachUser.getString("id");
                                        setTheSelectedTut(idRetrieve);  //retrieved later in PostReusedContract

                                    }
                                }
                            }
                            confirm_diff_tutor = findViewById(R.id.confirm_diff_tutor);
                            confirm_diff_tutor.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ContractDiffTutor.this, PostReusedContract.class);
                                    ContractDiffTutor.this.startActivity(intent);
                                }
                            });

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
        myQueue.add(myrequest);
    }

    //menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id == R.id.action_Logout) {
            Intent intent = new Intent(com.example.oms_2.ContractDiffTutor.this, LoginPage.class);
            ContractDiffTutor.this.startActivity(intent);
        } else if (id == R.id.homepage) {
            Intent intent = new Intent(com.example.oms_2.ContractDiffTutor.this, StudentLoggedIn.class);
            ContractDiffTutor.this.startActivity(intent);
        }
        return true;
    }


}
