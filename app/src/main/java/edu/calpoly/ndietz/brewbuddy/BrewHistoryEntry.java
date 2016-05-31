package edu.calpoly.ndietz.brewbuddy;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ndietz on 5/22/16.
 */
public class BrewHistoryEntry implements Parcelable {
    private Recipe m_recipe;
    private Review m_review;

    public BrewHistoryEntry(Recipe recipe, Review review) {
        this.m_recipe = recipe;
        this.m_review = review;
    }

    public Recipe getM_recipe() {
        return m_recipe;
    }

    public void setM_recipe(Recipe m_recipe) {
        this.m_recipe = m_recipe;
    }

    public Review getM_review() {
        return m_review;
    }

    public void setM_review(Review m_review) {
        this.m_review = m_review;
    }

    public static final Parcelable.Creator<BrewHistoryEntry> CREATOR = new
            Parcelable.Creator<BrewHistoryEntry>() {
                public BrewHistoryEntry createFromParcel(Parcel in) {
                    return new BrewHistoryEntry(in);
                }

                public BrewHistoryEntry[] newArray(int size) {
                    return new BrewHistoryEntry[size];
                }
            };

    private BrewHistoryEntry(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.m_recipe, 0);
        dest.writeParcelable(this.m_review, 0);
    }

    private void readFromParcel(Parcel in) {
        this.m_recipe = in.readParcelable(null);
        this.m_review = in.readParcelable(null);
    }
}
