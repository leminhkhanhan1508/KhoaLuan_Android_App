package com.example.attendence.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestNewLesson {
    @SerializedName("code_course")
    @Expose
    private String codeCourse;
    @SerializedName("code_user")
    @Expose
    private String codeUser;
    @SerializedName("date_lesson")
    @Expose
    private String dateLesson;
    @SerializedName("name_lesson")
    @Expose
    private String nameLesson;

    public String getCodeCourse() {
        return codeCourse;
    }

    public void setCodeCourse(String codeCourse) {
        this.codeCourse = codeCourse;
    }

    public String getCodeUser() {
        return codeUser;
    }

    public void setCodeUser(String codeUser) {
        this.codeUser = codeUser;
    }

    public String getDateLesson() {
        return dateLesson;
    }

    public void setDateLesson(String dateLesson) {
        this.dateLesson = dateLesson;
    }

    public String getNameLesson() {
        return nameLesson;
    }

    public void setNameLesson(String nameLesson) {
        this.nameLesson = nameLesson;
    }
}
