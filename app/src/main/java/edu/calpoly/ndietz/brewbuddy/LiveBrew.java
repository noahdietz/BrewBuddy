package edu.calpoly.ndietz.brewbuddy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LiveBrew extends AppCompatActivity {

    private TextView m_vwtime;
    private String m_totalTime;
    private BroadcastReceiver br;
    private Button m_vwFinishButton;
    private BrewDbHelper m_DbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_brew);

        this.m_DbHelper = BrewDbHelper.getInstance(this);

        initLayout();

        initListeners();

        launchBrewTimer();
    }

    private void initLayout() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.m_totalTime = extras.getString("extra_total_time");
            this.m_vwtime = (TextView)findViewById(R.id.live_brew_time);

            this.m_vwtime.setText(this.m_totalTime);
        }

        this.m_vwFinishButton = (Button)findViewById(R.id.live_brew_finish_button);
    }

    private void initListeners() {
        this.br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateTimeView(intent);
            }
        };

        this.m_vwFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_DbHelper.dumpContentsToLog();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(BrewTimer.COUNTDOWN_BR));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }

    @Override
    public void onStop() {
        try {
            unregisterReceiver(br);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, BrewTimer.class));
        super.onDestroy();
    }

    private void launchBrewTimer() {
        Intent i = new Intent(this, BrewTimer.class);
        i.putExtra("extra_brew_time", this.m_totalTime);
        startService(i);
    }

    private void updateTimeView(Intent intent) {
        if (intent.getExtras() != null) {
            String time_left = intent.getExtras().getString("extra_time_left");
            this.m_vwtime.setText(time_left);

            if (time_left.equals("0\n Drink up!")) {
                this.m_vwFinishButton.setEnabled(true);
            }
        }
    }
}
