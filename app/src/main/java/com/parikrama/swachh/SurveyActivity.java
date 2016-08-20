package com.parikrama.swachh;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.parikrama.swachh.adapters.SurveyPagerAdapter;
import com.parikrama.swachh.helpers.ResourceAccessHelper;
import com.parikrama.swachh.helpers.ResponseTranslator;
import com.parikrama.swachh.helpers.Utils;
import com.parikrama.swachh.models.QuestionList;
import com.parikrama.swachh.models.SurveyAnswer;
import com.parikrama.swachh.models.SurveyAnswerList;
import com.parikrama.swachh.models.SurveyQuestion;
import com.parikrama.swachh.network.NetworkService;

import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurveyActivity extends AppCompatActivity {

    @Bind(R.id.indicator)
    CircleIndicator indicator;
    @Bind(R.id.survey_pager)
    ViewPager pager;
    @Bind(R.id.survey_container)
    RelativeLayout container;
    @Bind(R.id.btn_previous)
    Button btnPrev;
    @Bind(R.id.btn_next)
    Button btnNext;
    @Bind(R.id.btn_submit)
    Button btnSubmit;
    private SurveyPagerAdapter pagerAdapter;
    private ArrayList<SurveyQuestion> questions = new ArrayList<>();
    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pagerAdapter = new SurveyPagerAdapter();
        pager.setAdapter(pagerAdapter);
        pagerAdapter.setData(questions);
        indicator.setViewPager(pager);
        initialize();
    }

    private void initialize() {
        btnNext.setVisibility(View.VISIBLE);
        btnPrev.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.GONE);
        pager.setPageMargin(Utils.convertDPTOPixels(this, 10));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    btnNext.setVisibility(View.VISIBLE);
                    btnPrev.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.GONE);
                } else if (position > 0 && position < questions.size()) {
                    btnNext.setVisibility(View.VISIBLE);
                    btnPrev.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.GONE);
                } else {
                    btnNext.setVisibility(View.GONE);
                    btnPrev.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Please wait..");
        mProgress.setTitle("Loading survey questions");
        mProgress.show();
        getSurveyQuestions();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.btn_previous)
    public void previous(View view) {
        pager.setCurrentItem(pager.getCurrentItem() - 1, true);
    }

    @OnClick(R.id.btn_next)
    public void next(View view) {
        pager.setCurrentItem(pager.getCurrentItem() + 1, true);
    }

    @OnClick(R.id.btn_submit)
    public void submit() {
        HashMap<String, SurveyAnswer> surveyData = SurveyAnswerList.getAnswerList();
        if (CollectionUtils.isNotEmpty(questions) && null != surveyData && surveyData.size() < questions.size()) {
            showErrorCrouton("Kindly answer all the questions!!", Snackbar.LENGTH_SHORT, false);
            return;
        }
        NetworkService networkService = new NetworkService();
        networkService.sendSurveyData(surveyData, new Callback<HashMap<String, SurveyAnswer>>() {
            @Override
            public void onResponse(Call<HashMap<String, SurveyAnswer>> call, Response<HashMap<String, SurveyAnswer>> response) {
                showSuccessCrouton("Your response has been successfully recorded!!");
                finish();
            }

            @Override
            public void onFailure(Call<HashMap<String, SurveyAnswer>> call, Throwable t) {
                showErrorCrouton("Unable to record your response", Snackbar.LENGTH_INDEFINITE, true);
            }
        });

    }

    private void getLocalSurveyQuestions(){
        try {
            String json = ResourceAccessHelper.getJsonData(this, "questions.json");
            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
            QuestionList questionList = ResponseTranslator.getSharedInstance().getSurveyQuestions(jsonObject);
            if (null != questions) {
                questions = questionList.questions;
                pagerAdapter.setData(questions);
                indicator.setViewPager(pager);
            }
            mProgress.hide();
        } catch (IOException e) {
            mProgress.hide();
            Log.e(SurveyActivity.class.getSimpleName() + ": IO error", "IO error: " + e.getMessage());
        }
    }

    private void getSurveyQuestions(){
        NetworkService networkService = new NetworkService();
        networkService.getSurveyData(new Callback<List<SurveyQuestion>>() {
            @Override
            public void onResponse(Call<List<SurveyQuestion>> call, Response<List<SurveyQuestion>> response) {
                questions = (ArrayList<SurveyQuestion>) response.body();
                pagerAdapter.setData(questions);
                indicator.setViewPager(pager);
                mProgress.hide();
            }

            @Override
            public void onFailure(Call<List<SurveyQuestion>> call, Throwable t) {
                getLocalSurveyQuestions();
            }
        });
    }

    private void showErrorCrouton(String message, int length, boolean showAction) {
        Snackbar snackbar = Snackbar.make(container, message, length);
        if (showAction) {
            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    submit();
                }
            });
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
        }
        snackbar.show();
    }

    private void showSuccessCrouton(String message) {
        Snackbar snackbar = Snackbar.make(container, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

}
