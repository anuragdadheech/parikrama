package com.parikrama.swachh.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Anurag
 */
public class SurveyQuestion implements Serializable {
    public QuestionType type;
    public String description;
    public String row;
    public ArrayList<String> answers;
}
