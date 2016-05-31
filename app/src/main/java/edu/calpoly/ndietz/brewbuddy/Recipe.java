package edu.calpoly.ndietz.brewbuddy;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.sql.Time;

/**
 * Created by ndietz on 4/17/16.
 */
public class Recipe implements Parcelable {
    private int m_gramsCoffee;
    private int m_gramsWater;
    private int m_timeMinutes;
    private int m_timeSeconds;
    private int m_totalTimeSeconds;
    private int m_coarseness;
    private BrewMethod m_brewMethod;

    public Recipe(int coffee, int coarse, int water, int minutes, int seconds, BrewMethod method) {
        this.m_gramsCoffee = coffee;
        this.m_gramsWater = water;
        this.m_timeMinutes = minutes;
        this.m_timeSeconds = seconds;
        this.m_brewMethod = method;
        this.m_coarseness = coarse;

        this.m_totalTimeSeconds = this.m_timeSeconds + (60 * this.m_timeMinutes);
    }

    public Recipe() {
        this.m_gramsCoffee = -1;
        this.m_gramsWater = -1;
        this.m_timeMinutes = -1;
        this.m_timeSeconds = -1;
        this.m_coarseness = -1;
        this.m_brewMethod = new BrewMethod();

        this.m_totalTimeSeconds = -1;
    }

    public BrewMethod getM_brewMethod() {
        return m_brewMethod;
    }

    public int getM_gramsCoffee() {
        return m_gramsCoffee;
    }

    public int getM_gramsWater() {
        return m_gramsWater;
    }

    public int getM_timeMinutes() {
        return m_timeMinutes;
    }

    public int getM_timeSeconds() {
        return m_timeSeconds;
    }

    public int getM_totalTimeSeconds() {
        return m_totalTimeSeconds;
    }

    public int getM_coarseness() {
        return m_coarseness;
    }

    public void setM_coarseness(int m_coarseness) {
        this.m_coarseness = m_coarseness;
    }

    public void setM_brewMethod(BrewMethod m_brewMethod) {
        this.m_brewMethod = m_brewMethod;
    }

    public void setM_gramsCoffee(int m_gramsCoffee) {
        this.m_gramsCoffee = m_gramsCoffee;
    }

    public void setM_gramsWater(int m_gramsWater) {
        this.m_gramsWater = m_gramsWater;
    }

    public void setM_timeMinutes(int m_timeMinutes) {
        this.m_timeMinutes = m_timeMinutes;
    }

    public void setM_timeSeconds(int m_timeSeconds) {
        this.m_timeSeconds = m_timeSeconds;
    }

    public void setM_totalTimeSeconds(int m_totalTimeSeconds) {
        this.m_totalTimeSeconds = m_totalTimeSeconds;
    }

    public void calc_totalTimeSeconds() {
        this.m_totalTimeSeconds = this.m_timeSeconds + (60 * this.m_timeMinutes);
    }

    public boolean check_recipe_ready() {
        return m_timeSeconds != -1 && m_timeMinutes != -1 && m_gramsCoffee != -1 &&
                m_gramsWater != -1 && m_coarseness != -1 && !m_brewMethod.toString().equals("");
    }

    public void setTimesFromTotal() {
        this.m_timeMinutes = new Time((long)this.m_totalTimeSeconds*1000).getMinutes();
        this.m_timeSeconds = this.m_totalTimeSeconds%60;
    }

    @Override
    public String toString() {
        return "coffee: " + this.m_gramsCoffee + " water: " + this.m_gramsWater + " minutes: " +
                this.m_timeMinutes + " seconds: " + this.m_timeSeconds + "  method: " + this.m_brewMethod.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Recipe) {
            Recipe tmp = (Recipe) obj;

            return this.m_brewMethod.equals(tmp.getM_brewMethod()) &&
                    this.m_gramsCoffee == tmp.getM_gramsCoffee() &&
                    this.m_gramsWater == tmp.getM_gramsWater() &&
                    this.m_totalTimeSeconds == this.getM_totalTimeSeconds();
        } else {
            return false;
        }
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new
        Parcelable.Creator<Recipe>() {
            public Recipe createFromParcel(Parcel in) {
                return new Recipe(in);
            }

            public Recipe[] newArray(int size) {
                return new Recipe[size];
            }
        };

    private Recipe(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.m_gramsCoffee);
        dest.writeInt(this.m_coarseness);
        dest.writeInt(this.m_gramsWater);
        dest.writeInt(this.m_timeMinutes);
        dest.writeInt(this.m_timeSeconds);
        dest.writeString(this.m_brewMethod.toString());
    }

    public void readFromParcel(Parcel in) {
        this.m_gramsCoffee = in.readInt();
        this.m_coarseness = in.readInt();
        this.m_gramsWater = in.readInt();
        this.m_timeMinutes = in.readInt();
        this.m_timeSeconds = in.readInt();
        this.m_brewMethod = new BrewMethod(in.readString());
        calc_totalTimeSeconds();
    }
}
