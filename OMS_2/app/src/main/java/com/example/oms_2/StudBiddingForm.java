package com.example.oms_2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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

//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements all the operations required for all the selected components in the bidding form.
 * Upon completion of the form, the student is directed to either the Open or Close biddings page.
 */
public class StudBiddingForm extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private static final String myApiKey = "INSERT_API_KEY_HERE";
    private static final String rootUrl = "https://fit3077.com/api/v1";

    private boolean toggle;                                     //attribute used for open-close toggle button
    private List<String> subjects = new ArrayList<>();          //attribute for dropdown list of subjects
    private List<String> qualifications = new ArrayList<>();    //attribute for dropdown list of qualifications
    private List<Integer> sessions = new ArrayList<>();         //attribute for dropdown list of number os sessions per week
    private List<Integer> rates = new ArrayList<>();            //attribute for dropdown list of rates per session

    //attribute for upon clicking on toggle
    private final CompoundButton.OnCheckedChangeListener toggleBidOnCheckChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            toggleBidChecked(isChecked);
        }
    };

    //attribute for upon clicking on dropdown subject list
    private final AdapterView.OnItemSelectedListener spinnerSelect = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String item = parent.getItemAtPosition(position).toString();
            Toast.makeText(parent.getContext(), item, Toast.LENGTH_LONG).show();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    //attribute for upon clicking on confirm button
    private final View.OnClickListener confirmCreateBidOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            confirmCreateBidClicked();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bidding_form);

        //initialise components
        Spinner bidSubject = findViewById(R.id.bidSubject);
        Spinner qualif_dropdown = findViewById(R.id.qualif_dropdown);
        Spinner num_session = findViewById(R.id.num_session);
        Spinner rate_per_session = findViewById(R.id.rate_per_session);
        ToggleButton toggleBid = findViewById(R.id.toggleBid);
        Button time_picker = findViewById(R.id.time_picker);
        Button day_picker = findViewById(R.id.day_picker);
        EditText bidUserID = findViewById(R.id.bidUserID);
        Button confirmCreateBid = findViewById(R.id.confirmCreateBid);

        //operations for selecting subject from dropdown subject list
        dropdownSubjList();
        ArrayAdapter<String> arrayAdapter1 = spinnerAdapterStr(getSubjects());
        bidSubject.setAdapter(arrayAdapter1);
        bidSubject.setOnItemSelectedListener(spinnerSelect);

        //operations for selecting tutor's qualifications
        dropdownQualList();
        ArrayAdapter<String> arrayAdapter2 = spinnerAdapterStr(getQualifications());
        qualif_dropdown.setAdapter(arrayAdapter2);
        qualif_dropdown.setOnItemSelectedListener(spinnerSelect);

        //operations for selecting sessions per week
        dropdownSessionList();
        ArrayAdapter<Integer> arrayAdapter3 = spinnerAdapterInt(getSessions());
        num_session.setAdapter(arrayAdapter3);
        num_session.setOnItemSelectedListener(spinnerSelect);

        //operations for selecting rates per session
        dropdownRateList();
        ArrayAdapter<Integer> arrayAdapter4 = spinnerAdapterInt(getRates());
        rate_per_session.setAdapter(arrayAdapter4);
        rate_per_session.setOnItemSelectedListener(spinnerSelect);

        //operations for selecting time
        time_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        //operations for selecting day
        day_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        //operations for clicking on open-close toggle button
        toggleBid.setOnCheckedChangeListener(toggleBidOnCheckChangeListener);

        //operations for clicking on 'confirm' button
        confirmCreateBid.setOnClickListener(confirmCreateBidOnClickListener);
    }

    /////////NOT COMPLETE: MUST TAKE THESE VALUES (arguments) TO NEXT PAGE
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textView11 = findViewById(R.id.textView11);
        String t = hourOfDay + ":" + minute;
        textView11.setText(t);
    }

    /////////NOT COMPLETE: MUST TAKE THESE VALUES (arguments) TO NEXT PAGE
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        TextView textView12 = findViewById(R.id.textView12);
        textView12.setText(currDate);
    }

    /**
     * Getter for the attribute: subjects
     * @return a list of subjects
     */
    public List<String> getSubjects(){
        return subjects;
    }

    /**
     * Setter for the attribute: qualifications
     * @param subjs
     */
    public void setSubjects(List<String> subjs){
        this.subjects = subjs;
    }

    /**
     * Getter for the attribute: qualifications
     * @return a list containing tutor's qualifications
     */
    public List<String> getQualifications(){
        return qualifications;
    }

    /**
     * Setter for the attribute: qualifications
     * @param qualifs
     */
    public void setQualifications(List<String> qualifs){
        this.qualifications = qualifs;
    }

    /**
     * Getter for the attribute: sessions
     * @return a list of number of sessions
     */
    public List<Integer> getSessions(){
        return sessions;
    }

    /**
     * Setter for the attribut: sessions
     * @param sess
     */
    public void setSessions(List<Integer> sess){
        this.sessions = sess;
    }

    /**
     * Getter for the attribute: rates
     * @return  a list of integers
     */
    public List<Integer> getRates(){
        return rates;
    }

    /**
     * Setter for the attribute: rates
     * @param rts
     */
    public void setRates(List<Integer> rts){
        this.rates = rts;
    }

    /**
     * Getter for the attribute: toggle
     * @return a boolean value
     */
    public boolean getToggle(){
        return toggle;
    }

    /**
     * Setter for the attribute: toggle
     * @param tog
     */
    public void setToggle(boolean tog){
        this.toggle = tog;
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

    /**
     * This method returns a collection of objects specified by the list provided
     * in the argument so that it can be used for the spinner.
     * @param bList list of integers
     * @return a collection of objects
     */
    public ArrayAdapter<Integer> spinnerAdapterInt(List<Integer> bList){
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bList);
        arrayAdapter.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item));
        return arrayAdapter;
    }

    /**
     * This method retrieves the JSON array using GET /subject endpoint
     * and the API key, so as to fill up the drop-down list with the
     * subjects/lessons that the student seeks for the bid-request.
     */
    public void dropdownSubjList(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String subjectUrl = rootUrl + "/subject";
        //json objects bounded by {} are found inside the json array bounded by []
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, subjectUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i=0 ; i < response.length() ; i++) {
                                //retrieve each json object {} inside the json array
                                JSONObject eachSubject = response.getJSONObject(i);
                                String subjectName = eachSubject.getString("name");
                                if (!(getSubjects().contains(subjectName))){
                                    subjects.add(subjectName);
                                }
                            }
                            setSubjects(subjects);

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
     * This method retrieves the JSON array using GET /qualifications endpoint
     * and the API key, so as to fill up the drop-down list with the
     * tutor's qualifications that the student seeks for the bid-request.
     */
    public void dropdownQualList(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String qualUrl = rootUrl + "/qualification";
        //json objects bounded by {} are found inside the json array bounded by []
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, qualUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i=0 ; i < response.length() ; i++) {
                                //retrieve each json object {} inside the json array
                                JSONObject eachQual = response.getJSONObject(i);
                                String title = eachQual.getString("title");
                                String description = eachQual.getString("description");
                                String temp = title + ": " + description;
                                if (!(getQualifications().contains(temp))){
                                    qualifications.add(temp);
                                }
                                setQualifications(qualifications);
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
     * This method adds different number of sessions per week to the sessions list
     * so that the student can select from.
     */
    public void dropdownSessionList(){
        for (int i=1 ; i <= 3 ; i++){
            sessions.add(i);
        }
        setSessions(sessions);
    }

    /**
     * This method adds different rates per session to the rates list
     * so that the student can select from.
     */
    public void dropdownRateList(){
        for (int i=20 ; i <= 100 ; i+=20){
            rates.add(i);
        }
        setRates(rates);
    }

    ///////NOT COMPLETE: MUST ADD THE "open/close" STRING TO THE ENDPOINT
    public void toggleBidChecked(boolean isChecked){
        setToggle(isChecked);
    }

    /**
     * This method is called when the 'confirm' button is clicked so that
     * the student is taken to from the Bidding Form page to either the
     * Open Bidding page or the Close bidding page, depending on his bid-request choice.
     */
    ///////NOT COMPLETE: MUST TAKE/INCLUDE ALL THE CHOICES TO THE NEXT PAGE
    public void confirmCreateBidClicked(){
        Intent intent;
        if (getToggle()) {   //go to view current OPEN biddings page
            intent = new Intent(StudBiddingForm.this, StudViewOpenBids.class);
        } else {   //go to view current CLOSE biddings page
            intent = new Intent(StudBiddingForm.this, StudViewCloseBids.class);
        }
        StudBiddingForm.this.startActivity(intent);
    }


}
