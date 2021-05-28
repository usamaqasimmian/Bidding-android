package com.example.oms_2;

import org.json.JSONObject;

/**
 * This class is a template class to be used by other classes, namely ViewContract,
 * such that the attributes here are used to fill up the contract cards.
 */
public class ContractViewItems {

    private String studentID;
    private String tutorID;
    private String dateCreated;
    private String dateExpired;
    private String payment;
    private String lesson;
    private String subjectIdH;

    public ContractViewItems(String studentID, String tutorID, String dateCreated, String dateExpired, String payment, String lesson, String sidh) {
        this.studentID = studentID;
        this.tutorID = tutorID;
        this.dateCreated = dateCreated;
        this.dateExpired = dateExpired;
        this.payment = payment;
        this.lesson = lesson;
        this.subjectIdH = sidh;
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

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getSubjectIdH(){ return subjectIdH; }

    public void setSubjectIdH(String ssidh){ this.subjectIdH = ssidh; }
}
