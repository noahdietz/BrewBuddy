package edu.calpoly.ndietz.brewbuddy;

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
public class TabFragmentCoffee extends Fragment {

    private EditText m_vwGramsCoffee;
    private static final String FRAG_COFFEE_TAG = "TabFragCoffee";
    private ImageButton m_vwAdvanceButton;
    private ImageButton m_vwRetreatButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_frag_coffee, container, false);
        this.m_vwGramsCoffee = (EditText) rootView.findViewById(R.id.recipe_grams_coffee);
        this.m_vwAdvanceButton = (ImageButton) rootView.findViewById(R.id.coffee_advance_button);
        this.m_vwRetreatButton = (ImageButton) rootView.findViewById(R.id.coffee_cancel_button);

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            if (extras.getBoolean(TabbedRecipe.EXTRA_LIVE)) {
                Recipe temp = extras.getParcelable(TabbedRecipe.EXTRA_RECIPE);
                this.m_vwGramsCoffee.setText(temp.getM_gramsCoffee()+"");
            } else if (extras.getBoolean(BrewHistoryList.EXTRA_FROM_HISTORY)) {
                Recipe temp = extras.getParcelable(BrewHistoryList.EXTRA_HISTORICAL_RECIPE);
                this.m_vwGramsCoffee.setText(temp.getM_gramsCoffee()+"");
            }
        }

        initListeners();

        return rootView;
    }

    private void initListeners() {
        this.m_vwGramsCoffee.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) &&
                        m_vwGramsCoffee.getText().length() != 0 &&
                        event.getAction() == KeyEvent.ACTION_UP) {

                    ((TabbedRecipe)getActivity()).setRecipeCoffee(Integer.parseInt(m_vwGramsCoffee.getText().toString()));
                }

                return false;
            }
        });

        this.m_vwAdvanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabbedRecipe act = (TabbedRecipe) getActivity();
                if (act.isRecipeCoffeeSet() || !m_vwGramsCoffee.getText().toString().equals("")) {
                    Log.d(FRAG_COFFEE_TAG, "Advance button pressed");
                    act.setRecipeCoffee(Integer.parseInt(m_vwGramsCoffee.getText().toString()));
                    act.advanceTab();
                } else {
                    Toast.makeText(getContext(), "Grams of coffee is not set", Toast.LENGTH_SHORT).show();
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
