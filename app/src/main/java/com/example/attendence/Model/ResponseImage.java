package com.example.attendence.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseImage  implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("code_student")
    @Expose
    private String codeStudent;


    @SerializedName("url_image")
    @Expose
    private String urlImage;
    @SerializedName("report")
    @Expose
    private Boolean report;
    @SerializedName("check_inf")
    @Expose
    private Boolean checkInf;
    @SerializedName("note")
    @Expose
    private String note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
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
    public String getCodeStudent() {
        return codeStudent;
    }

    public void setCodeStudent(String codeStudent) {
        this.codeStudent = codeStudent;
    }

}
