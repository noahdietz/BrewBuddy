package edu.calpoly.ndietz.brewbuddy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BrewHistoryList extends AppCompatActivity {

    private static final String SAVED_LIST = "historyList";
    private static final String SAVED_FILTER = "filter";
    private static final String SAVED_FILTERED_LIST = "filteredList";
    public static final String EXTRA_FILTER = "extra_filter";
    public static String EXTRA_HISTORICAL_RECIPE = "historicalRecipe";
    public static String EXTRA_FROM_HISTORY = "fromHistory";

    public static int FILTER_SHOW_ALL = 0;
    public static int FILTER_CHEMEX = FILTER_SHOW_ALL + 1;
    public static int FILTER_V60 = FILTER_SHOW_ALL + 2;
    public static int FILTER_BEEHOUSE = FILTER_SHOW_ALL + 3;
    public static int FILTER_KALITA = FILTER_SHOW_ALL + 4;
    public static int FILTER_MOKAPOT = FILTER_SHOW_ALL + 5;
    public static int FILTER_FRENCHPRESS = FILTER_SHOW_ALL + 6;
    public static int FILTER_AEROPRESS = FILTER_SHOW_ALL + 7;
    private static String[] filter_names = {"all",
                                            "Chemex",
                                            "Hario V60",
                                            "Bee House",
                                            "Kalita Wave",
                                            "Moka Pot",
                                            "French Press",
                                            "Aeropress"};

    protected ListView m_vwBrewHistoryListView;
    protected BrewHistoryAdapter m_historyAdapter;
    protected ArrayList<BrewHistoryEntry> m_historyList;
    protected ArrayList<BrewHistoryEntry> m_filteredHistory;
    private int m_filter;
    private  BrewDbHelper dbHelper;
    private Context m_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew_history_list);
        this.m_context = this;

        this.m_vwBrewHistoryListView = (ListView)findViewById(R.id.history_list);

        this.dbHelper = BrewDbHelper.getInstance(this);

        if (savedInstanceState != null) {
            this.m_historyList = savedInstanceState.getParcelableArrayList(SAVED_LIST);
            this.m_filteredHistory = savedInstanceState.getParcelableArrayList(SAVED_FILTERED_LIST);
            this.m_filter = savedInstanceState.getInt(SAVED_FILTER);

            Log.d("brewhistory", "filter: " + this.m_filter + "filtered size: "+m_filteredHistory.size() + " all: "+m_historyList.size());
        } else {
            Bundle extras = getIntent().getExtras();
            if (extras != null && extras.getInt(EXTRA_FILTER, -1) != -1) {
                this.m_filter = extras.getInt(EXTRA_FILTER);
            } else {
                this.m_filter = FILTER_SHOW_ALL;
            }

            this.m_historyList = dbHelper.getHistory();
            this.m_filteredHistory = new ArrayList<>();

            if (this.m_filter == FILTER_SHOW_ALL) {
                this.m_filteredHistory.addAll(this.m_historyList);
            } else {
                filterBrewHistory();
            }
        }

        this.m_historyAdapter = new BrewHistoryAdapter(this, this.m_filteredHistory);
        this.m_vwBrewHistoryListView.setAdapter(this.m_historyAdapter);
        this.m_historyAdapter.notifyDataSetChanged();

        this.m_vwBrewHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(m_context, TabbedRecipe.class);

                if (m_filter == FILTER_SHOW_ALL) {
                    i.putExtra(EXTRA_HISTORICAL_RECIPE, m_historyList.get(position).getM_recipe());
                } else {
                    i.putExtra(EXTRA_HISTORICAL_RECIPE, m_filteredHistory.get(position).getM_recipe());
                }
                i.putExtra(EXTRA_FROM_HISTORY, true);
                startActivity(i);
            }
        });
    }

    private void filterBrewHistory() {
        this.m_filteredHistory.clear();

        if (this.m_filter == FILTER_SHOW_ALL) {
            this.m_filteredHistory.addAll(this.m_historyList);
        } else {
            for (BrewHistoryEntry b :
                    this.m_historyList) {
                if (b.getM_recipe().getM_brewMethod().toString().equals(filter_names[this.m_filter])) {
                    this.m_filteredHistory.add(b);
                }
            }
        }

        Log.d("brewhistory", "filtered size: "+m_filteredHistory.size());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.filter_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submenu_chemex:
                this.m_filter = FILTER_CHEMEX;
                filterBrewHistory();
                this.m_historyAdapter.setList(this.m_filteredHistory);
                this.m_historyAdapter.notifyDataSetChanged();
                break;
            case R.id.submenu_v60:
                this.m_filter = FILTER_V60;
                filterBrewHistory();
                this.m_historyAdapter.setList(this.m_filteredHistory);
                this.m_historyAdapter.notifyDataSetChanged();
                break;
            case R.id.submenu_beehouse:
                this.m_filter = FILTER_BEEHOUSE;
                filterBrewHistory();
                this.m_historyAdapter.setList(this.m_filteredHistory);
                this.m_historyAdapter.notifyDataSetChanged();
                break;
            case R.id.submenu_kalita:
                this.m_filter = FILTER_KALITA;
                filterBrewHistory();
                this.m_historyAdapter.setList(this.m_filteredHistory);
                this.m_historyAdapter.notifyDataSetChanged();
                break;
            case R.id.submenu_mokapot:
                this.m_filter = FILTER_MOKAPOT;
                filterBrewHistory();
                this.m_historyAdapter.setList(this.m_filteredHistory);
                this.m_historyAdapter.notifyDataSetChanged();
                break;
            case R.id.submenu_frenchpress:
                this.m_filter = FILTER_FRENCHPRESS;
                filterBrewHistory();
                this.m_historyAdapter.setList(this.m_filteredHistory);
                this.m_historyAdapter.notifyDataSetChanged();
                break;
            case R.id.submenu_aeropress:
                this.m_filter = FILTER_AEROPRESS;
                filterBrewHistory();
                this.m_historyAdapter.setList(this.m_filteredHistory);
                this.m_historyAdapter.notifyDataSetChanged();
                break;
            case R.id.submenu_all:
                this.m_filter = FILTER_SHOW_ALL;
                filterBrewHistory();
                this.m_historyAdapter.setList(this.m_filteredHistory);
                this.m_historyAdapter.notifyDataSetChanged();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(SAVED_LIST , this.m_historyList);
        outState.putParcelableArrayList(SAVED_FILTERED_LIST, this.m_filteredHistory);
        outState.putInt(SAVED_FILTER, this.m_filter);

        super.onSaveInstanceState(outState);
    }
}
