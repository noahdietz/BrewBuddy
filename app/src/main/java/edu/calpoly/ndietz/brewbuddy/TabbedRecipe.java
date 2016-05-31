package edu.calpoly.ndietz.brewbuddy;

/**
 * Created by ndietz on 5/12/16.
 */

import android.app.PendingIntent;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

// basis of class sourced from http://www.android4devs.com/2015/12/tab-layout-material-design-support.html
public class TabbedRecipe extends AppCompatActivity {


    public static final int TAB_COFFEE = 0;
    public static final int TAB_WATER = 1;
    public static final int TAB_TIME = 2;
    public static final int TAB_BREW = 3;

    private static final String TAG = "TabbedRecipe";
    static final String EXTRA_LIVE = "live";
    static final String EXTRA_RECIPE = "recipe";
    static final String STATE_RECIPE = "recipe";

    private Recipe m_recipe;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private boolean startForegroundService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_recipe);

        getSupportActionBar().hide();

        /*
        Assigning view variables to their respective view in xml
        by findViewByID method
         */
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        Bundle extras = getIntent().getExtras();

        if (savedInstanceState != null) {
            this.m_recipe = savedInstanceState.getParcelable(STATE_RECIPE);

            this.m_recipe.calc_totalTimeSeconds();
        } else {
            this.m_recipe = new Recipe();
            this.startForegroundService = true;

            this.m_recipe.setM_coarseness(1); // TODO: implement or delete coarsness

            if (extras != null) {
                this.m_recipe.setM_brewMethod(new BrewMethod(extras.getString("extra_brew_method")));
            }
        }

        /*
        Creating Adapter and setting that adapter to the viewPager
         */
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        /*
        TabLayout.newTab() method creates a tab view, Now a Tab view is not the view
        which is below the tabs, its the tab itself.
         */

        final TabLayout.Tab coffee = tabLayout.newTab();
        final TabLayout.Tab water = tabLayout.newTab();
        final TabLayout.Tab time = tabLayout.newTab();
        final TabLayout.Tab brew = tabLayout.newTab();

//       TODO: get correct icon densities
        coffee.setIcon(R.drawable.ic_coffee_white);
        water.setIcon(R.drawable.ic_water_white);
        time.setIcon(R.drawable.ic_timer_white);
        brew.setIcon(R.drawable.ic_flask_outline_white);

        /*
        Adding the tab view to our tablayout at appropriate positions
        As I want home at first position I am passing home and 0 as argument to
        the tablayout and like wise for other tabs as well
         */
        tabLayout.addTab(coffee, TAB_COFFEE);
        tabLayout.addTab(water, TAB_WATER);
        tabLayout.addTab(time, TAB_TIME);
        tabLayout.addTab(brew, TAB_BREW);

        /*
        Adding a onPageChangeListener to the viewPager
        1st we add the PageChangeListener and pass a TabLayoutPageChangeListener so that Tabs Selection
        changes when a viewpager page changes.
         */

        if (extras != null && extras.getBoolean(EXTRA_LIVE)) {
            this.m_recipe = extras.getParcelable(EXTRA_RECIPE);
            Log.d(TAG, "loading from extras\bRecipe: "+this.m_recipe.toString());

            viewPager.setCurrentItem(TAB_BREW);
            tabLayout.getTabAt(TAB_BREW).select();
        } else if (extras != null && extras.getBoolean(BrewHistoryList.EXTRA_FROM_HISTORY)) {
            this.m_recipe = extras.getParcelable(BrewHistoryList.EXTRA_HISTORICAL_RECIPE);
        }

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(STATE_RECIPE, this.m_recipe);

        super.onSaveInstanceState(savedInstanceState);
    }



    public void finishBrew() {
        if (BrewTimer.getInstance() != null) {
            BrewTimer.getInstance().stopSelf();
        }
        Log.d(TAG, "Finished brewing:\n"+this.m_recipe.toString());
        Intent i = new Intent(this, BrewReviewActivity.class);

        i.putExtra(EXTRA_RECIPE, this.m_recipe);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.startForegroundService = false;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (this.startForegroundService) {
            Intent mainIntent = new Intent(this, MainList.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Intent i = new Intent(this, TabbedRecipe.class);
            i.putExtra(EXTRA_RECIPE, this.m_recipe);
            i.putExtra(EXTRA_LIVE, true);

            PendingIntent pending =
                    PendingIntent.getActivities(this,
                            0,
                            new Intent[]{mainIntent, i},
                            PendingIntent.FLAG_ONE_SHOT);

            if (BrewTimer.getInstance() != null) {
                BrewTimer.getInstance().promoteToForeground(pending);
            }
        } else {
            if (BrewTimer.getInstance() != null) {
                BrewTimer.getInstance().stopSelf();
            }
        }
    }

    public void advanceTab() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    public void retreatTab() {
        if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        } else {
            finish();
        }
    }

    public void setRecipeCoffee(int coffee) {
        this.m_recipe.setM_gramsCoffee(coffee);
        Log.d(TAG, "Coffee: "+this.m_recipe.getM_gramsCoffee());
    }

    public boolean isRecipeCoffeeSet() {
        return this.m_recipe.getM_gramsCoffee() != -1;
    }

    public void setRecipeWater(int water) {
        this.m_recipe.setM_gramsWater(water);
        Log.d(TAG, "Water: "+this.m_recipe.getM_gramsWater());
    }

    public boolean isRecipeWaterSet() {
        return this.m_recipe.getM_gramsWater() != -1;
    }

    public void setRecipeMinutes(int minutes) {
        this.m_recipe.setM_timeMinutes(minutes);
        Log.d(TAG, "Minutes: "+this.m_recipe.getM_timeMinutes());
    }

    public boolean isRecipeMinutesSet() {
        return this.m_recipe.getM_timeMinutes() != -1;
    }

    public void setRecipeSeconds(int seconds) {
        this.m_recipe.setM_timeSeconds(seconds);
        Log.d(TAG, "Seconds: "+this.m_recipe.getM_timeSeconds());
    }

    public boolean isRecipeSecondsSet() {
        return this.m_recipe.getM_timeSeconds() != -1;
    }

    public int getRecipeTotalSeconds() {
        if (this.m_recipe.check_recipe_ready()) {
            this.m_recipe.calc_totalTimeSeconds();
            return this.m_recipe.getM_totalTimeSeconds();
        } else {
            Toast.makeText(this, "Recipe is not ready!", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    public boolean isRecipeReady() {
        Log.d(TAG, this.m_recipe.toString());
        return this.m_recipe.check_recipe_ready();
    }
}
