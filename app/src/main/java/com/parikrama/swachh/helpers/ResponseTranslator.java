package com.parikrama.swachh.helpers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.parikrama.swachh.models.QuestionList;
import com.parikrama.swachh.models.SurveyQuestion;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Anurag
 */
public class ResponseTranslator {
    private static ResponseTranslator sharedInstance;
    private Gson gson;

    private ResponseTranslator() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .create();
        }
    }

    public static synchronized ResponseTranslator getSharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new ResponseTranslator();
        }
        return sharedInstance;
    }

    public QuestionList getSurveyQuestions(JsonObject questionsJson) {
        try {
            return ResponseTranslator.getSharedInstance().gson.fromJson(questionsJson.getAsJsonObject(ResponseKeys.DATA), QuestionList.class);
        } catch (Exception e) {
            Log.e(ResponseTranslator.class.getSimpleName() + ": IO error", "IO error: " + e.getMessage());
        }
        return null;
    }

    public interface ResponseKeys {
        String DATA = "data";
        String META = "meta";
    }

}
