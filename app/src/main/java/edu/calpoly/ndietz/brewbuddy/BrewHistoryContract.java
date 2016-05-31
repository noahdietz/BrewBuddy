package edu.calpoly.ndietz.brewbuddy;

import android.provider.BaseColumns;

/**
 * Created by ndietz on 4/17/16.
 */
public final class BrewHistoryContract {
    public BrewHistoryContract() {}

    public static abstract class BrewEntry implements BaseColumns {
        public static final String TABLE_NAME = "brew";
        public static final String COLUMN_NAME_COFFEE = "coffee";
        public static final String COLUMN_NAME_GRIND = "grind";
        public static final String COLUMN_NAME_WATER = "water";
        public static final String COLUMN_NAME_TOTAL_SECONDS = "totalSeconds";
        public static final String COLUMN_NAME_BREW_METHOD = "brewMethod";
        public static final String COLUMN_NAME_ACIDITY = "acidity";
        public static final String COLUMN_NAME_BODY = "body";
        public static final String COLUMN_NAME_FLAVOR = "flavor";
        public static final String COLUMN_NAME_OVERALL = "overall";
    }
}
