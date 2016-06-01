package edu.calpoly.ndietz.brewbuddy;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ndietz on 5/31/16.
 */
public class BrewMethodViewHolder extends RecyclerView.ViewHolder {
    private BrewMethodView mBv;

    public BrewMethodViewHolder(View itemView) {
        super(itemView);

        this.mBv = (BrewMethodView) itemView;
        this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TabbedRecipe.class);
                intent.putExtra("extra_brew_method", mBv.toString());
                v.getContext().startActivity(intent);
            }
        });
    }

    public void bind(BrewMethod bm) {
        this.mBv.setMethod(bm);
    }

    public BrewMethodView getBrewMethodView() {
        return this.mBv;
    }
}
