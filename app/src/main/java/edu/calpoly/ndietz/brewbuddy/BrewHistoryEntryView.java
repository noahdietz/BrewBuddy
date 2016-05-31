package edu.calpoly.ndietz.brewbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by ndietz on 5/22/16.
 */
public class BrewHistoryEntryView extends LinearLayout {
    private BrewHistoryEntry m_entry;
    private TextView m_vwMethodName;
    private TextView m_vwOverallRating;
    private TextView m_vwGramsCoffee;
    private TextView m_vwGramsWater;
    private TextView m_vwTime;

    public BrewHistoryEntryView(Context context, BrewHistoryEntry entry) {
        super(context);

        this.m_entry = entry;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.brew_history_entry, this, true);

        // get view items
        this.m_vwMethodName = (TextView)findViewById(R.id.brew_history_method);
        this.m_vwOverallRating = (TextView)findViewById(R.id.brew_history_rating_overall);
        this.m_vwGramsCoffee = (TextView)findViewById(R.id.history_grams_coffee);
        this.m_vwGramsWater = (TextView)findViewById(R.id.history_grams_water);
        this.m_vwTime = (TextView)findViewById(R.id.history_time);

        this.m_vwMethodName.setText(this.m_entry.getM_recipe().getM_brewMethod().getM_method_name());
        this.m_vwOverallRating.setText("Overall: "+this.m_entry.getM_review().getM_overall());
        this.m_vwGramsCoffee.setText(this.m_entry.getM_recipe().getM_gramsCoffee()+"g");
        this.m_vwGramsWater.setText(this.m_entry.getM_recipe().getM_gramsWater()+"g");
        this.m_vwTime.setText(this.m_entry.getM_recipe().getM_timeMinutes()+":"+this.m_entry.getM_recipe().getM_timeSeconds());
    }

    public void setM_entry(BrewHistoryEntry m_entry) {
        this.m_entry = m_entry;

        // update view values with new entry
        this.m_vwMethodName.setText(this.m_entry.getM_recipe().getM_brewMethod().getM_method_name());
        this.m_vwOverallRating.setText("Overall: "+this.m_entry.getM_review().getM_overall());
        this.m_vwGramsCoffee.setText(this.m_entry.getM_recipe().getM_gramsCoffee()+"g");
        this.m_vwGramsWater.setText(this.m_entry.getM_recipe().getM_gramsWater()+"g");
        this.m_vwTime.setText(this.m_entry.getM_recipe().getM_timeMinutes()+":"+this.m_entry.getM_recipe().getM_timeSeconds());

        this.requestLayout();
    }

    public BrewHistoryEntry getM_entry() {
        return m_entry;
    }
}
