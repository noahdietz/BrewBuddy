package edu.calpoly.ndietz.brewbuddy;

import android.content.Intent;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by ndietz on 5/14/16.
 */
public class TabFragmentWater extends Fragment {

    private EditText m_vwGramsWater;
    private static final String FRAG_WATER_TAG = "TabFragWater";
    private ImageButton m_vwWaterAdvanceButton;
    private ImageButton m_vwRetreatButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_frag_water, container, false);
        this.m_vwGramsWater = (EditText) rootView.findViewById(R.id.recipe_grams_water);
        this.m_vwWaterAdvanceButton = (ImageButton) rootView.findViewById(R.id.water_advance_button);
        this.m_vwRetreatButton = (ImageButton) rootView.findViewById(R.id.water_cancel_button);

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            if (extras.getBoolean(TabbedRecipe.EXTRA_LIVE)) {
                Recipe temp = extras.getParcelable(TabbedRecipe.EXTRA_RECIPE);
                this.m_vwGramsWater.setText(temp.getM_gramsWater()+"");
            } else if (extras.getBoolean(BrewHistoryList.EXTRA_FROM_HISTORY)) {
                Recipe temp = extras.getParcelable(BrewHistoryList.EXTRA_HISTORICAL_RECIPE);
                this.m_vwGramsWater.setText(temp.getM_gramsWater()+"");
            }
        }

        initListeners();

        return rootView;
    }

    private void initListeners() {
        this.m_vwGramsWater.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) &&
                        m_vwGramsWater.getText().length() != 0 &&
                        event.getAction() == KeyEvent.ACTION_UP) {

                    ((TabbedRecipe)getActivity()).setRecipeWater(Integer.parseInt(m_vwGramsWater.getText().toString()));
                }

                return false;
            }
        });

        this.m_vwWaterAdvanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabbedRecipe act = (TabbedRecipe) getActivity();
                if (act.isRecipeWaterSet() || !m_vwGramsWater.getText().toString().equals("")) {
                    Log.d(FRAG_WATER_TAG, "Advance button pressed");
                    act.setRecipeWater(Integer.parseInt(m_vwGramsWater.getText().toString()));
                    act.advanceTab();
                } else {
                    Toast.makeText(getContext(), "Grams of water is not set", Toast.LENGTH_SHORT).show();
                }

            }
        });

        this.m_vwRetreatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TabbedRecipe)getActivity()).retreatTab();
            }
        });
    }
}
