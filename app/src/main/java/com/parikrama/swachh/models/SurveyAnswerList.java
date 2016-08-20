package com.parikrama.swachh.models;

import java.util.HashMap;

/**
 * @author Anurag
 */
public class SurveyAnswerList {

    private static HashMap<String, SurveyAnswer> answerList;

    public static synchronized HashMap<String, SurveyAnswer> getAnswerList(){
        if(answerList == null) {
            return new HashMap<String, SurveyAnswer>();
        }
        return answerList;
    }
}
