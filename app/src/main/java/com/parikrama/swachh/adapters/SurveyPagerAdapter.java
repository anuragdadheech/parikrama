package com.parikrama.swachh.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parikrama.swachh.R;
import com.parikrama.swachh.helpers.ResourceAccessHelper;
import com.parikrama.swachh.models.SurveyQuestion;

import java.util.ArrayList;

/**
 * @author Anurag
 */
public class SurveyPagerAdapter extends PagerAdapter {

    private ArrayList<SurveyQuestion> surveyQuestions = new ArrayList<>();

    public SurveyPagerAdapter() {
        super();
    }

    @Override
    public int getCount() {
        return 1 + (surveyQuestions.size() > 0 ? surveyQuestions.size() : 0);
    }

    public void setData(ArrayList<SurveyQuestion> surveyQuestions){
        this.surveyQuestions = surveyQuestions;
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup parent, int position) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (position == 0) {
            view = inflater.inflate(R.layout.intro_layout, parent, false);
        } else {
            view = inflater.inflate(R.layout.question_layout, parent, false);
            SurveyQuestion question = surveyQuestions.get(position - 1);
            SurveyQuestionViewHolder surveyQuestionViewHolder = new SurveyQuestionViewHolder(view);
            surveyQuestionViewHolder.bindData(question, position);
        }
        parent.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View)view);
    }
}
