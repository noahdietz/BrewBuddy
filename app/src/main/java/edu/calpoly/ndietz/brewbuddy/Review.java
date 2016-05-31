package edu.calpoly.ndietz.brewbuddy;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ndietz on 5/18/16.
 */
public class Review implements Parcelable {
    private int m_acidity;
    private int m_body;
    private int m_flavor;
    private int m_overall;

    public Review() {
        this.m_acidity = -1;
        this.m_body = -1;
        this.m_flavor = -1;
        this.m_overall = -1;
    }

    public Review(int acidity, int body, int flavor, int overall) {
        this.m_acidity = acidity;
        this.m_body = body;
        this.m_flavor = flavor;
        this.m_overall = flavor;
    }

    public int getM_overall() {
        return m_overall;
    }

    public void setM_overall(int m_overall) {
        this.m_overall = m_overall;
    }

    public int getM_flavor() {
        return m_flavor;
    }

    public void setM_flavor(int m_flavor) {
        this.m_flavor = m_flavor;
    }

    public int getM_body() {
        return m_body;
    }

    public void setM_body(int m_body) {
        this.m_body = m_body;
    }

    public int getM_acidity() {
        return m_acidity;
    }

    public void setM_acidity(int m_acidity) {
        this.m_acidity = m_acidity;
    }

    public static final Parcelable.Creator<Review> CREATOR = new
            Parcelable.Creator<Review>() {
                public Review createFromParcel(Parcel in) {
                    return new Review(in);
                }

                public Review[] newArray(int size) {
                    return new Review[size];
                }
            };

    private Review(Parcel in) {
        readFromParcel(in);
    }

    public String toString() {
        return "Acidity: " + m_acidity + " Body: " + m_body + " Flavor: " + m_flavor +
                " Overall: " + m_overall;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.m_acidity);
        dest.writeInt(this.m_body);
        dest.writeInt(this.m_flavor);
        dest.writeInt(this.m_overall);
    }

    public void readFromParcel(Parcel in) {
        this.m_acidity = in.readInt();
        this.m_body = in.readInt();
        this.m_flavor = in.readInt();
        this.m_overall = in.readInt();
    }
}
