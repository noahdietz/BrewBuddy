package edu.calpoly.ndietz.brewbuddy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by ndietz on 4/17/16.
 */
public class BrewMethodView extends RelativeLayout {

    private TextView m_vwMethodText;
    private BrewMethod m_method;
    private ImageView m_vwBackgroundImage;
    private Context m_context;

    public BrewMethodView(Context context, BrewMethod method) {
        super(context);

        this.m_context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.brew_method, this, true);

        this.m_vwMethodText = (TextView) this.findViewById(R.id.brew_method_text);
        this.m_vwBackgroundImage = (ImageView) this.findViewById(R.id.background_image_view);
        this.setMethod(method);
    }

    public BrewMethodView(Context context) {
        super(context);

        this.m_context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.brew_method, this, true);

        this.m_vwMethodText = (TextView) this.findViewById(R.id.brew_method_text);
        this.m_vwBackgroundImage = (ImageView) this.findViewById(R.id.background_image_view);
        this.setMethod(new BrewMethod(""));
    }

    public void setMethod(BrewMethod method) {
        this.m_method = method;
        this.m_vwMethodText.setText(this.m_method.getM_method_name());

        switch (method.getM_method_name()) {
            case "Chemex":
                Picasso.with(this.m_context).load(R.drawable.chemex_background).into(this.m_vwBackgroundImage);
                break;
            case "French Press":
                Picasso.with(this.m_context).load(R.drawable.frenchpress_background).into(this.m_vwBackgroundImage);
                break;
            case "Aeropress":
                Picasso.with(this.m_context).load(R.drawable.aeropress_background).into(this.m_vwBackgroundImage);
                break;
            case "Hario V60":
                Picasso.with(this.m_context).load(R.drawable.hario_background).into(this.m_vwBackgroundImage);
                break;
            case "Bee House":
                Picasso.with(this.m_context).load(R.drawable.beehouse_background).into(this.m_vwBackgroundImage);
                break;
            case "Kalita Wave":
                Picasso.with(this.m_context).load(R.drawable.kalita_background).into(this.m_vwBackgroundImage);
                break;
            case "Moka Pot":
                Picasso.with(this.m_context).load(R.drawable.mokapot_background).into(this.m_vwBackgroundImage);
                break;
            default:
                break;
        }

        this.requestLayout();
    }

    public BrewMethod getMethod() { return this.m_method; }
}
