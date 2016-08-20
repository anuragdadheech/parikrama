package com.parikrama.swachh.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.parikrama.swachh.R;
import com.parikrama.swachh.models.SurveyAnswer;
import com.parikrama.swachh.models.SurveyAnswerList;
import com.parikrama.swachh.models.SurveyQuestion;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Anurag
 */
public class SurveyQuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    @Bind(R.id.question_text)
    TextView questionText;
    @Bind(R.id.answer)
    MaterialEditText answer;
    @Bind(R.id.radiogroup)
    RadioGroup radioGroup;

    public SurveyQuestionViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    public void bindData(final SurveyQuestion item, int position) {
        questionText.setText(item.description);
        switch (item.type) {
            case SELECT:
                radioGroup.setVisibility(View.VISIBLE);
                answer.setVisibility(View.GONE);
                addRadioButtons(item, position);
                break;
            case TEXT:
                radioGroup.setVisibility(View.GONE);
                answer.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {

    }

    public void addRadioButtons(final SurveyQuestion question, int position) {
        if (null == question || question.answers == null || question.answers.size() <= 0) {
            return;
        }
        int size = question.answers.size();
        for (int i = 0; i <= size - 1; i++) {
            RadioButton rdbtn = new RadioButton(itemView.getContext());
            rdbtn.setId(position*100 + i);
            rdbtn.setText(question.answers.get(i));
            radioGroup.addView(rdbtn);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SurveyAnswer answer = new SurveyAnswer(
                    question.row,
                    String.valueOf(((RadioButton) itemView.findViewById(checkedId)).getText())
                );
                SurveyAnswerList.getAnswerList().put(question.row, answer);
            }
        });
    }
}
