package com.parikrama.swachh.network;

import com.google.gson.JsonObject;
import com.parikrama.swachh.helpers.ResponseTranslator;
import com.parikrama.swachh.models.SurveyAnswer;
import com.parikrama.swachh.models.SurveyQuestion;

import java.util.HashMap;
import java.util.List;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Anurag
 */
public class NetworkService {

    private NetworkAPI networkAPI = null;

    public NetworkService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://parikrama-nlmm01.rhcloud.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        networkAPI = retrofit.create(NetworkAPI.class);
    }

    public void getSurveyData(final Callback<List<SurveyQuestion>> callback){
        Call<List<SurveyQuestion>> call = networkAPI.getSurveyData();
        call.enqueue(new Callback<List<SurveyQuestion>>() {
            @Override
            public void onResponse(Call<List<SurveyQuestion>> call, retrofit2.Response<List<SurveyQuestion>> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<List<SurveyQuestion>> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public void sendSurveyData(HashMap<String, SurveyAnswer> surveyData, final Callback<HashMap<String, SurveyAnswer>> callback){
        Call<HashMap<String, SurveyAnswer>> call = networkAPI.sendSurveyData(surveyData);
        call.enqueue(new Callback<HashMap<String, SurveyAnswer>>() {
            @Override
            public void onResponse(Call<HashMap<String, SurveyAnswer>> call, retrofit2.Response<HashMap<String, SurveyAnswer>> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<HashMap<String, SurveyAnswer>> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
}
