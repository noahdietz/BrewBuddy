package edu.calpoly.ndietz.brewbuddy;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ndietz on 5/22/16.
 */
public class BrewHistoryAdapter extends BaseAdapter {
    private Context m_context;
    private List<BrewHistoryEntry> m_list;

    public BrewHistoryAdapter(Context context, List<BrewHistoryEntry> list) {
        this.m_context = context;
        this.m_list = list;
    }

    @Override
    public int getCount() {
        return this.m_list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.m_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BrewHistoryEntry temp;

        if (convertView == null) {
            temp = this.m_list.get(position);
            BrewHistoryEntryView bv = new BrewHistoryEntryView(this.m_context, temp);

            return bv;

        } else {
            temp = this.m_list.get(position);
            ((BrewHistoryEntryView) convertView).setM_entry(temp);

            return convertView;
        }
    }

    public void setList(ArrayList list) {
        this.m_list = list;
    }
}
