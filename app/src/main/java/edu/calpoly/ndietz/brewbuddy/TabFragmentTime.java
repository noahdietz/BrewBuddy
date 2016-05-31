package edu.calpoly.ndietz.brewbuddy;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by ndietz on 5/15/16.
 */
public class TabFragmentTime extends Fragment {

    private static final String FRAG_TIME_TAG = "TabFragTime";

    private EditText m_vwTimeMinutes;
    private EditText m_vwTimeSeconds;
    private ImageButton m_vwAdvanceButton;
    private ImageButton m_vwRetreatButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_frag_time, container, false);
        this.m_vwTimeMinutes = (EditText) rootView.findViewById(R.id.recipe_time_minutes);
        this.m_vwTimeSeconds = (EditText) rootView.findViewById(R.id.recipe_time_seconds);
        this.m_vwAdvanceButton = (ImageButton) rootView.findViewById(R.id.time_advance_button);
        this.m_vwRetreatButton = (ImageButton) rootView.findViewById(R.id.time_cancel_button);

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            if (extras.getBoolean(TabbedRecipe.EXTRA_LIVE)) {
                Recipe temp = extras.getParcelable(TabbedRecipe.EXTRA_RECIPE);
                this.m_vwTimeMinutes.setText(temp.getM_timeMinutes()+"");
                this.m_vwTimeSeconds.setText(temp.getM_timeSeconds()+"");
            } else if (extras.getBoolean(BrewHistoryList.EXTRA_FROM_HISTORY)) {
                Recipe temp = extras.getParcelable(BrewHistoryList.EXTRA_HISTORICAL_RECIPE);
                this.m_vwTimeMinutes.setText(temp.getM_timeMinutes()+"");
                this.m_vwTimeSeconds.setText(temp.getM_timeSeconds()+"");
            }
        }

        initListeners();

        return rootView;
    }

    private void initListeners() {
        this.m_vwTimeMinutes.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) &&
                        m_vwTimeMinutes.getText().length() != 0 &&
                        event.getAction() == KeyEvent.ACTION_UP) {

                    Log.d(FRAG_TIME_TAG, "minutes: "+m_vwTimeMinutes.getText().toString());
                    ((TabbedRecipe)getActivity()).setRecipeMinutes(Integer.parseInt(m_vwTimeMinutes.getText().toString()));
                }

                return false;
            }
        });

        this.m_vwTimeSeconds.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) &&
                        m_vwTimeSeconds.getText().length() != 0 &&
                        event.getAction() == KeyEvent.ACTION_UP) {

                    ((TabbedRecipe)getActivity()).setRecipeSeconds(Integer.parseInt(m_vwTimeSeconds.getText().toString()));

                }

                return false;
            }
        });

        this.m_vwAdvanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabbedRecipe act = (TabbedRecipe) getActivity();
                if ((act.isRecipeMinutesSet() && act.isRecipeSecondsSet()) ||
                        (!m_vwTimeMinutes.getText().toString().equals("") && !m_vwTimeSeconds.getText().toString().equals(""))) {
                    Log.d(FRAG_TIME_TAG, "Advance button pressed");

                    act.setRecipeMinutes(Integer.parseInt(m_vwTimeMinutes.getText().toString()));
                    act.setRecipeSeconds(Integer.parseInt(m_vwTimeSeconds.getText().toString()));
                    act.advanceTab();
                } else {
                    Toast.makeText(getContext(), "Time is not set", Toast.LENGTH_SHORT).show();
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
