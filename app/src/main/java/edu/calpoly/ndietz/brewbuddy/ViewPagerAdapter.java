package edu.calpoly.ndietz.brewbuddy;

/**
 * Created by ndietz on 5/12/16.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case TabbedRecipe.TAB_COFFEE:
                return new TabFragmentCoffee();
            case TabbedRecipe.TAB_WATER:
                return new TabFragmentWater();
            case TabbedRecipe.TAB_TIME:
                return new TabFragmentTime();
            case TabbedRecipe.TAB_BREW:
                return new TabFragmentLiveBrew();
            default:
                return new TabFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
