package edu.calpoly.ndietz.brewbuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.channguyen.rsv.RangeSliderView;

public class BrewReviewActivity extends AppCompatActivity {

    private static final String TAG = "BrewReviewActivity";
    static final String STATE_REVIEW = "review";
    static final String STATE_RECIPE = "recipe";

    private RangeSliderView m_vwAcidityRating;
    private RangeSliderView m_vwBodyRating;
    private RangeSliderView m_vwFlavorRating;
    private RangeSliderView m_vwOverallRating;

    private Review m_review;
    private Recipe m_recipe;
    private Button m_vwSubmit;
    private BrewDbHelper m_DbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew_review);

        this.m_vwAcidityRating = (RangeSliderView) this.findViewById(R.id.review_acidity);
        this.m_vwBodyRating = (RangeSliderView) this.findViewById(R.id.review_body);
        this.m_vwFlavorRating = (RangeSliderView) this.findViewById(R.id.review_flavor);
        this.m_vwOverallRating = (RangeSliderView) this.findViewById(R.id.review_overall);
        this.m_vwSubmit = (Button) this.findViewById(R.id.review_submit);

        if (savedInstanceState != null) {
            this.m_review = savedInstanceState.getParcelable(STATE_REVIEW);
            this.m_recipe = savedInstanceState.getParcelable(STATE_RECIPE);
            Log.d(TAG, this.m_recipe.toString());
        } else {
            this.m_review = new Review();

            Bundle extras = getIntent().getExtras();
            if (extras != null && extras.containsKey(TabbedRecipe.EXTRA_RECIPE)) {
                this.m_recipe = extras.getParcelable(TabbedRecipe.EXTRA_RECIPE);
            } else {
                this.m_recipe = new Recipe();
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.m_DbHelper = BrewDbHelper.getInstance(this);

        initListeners();
    }

    private void initListeners() {
        this.m_vwAcidityRating.setOnSlideListener(new RangeSliderView.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                m_review.setM_acidity(index);
            }
        });

        this.m_vwBodyRating.setOnSlideListener(new RangeSliderView.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                m_review.setM_body(index);
            }
        });

        this.m_vwFlavorRating.setOnSlideListener(new RangeSliderView.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                m_review.setM_flavor(index);
            }
        });

        this.m_vwOverallRating.setOnSlideListener(new RangeSliderView.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                m_review.setM_overall(index);
            }
        });

        this.m_vwSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, m_review.toString());

                saveBrew();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(STATE_REVIEW, this.m_review);
        savedInstanceState.putParcelable(STATE_RECIPE, this.m_recipe);

        super.onSaveInstanceState(savedInstanceState);
    }

    public void saveBrew() {
        m_DbHelper.addBrewEntry(this.m_recipe, this.m_review);

        finish();
    }
}
