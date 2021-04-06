package com.example.attendence.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Request_Change_Info_Attendence {
    @SerializedName("code_student")
    @Expose
    private String codeStudent;
    @SerializedName("report")
    @Expose
    private Boolean report;
    @SerializedName("check_inf")
    @Expose
    private Boolean checkInf;
    @SerializedName("note")
    @Expose
    private String note;

    public String getCodeStudent() {
        return codeStudent;
    }

    public void setCodeStudent(String codeStudent) {
        this.codeStudent = codeStudent;
    }

    public Boolean getReport() {
        return report;
    }

    public void setReport(Boolean report) {
        this.report = report;
    }

    public Boolean getCheckInf() {
        return checkInf;
    }

    public void setCheckInf(Boolean checkInf) {
        this.checkInf = checkInf;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
