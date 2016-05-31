package edu.calpoly.ndietz.brewbuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainList extends AppCompatActivity {

    private static final String MAIN_LIST_TAG = "MainList Activity";

    protected RecyclerView m_vwBrewMethodList;
    protected BrewListAdapter m_brewAdapter;
    protected ArrayList<BrewMethod> m_arrMethodList;
    private BrewDbHelper mDbHelper;
    private Menu m_vwMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        mDbHelper = BrewDbHelper.getInstance(this);

        this.m_arrMethodList = new ArrayList<>();
        this.m_brewAdapter = new BrewListAdapter(this, this.m_arrMethodList);

        initLayout();

        BrewMethod bm;
        String[] names = getResources().getStringArray(R.array.method_list);
        for (int i = 0; i < names.length; i++) {
            bm = new BrewMethod(names[i]);

            addBrewMethod(bm);
        }

        this.m_brewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.mainmenu, menu);

        this.m_vwMenu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_history) {
            Log.d(MAIN_LIST_TAG, "History button pressed");

            Intent i = new Intent(this, BrewHistoryList.class);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addBrewMethod(BrewMethod bm) {
        this.m_arrMethodList.add(bm);
    }

    protected void initLayout() {
        this.setContentView(R.layout.activity_main_list);

        this.m_vwBrewMethodList = (RecyclerView) this.findViewById(R.id.main_list);
        this.m_vwBrewMethodList.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        this.m_vwBrewMethodList.setAdapter(this.m_brewAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int index = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {

                    Intent intent = new Intent(((BrewMethodViewHolder)viewHolder).getBrewMethodView().getContext(), BrewHistoryList.class);
                    intent.putExtra(BrewHistoryList.EXTRA_FILTER, index+1);
                    startActivity(intent);
                    m_brewAdapter.notifyItemChanged(viewHolder.getAdapterPosition());

                } else if (direction == ItemTouchHelper.RIGHT) {

                    Intent intent = new Intent(((BrewMethodViewHolder)viewHolder).getBrewMethodView().getContext(), TabbedRecipe.class);
                    intent.putExtra("extra_brew_method", m_arrMethodList.get(index).toString());
                    startActivity(intent);
                    m_brewAdapter.notifyItemChanged(index);
                }

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(this.m_vwBrewMethodList);
    }
}
