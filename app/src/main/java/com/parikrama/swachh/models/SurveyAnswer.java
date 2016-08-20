package com.parikrama.swachh.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Anurag
 */
public class SurveyAnswer implements Serializable {

    public SurveyAnswer(String id, String response) {
        this.id = id;
        this.response = response;
    }

    public String id;
    public String response;
}
