package com.example.attendence.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestJoinCourse {
    @SerializedName("code_user")
    @Expose
    private String codeUser;
    @SerializedName("code_course")
    @Expose
    private String codeCourse;
    @SerializedName("key_course")
    @Expose
    private String keyCourse;

    public String getCodeUser() {
        return codeUser;
    }

    public void setCodeUser(String codeUser) {
        this.codeUser = codeUser;
    }

    public String getCodeCourse() {
        return codeCourse;
    }

    public void setCodeCourse(String codeCourse) {
        this.codeCourse = codeCourse;
    }

    public String getKeyCourse() {
        return keyCourse;
    }

    public void setKeyCourse(String keyCourse) {
        this.keyCourse = keyCourse;
    }
}
