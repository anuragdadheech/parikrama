package com.parikrama.swachh.network;

import com.google.gson.JsonObject;
import com.parikrama.swachh.models.SurveyAnswer;
import com.parikrama.swachh.models.SurveyQuestion;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Anurag
 */
public interface NetworkAPI {
    @GET("/")
    Call<List<SurveyQuestion>> getSurveyData();
    @POST("/survey")
    Call<HashMap<String, SurveyAnswer>> sendSurveyData(@Body HashMap<String, SurveyAnswer> data);
}
