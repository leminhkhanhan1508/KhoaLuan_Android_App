package com.example.attendence.Service;



import com.example.attendence.Model.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface AppService {
    //    @Headers("Content-Type:application/json")
    @GET("/courses/")
    Call<List<Course>> GetCourses();

    @POST("/attend/")
    Call<List<ResponseImage>> GetImage(@Body RequestImage requestImage);

    @POST("/join/")
    Call<ResponseMessage> JoinCourse(@Body RequestJoinCourse requestJoinCourse);
    @POST("/register/")
    Call<ResponseMessage> Register(@Body RequestRegister requestRegister);

    @POST("/login/")
    Call<ResponseLogin> Login(@Body RequestLogin requestLogin);
    @PUT("/attend/{idImage}/")
    Call<ResponseMessage>RenameImage(@Path("idImage") int id,
                                     @Body RequestRenameImage request);
    @PUT("/attend/report/{idImage}/")
    Call<ResponseMessage>ReportImage(@Path("idImage") int id,
                                     @Body RequestReportImage request);
    @POST("/courses/newcourse/")
    Call<ResponseMessage> CreateCourse(@Body RequestNewCourse requestNewCourse);
    @POST("/course/lesson/")
    Call<ResponseMessage> NewLesson(@Body RequestNewLesson requestNewLesson);
    @POST("/course/lesson/information/")
    Call<List<ResponseInfoLesson>> GetInfoLesson (@Body RequestInfoLesson requestInfoLesson);
    @POST("/course/information/")
    Call<List<ResponseInfoLesson>> GetInfoCourse (@Body RequestInfoCourse requestInfoCourse);


}
