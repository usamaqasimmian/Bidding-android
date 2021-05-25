package com.example.oms_2;

import org.json.JSONObject;

public class ContractViewItems {

    private String studentID;
    private String tutorID;
    private String dateCreated;
    private String dateExpired;
    private JSONObject payment;
    private JSONObject lesson;

    public ContractViewItems(String studentID, String tutorID, String dateCreated, String dateExpired, String payment, String lesson) {
        this.studentID = studentID;
        this.tutorID = tutorID;
        this.dateCreated = dateCreated;
        this.dateExpired = dateExpired;
        this.payment = payment;
        this.lesson = lesson;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getTutorID() {
        return tutorID;
    }

    public void setTutorID(String tutorID) {
        this.tutorID = tutorID;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateExpired() {
        return dateExpired;
    }

    public void setDateExpired(String dateExpired) {
        this.dateExpired = dateExpired;
    }

    public JSONObject getPayment() {
        return payment;
    }

    public void setPayment(JSONObject payment) {
        this.payment = payment;
    }

    public JSONObject getLesson() {
        return lesson;
    }

    public void setLesson(JSONObject lesson) {
        this.lesson = lesson;
    }
}
