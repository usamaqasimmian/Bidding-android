package com.example.oms_2;

import android.app.TimePickerDialog;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.oms_2.OMSConstants.myApiKey;
import static com.example.oms_2.OMSConstants.rootUrl;

/**
 * This class implements all the operations required for all the selected components in the request form.
 * Upon completion of the form, the student is directed to select either the home page or the request form page.
 */
public class StudRequestForm extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    //attributes for the xml layout components
    private Spinner bidSubject, qualif_dropdown, num_session, rate_per_session;
    private ToggleButton toggleBid;
    private Button time_picker, confirmCreateBid;
    private TextView textView11;
    private CheckBox checkMonday, checkTuesday, checkWednesday, checkThursday, checkFriday;

    private boolean toggle;                                     //attribute used for open-close toggle button
    private static String bidType;
    private List<String> subjects = new ArrayList<>();          //attribute for dropdown list of subjects
    private List<String> subjectsId = new ArrayList<>();        //attribute for the list of subjects ids
    private static String theSubjId;                            //attribute for the selected subject's id
    private List<String> qualifications = new ArrayList<>();    //attribute for dropdown list of qualifications
    private List<Integer> sessions = new ArrayList<>();         //attribute for dropdown list of number os sessions per week
    private List<Integer> rates = new ArrayList<>();            //attribute for dropdown list of rates per session
    private List<String> checkBoxList = new ArrayList<>();      //attribute used to add checked box values
    private static String thequalif, thesession, therate, thetime, thedays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_request_form);

        //initialise components
        num_session = findViewById(R.id.num_session);
        rate_per_session = findViewById(R.id.rate_per_session);
        toggleBid = findViewById(R.id.toggleBid);
        time_picker = findViewById(R.id.time_picker);
        confirmCreateBid = findViewById(R.id.confirmCreateBid);

        dropdownSubjList();     //operations for selecting subject from dropdown subject list
        dropdownQualList();     //operations for selecting tutor's qualifications

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

        checkBoxSelect();   //operations for selecting day

        toggleBid.setOnCheckedChangeListener(toggleBidOnCheckChangeListener);   //operations for clicking on open-close toggle button
        confirmCreateBid.setOnClickListener(confirmCreateBidOnClickListener);   //operations for clicking on 'confirm' button

    }

    //attribute for upon clicking on confirm button
    private final View.OnClickListener confirmCreateBidOnClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {
            callIntentPutExtras();
            Intent intent = new Intent(StudRequestForm.this, Bidding.class);
            StudRequestForm.this.startActivity(intent);
        }
    };

    /**
     * This method puts the student's choices in the form of string into intent as extras.
     * These can be accessed via other activities, or static getters can be used as an alternate.
     */
    public void callIntentPutExtras(){
        String selectedBid = toggleBid.getText().toString().toLowerCase();
        setBidType(selectedBid);

        String selectedSubject = bidSubject.getSelectedItem().toString();
        String selectedSubjId = getSubjectsId().get(getSubjects().indexOf(selectedSubject));
        setTheSubjId(selectedSubjId);

        String selectedQualif = qualif_dropdown.getSelectedItem().toString();
        setThequalif(selectedQualif);

        String selectedSession = num_session.getSelectedItem().toString();
        setThesession(selectedSession);

        String selectedRate = rate_per_session.getSelectedItem().toString();
        setTherate(selectedRate);

        String selectedTime = textView11.getText().toString();
        setThetime(selectedTime);

        StringBuilder sDays = new StringBuilder();
        for (String each: getCheckBoxList()){ sDays.append(each).append(","); }
        String selectedDays = sDays.deleteCharAt(sDays.length()-1).toString();
        setThedays(selectedDays);

    }

    //attribute for upon clicking on toggle
    private final CompoundButton.OnCheckedChangeListener toggleBidOnCheckChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            toggleBidChecked(isChecked);
        }
    };

    /**
     * This method sets the value of the toggle button.
     * @param isChecked boolean value
     */
    public void toggleBidChecked(boolean isChecked){
        setToggle(isChecked);
        if (isChecked){ setBidType("open"); }
        else{ setBidType("close"); }
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
     * This method adds the checked box value to a list of strings.
     */
    public void checkBoxSelect(){
        checkMonday = findViewById(R.id.checkMonday);
        checkTuesday = findViewById(R.id.checkTuesday);
        checkWednesday = findViewById(R.id.checkWednesday);
        checkThursday = findViewById(R.id.checkThursday);
        checkFriday = findViewById(R.id.checkFriday);

        checkMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkMonday.isChecked()){ checkBoxList.add("Monday"); }
                else{ checkBoxList.remove("Monday"); }
            }
        });
        checkTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkTuesday.isChecked()){ checkBoxList.add("Tuesday"); }
                else{ checkBoxList.remove("Tuesday"); }
            }
        });
        checkWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkWednesday.isChecked()){ checkBoxList.add("Wednesday"); }
                else{ checkBoxList.remove("Wednesday"); }
            }
        });
        checkThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkThursday.isChecked()){ checkBoxList.add("Thursday"); }
                else{ checkBoxList.remove("Thursday"); }
            }
        });
        checkFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFriday.isChecked()){ checkBoxList.add("Friday"); }
                else{ checkBoxList.remove("Friday"); }
            }
        });
        setCheckBoxList(checkBoxList);
    }

    /**
     * This method takes the time picked by the user and add it to a textview
     * which is displayed below the 'select time' button.
     * @param view
     * @param hourOfDay
     * @param minute
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        textView11 = findViewById(R.id.textView11);
        String min;
        if (minute < 10){ min = "0" + String.valueOf(minute); }
        else { min = String.valueOf(minute); }
        String t = hourOfDay + ":" + min;
        textView11.setText(t);
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
                                String subjectDesc = eachSubject.getString("description");
                                String subjectId = eachSubject.getString("id");
                                subjects.add(subjectName + ": " + subjectDesc);
                                subjectsId.add(subjectId);

                            }
                            setSubjects(subjects);
                            setSubjectsId(subjectsId);
                            ArrayAdapter<String> arrayAdapter1 = spinnerAdapterStr(getSubjects());
                            bidSubject = findViewById(R.id.bidSubject);
                            bidSubject.setAdapter(arrayAdapter1);
                            bidSubject.setOnItemSelectedListener(spinnerSelect);

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
                                ArrayAdapter<String> arrayAdapter2 = spinnerAdapterStr(getQualifications());
                                qualif_dropdown = findViewById(R.id.qualif_dropdown);
                                qualif_dropdown.setAdapter(arrayAdapter2);
                                qualif_dropdown.setOnItemSelectedListener(spinnerSelect);
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
        for (int i=1 ; i <= 5 ; i++){
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

    //Getters
    public static String getThequalif() { return thequalif; }
    public static String getThesession() { return thesession; }
    public static String getTherate() { return therate; }
    public static String getThedays() { return thedays; }
    public static String getThetime() { return thetime; }
    public List<String> getCheckBoxList(){ return checkBoxList; }
    public static String getBidType(){ return bidType; }
    public boolean getToggle(){ return toggle; }
    public List<Integer> getRates(){ return rates; }
    public List<Integer> getSessions(){ return sessions; }
    public List<String> getQualifications(){ return qualifications; }
    public static String getTheSubjId(){ return theSubjId; }
    public List<String> getSubjectsId(){ return subjectsId; }
    public List<String> getSubjects(){ return subjects; }

    //Setters
    public void setThetime(String thetime) { this.thetime = thetime; }
    public void setThedays(String thedays) { this.thedays = thedays; }
    public void setTherate(String therate) { this.therate = therate; }
    public void setThesession(String thesession) { this.thesession = thesession; }
    public void setThequalif(String thequalif) { this.thequalif = thequalif; }
    public void setCheckBoxList(List<String> cbl){ this.checkBoxList = cbl; }
    public void setBidType(String bt){ this.bidType = bt; }
    public void setToggle(boolean tog){ this.toggle = tog; }
    public void setRates(List<Integer> rts){ this.rates = rts; }
    public void setSessions(List<Integer> sess){ this.sessions = sess; }
    public void setQualifications(List<String> qualifs){ this.qualifications = qualifs; }
    public void setTheSubjId(String tsid){ this.theSubjId = tsid;}
    public void setSubjectsId(List<String> subjId){ this.subjectsId = subjId; }
    public void setSubjects(List<String> subjs){ this.subjects = subjs; }
}
