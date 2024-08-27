package com.java.uidemo.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.java.uidemo.R;
import com.java.uidemo.application.UIDemo;

public class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter
{

    private View view;
    private Context context;
    private UIDemo demo;

    public MapInfoWindowAdapter(Context context)
    {
        this.context = context;
        demo = (UIDemo)context.getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.info_window, null);
    }

    @Override
    public View getInfoContents(Marker marker)
    {
        if (marker != null
                && marker.isInfoWindowShown())
        {
            marker.hideInfoWindow();
            marker.showInfoWindow();
        }
        return null;
    }

    @Override
    public View getInfoWindow(final Marker marker)
    {
        if (marker.getTitle().equals("USER"))
            return null;
        String[] data = marker.getSnippet().split("\\|");
        final ImageView iv_info = view.findViewById(R.id.iv_info_window);
        iv_info.setImageResource(Integer.parseInt(data[0]));
        final TextView tv_title = view.findViewById(R.id.tv_title_info_window);
        tv_title.setTypeface(demo.getTf_ashley_semibold());
        tv_title.setText(data[1]);
        final TextView tv_text = view.findViewById(R.id.tv_text_info_window);
        tv_text.setTypeface(demo.getTf_monserrat_light());
        tv_text.setText(data[2]);
        return view;
    }
}