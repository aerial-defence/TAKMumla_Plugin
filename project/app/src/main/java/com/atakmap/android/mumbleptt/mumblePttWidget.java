package com.atakmap.android.mumbleptt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.MotionEvent;

import com.atakmap.android.dropdown.DropDownManager;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapView;

import com.atakmap.android.widgets.LinearLayoutWidget;
import com.atakmap.android.widgets.MapWidget;
import com.atakmap.android.widgets.RootLayoutWidget;
import com.atakmap.coremap.maps.assets.Icon;
import com.atakmap.android.widgets.MarkerIconWidget;
import com.atakmap.coremap.log.Log;
import com.atakmap.android.mumbleptt.plugin.R;

public class mumblePttWidget extends MarkerIconWidget
        implements MapWidget.OnPressListener, MapWidget.OnUnpressListener, MapWidget.OnClickListener {
    private final static int ICON_WIDTH = 64;
    private final static int ICON_HEIGHT = 64;
    private static SharedPreferences sharedPreference = null;
    private int toggled = 0;

    public static final String TAG = "mumblePttWidget";

    public mumblePttWidget(MapView mapView) {
        setName("Mumble PTT");
        RootLayoutWidget root = (RootLayoutWidget) mapView.getComponentExtra("rootLayoutWidget");
        LinearLayoutWidget brLayout = root.getLayout(RootLayoutWidget.BOTTOM_RIGHT);
        brLayout.addWidget(this);
        addOnPressListener(this);
        addOnUnpressListener(this);
        addOnClickListener(this);
        setIcon(toggled);
        this.sharedPreference = PreferenceManager.getDefaultSharedPreferences(mapView.getContext().getApplicationContext());

    }
    private void setIcon(int toggle) {

        int drawId;
        switch (toggle) {
            case 1:
                drawId = R.drawable.mic_on;
                break;
            default:
                drawId = R.drawable.mic_off;
                break;
        }

        String imageUri = "android.resource://com.atakmap.android.mumbleptt.plugin/" + drawId;

        Log.d(TAG, "imageURi " + imageUri);
        Icon.Builder builder = new Icon.Builder();
        builder.setAnchor(0, 0);
        builder.setColor(Icon.STATE_DEFAULT, Color.WHITE);
        builder.setSize(ICON_WIDTH, ICON_HEIGHT);
        builder.setImageUri(Icon.STATE_DEFAULT, imageUri);

        Icon icon = builder.build();
        setIcon(icon);
    }

    @Override
    public void onMapWidgetPress(MapWidget mapWidget, MotionEvent event) {
        if (mapWidget == this) {
            if (sharedPreference.getBoolean("plugin_mumbleptt_ptt", false)) {
                Intent i = new Intent("se.lublin.mumla.action.TALK");
                AtakBroadcast.getInstance().sendSystemBroadcast(i);
                setIcon(((++toggled) % 2));
            }
        }
    }

    @Override
    public void onMapWidgetUnpress(MapWidget mapWidget, MotionEvent event) {
        if (mapWidget == this) {
            if (sharedPreference.getBoolean("plugin_mumbleptt_ptt", false)) {
                Intent i = new Intent("se.lublin.mumla.action.TALK");
                AtakBroadcast.getInstance().sendSystemBroadcast(i);
                setIcon(((++toggled) % 2));
            }
        }

    }

    @Override
    public void onMapWidgetClick(MapWidget mapWidget, MotionEvent event) {
        if (mapWidget == this) {
            if (!sharedPreference.getBoolean("plugin_mumbleptt_ptt", false)) {
                Intent i = new Intent("se.lublin.mumla.action.TALK");
                AtakBroadcast.getInstance().sendSystemBroadcast(i);
                setIcon(((++toggled) % 2));
            }
        }
    }
}
