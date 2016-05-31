package edu.calpoly.ndietz.brewbuddy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by ndietz on 4/17/16.
 */
public class BrewListAdapter extends RecyclerView.Adapter<BrewMethodViewHolder> {

    private Context m_context;
    private List<BrewMethod> m_brewList;

    public BrewListAdapter(Context context, List<BrewMethod> brewList) {
        this.m_context = context;
        this.m_brewList = brewList;
    }

    @Override
    public BrewMethodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BrewMethodViewHolder(new BrewMethodView(m_context));
    }

    @Override
    public void onBindViewHolder(BrewMethodViewHolder holder, int position) {
        holder.bind(this.m_brewList.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.m_brewList.size();
    }
}
