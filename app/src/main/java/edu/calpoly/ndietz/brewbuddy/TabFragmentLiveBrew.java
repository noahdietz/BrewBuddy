package edu.calpoly.ndietz.brewbuddy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import me.itangqi.waveloadingview.WaveLoadingView;

/**
 * Created by ndietz on 5/15/16.
 */
public class TabFragmentLiveBrew extends Fragment {

    private static String STATE_START_BUTTON = "start";
    private static String STATE_FINISH_BUTTON = "finish";
    private static String STATE_TOTAL_TIME = "total_time";
    private static String STATE_REMAINING_TIME = "remaining_time";

    private Button m_vwStartButton;
    private Button m_vwFinishButton;
    private int m_totalTime;
    private BroadcastReceiver br;
    private WaveLoadingView m_vwWave;

    public static final String TIME_LEFT_EXTRA = "extra_time_left";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_frag_live, container, false);

        this.m_vwStartButton = (Button) rootView.findViewById(R.id.live_brew_start_button);
        this.m_vwFinishButton = (Button) rootView.findViewById(R.id.live_brew_finish_button);
        this.m_vwWave = (WaveLoadingView) rootView.findViewById(R.id.waveLoadingView);

        if (savedInstanceState != null) {
            this.m_vwStartButton.setEnabled(savedInstanceState.getBoolean(STATE_START_BUTTON));
            this.m_vwFinishButton.setEnabled(savedInstanceState.getBoolean(STATE_FINISH_BUTTON));
            this.m_totalTime = savedInstanceState.getInt(STATE_TOTAL_TIME);
            this.m_vwWave.setProgressValue(savedInstanceState.getInt(STATE_REMAINING_TIME));
        } else {
            this.m_totalTime = 0;
        }

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            if (extras.getBoolean(TabbedRecipe.EXTRA_LIVE)) {
                this.m_vwStartButton.setEnabled(false);

                if (BrewTimer.getInstance() != null && BrewTimer.getInstance().checkIfDone()) {
                    this.m_vwWave.setProgressValue(100);
                    this.m_vwWave.setCenterTitle("Drink up!");
                    this.m_vwFinishButton.setEnabled(true);
                } else {
                    this.m_totalTime = ((TabbedRecipe)getActivity()).getRecipeTotalSeconds();
                }
            } else if (extras.getBoolean(BrewHistoryList.EXTRA_FROM_HISTORY)) {
                Recipe temp = extras.getParcelable(BrewHistoryList.EXTRA_HISTORICAL_RECIPE);
                this.m_totalTime = temp.getM_totalTimeSeconds();
            }
        }

        initListeners();

        return rootView;
    }

    private void initListeners() {
        this.m_vwStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                kick off intent service
                if (!((TabbedRecipe)getActivity()).isRecipeReady()) {
                    Toast.makeText(getContext(), "Cannot start without a recipe!", Toast.LENGTH_SHORT).show();
                } else {
                    m_totalTime = ((TabbedRecipe)getActivity()).getRecipeTotalSeconds();

                    launchBrewTimer();

//                turn off start button
                    m_vwStartButton.setEnabled(false);
                }
            }
        });

        this.m_vwFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TabbedRecipe)getActivity()).finishBrew();
            }
        });

        this.br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateTimeView(intent);
            }
        };
    }

    private void updateTimeView(Intent intent) {
        if (intent.getExtras() != null && intent.hasExtra(TIME_LEFT_EXTRA)) {
            int time_left = (int)intent.getExtras().getLong(TIME_LEFT_EXTRA);

            if (time_left == 0) {
                this.m_vwFinishButton.setEnabled(true);
                this.m_vwWave.setProgressValue(100);
                this.m_vwWave.setCenterTitle("Drink up!");
            } else {
                this.m_vwWave.setProgressValue(calcProgress(time_left));
            }
        }
    }

    private int calcProgress(int time_left) {
        int progress;

        progress = (int) (100-(100*(time_left/(double)m_totalTime)));

        return progress;
    }

    private void launchBrewTimer() {
        Intent i = new Intent(getContext(), BrewTimer.class);
        i.putExtra("extra_brew_time", this.m_totalTime);
        getActivity().startService(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(br, new IntentFilter(BrewTimer.COUNTDOWN_BR));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(br);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(STATE_START_BUTTON, this.m_vwStartButton.isEnabled());
        savedInstanceState.putBoolean(STATE_FINISH_BUTTON, this.m_vwFinishButton.isEnabled());
        savedInstanceState.putInt(STATE_REMAINING_TIME, this.m_vwWave.getProgressValue());
        savedInstanceState.putInt(STATE_TOTAL_TIME, this.m_totalTime);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStop() {
        try {
            getActivity().unregisterReceiver(br);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }
}



