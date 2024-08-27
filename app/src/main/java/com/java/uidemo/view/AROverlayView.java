package com.java.uidemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.opengl.Matrix;
import android.view.View;

import androidx.annotation.NonNull;

import com.java.uidemo.R;
import com.java.uidemo.application.UIDemo;
import com.java.uidemo.model.ARPoint;
import com.java.uidemo.util.Util;

import java.util.Locale;
import java.util.Random;

/**
 * A very simple 2D overlay for Augmented Reality.
 * </p>
 * It will display a marker on screen if the device is pointed towards its coordinates.
 */
public class AROverlayView extends View
{
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap bm_arrow_t, bm_arrow_r, bm_arrow_b, bm_arrow_l;
    private Location current_location;
    private float[] rotated_projection_matrix;
    private final float[] camera_coordinate_vector;
    private ARPoint current_ar_point;
    private int animation_frame;
    private final AROverlayListener listener;

    public AROverlayView(Context context, AROverlayListener listener)
    {
        super(context);

        UIDemo demo = (UIDemo) context.getApplicationContext();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(demo.getTf_monserrat_light());
        paint.setTextSize(30);

        animation_frame = 0;

        current_ar_point = new ARPoint("",0,0,0);

        bm_arrow_t = BitmapFactory.decodeResource(context.getResources(), R.drawable.ar_arrow_t);
        bm_arrow_t = Bitmap.createScaledBitmap(bm_arrow_t, 120, 120, false);
        bm_arrow_r = BitmapFactory.decodeResource(context.getResources(), R.drawable.ar_arrow_r);
        bm_arrow_r = Bitmap.createScaledBitmap(bm_arrow_r, 120, 120, false);
        bm_arrow_b = BitmapFactory.decodeResource(context.getResources(), R.drawable.ar_arrow_b);
        bm_arrow_b = Bitmap.createScaledBitmap(bm_arrow_b, 120, 120, false);
        bm_arrow_l = BitmapFactory.decodeResource(context.getResources(), R.drawable.ar_arrow_l);
        bm_arrow_l = Bitmap.createScaledBitmap(bm_arrow_l, 120, 120, false);

        rotated_projection_matrix = new float[16];
        camera_coordinate_vector = new float[4];
        this.listener = listener;
    }

    public void updateCurrentLocation(Location location)
    {
        if (!isBetterLocation(location))
            return;

        current_location = location;
        if (current_ar_point.getLocation().getLatitude()==0&& current_ar_point.getLocation().getLongitude()==0)
        {
            generateNewArPoint();
        }
        invalidate();
    }

    public void generateNewArPoint()
    {
        Random r = new Random();
        float lat = (float) current_location.getLatitude() + r.nextFloat()-0.5f;
        float lon = (float) current_location.getLongitude() + r.nextFloat()-0.5f;
        current_ar_point = new ARPoint(this.getContext().getString(R.string.click_me), lat, lon, current_location.getAltitude());
    }

    public void updateRotatedProjectionMatrix(float[] rotated_projection_matrix)
    {
        this.rotated_projection_matrix = rotated_projection_matrix;
        invalidate();
    }

    private boolean isBetterLocation(Location location)
    {
        if (current_location == null)
            return true;

        final long time_delta = location.getTime() - current_location.getTime();
        final boolean is_newer = time_delta > 0;

        if (time_delta > TWO_MINUTES)
            return true;
        else if (time_delta < -TWO_MINUTES)
            return false;

        int accuracy_delta = (int) (location.getAccuracy() - current_location.getAccuracy());

        if (accuracy_delta < 0)
            return true;
        else if (is_newer && accuracy_delta == 0)
            return true;
        else
            return is_newer && accuracy_delta <= 200 && location.getProvider()!=null && location.getProvider().equals(current_location.getProvider());
    }

    public boolean checkClick(int x, int y)
    {
        if (ar_point_on_screen_x>-1&&ar_point_on_screen_y>-1)
        {
            double d = Math.sqrt(Math.pow(x - ar_point_on_screen_x, 2) + Math.pow(y - ar_point_on_screen_y, 2));
            return d < Util.convertDpToPixel(50,getContext());
        }
        return false;
    }

    private int ar_point_on_screen_x, ar_point_on_screen_y;

    @Override
    protected void onDraw(@NonNull Canvas canvas)
    {
        super.onDraw(canvas);
        if (current_location == null)
            return;

        final Location location = current_ar_point.getLocation();

        final float[] current_location_ECEF = Util.WSG84toECEF(current_location);
        final float[] point_ECEF = Util.WSG84toECEF(location);
        final float[] pointInENU = Util.ECEFtoENU(current_location, current_location_ECEF, point_ECEF);

        final int distance = Math.round(current_location.distanceTo(location));
        String distance_text = distance + " m";

        if (distance > 1000)
            distance_text = String.format(Locale.US, "%.2f", distance / 1000f) + " km";

        Matrix.multiplyMV(camera_coordinate_vector, 0, rotated_projection_matrix, 0, pointInENU, 0);

        final float x = (0.5f + camera_coordinate_vector[0] / camera_coordinate_vector[3]) * getWidth();
        final float y = (0.5f - camera_coordinate_vector[1] / camera_coordinate_vector[3]) * getHeight();

        if (x < 0 || x > getWidth() || y < 0 || y > getHeight() || camera_coordinate_vector[2]>=0)
        {
            ar_point_on_screen_x = -1;
            ar_point_on_screen_y = -1;
            if (camera_coordinate_vector[2]>=0)
            {
                if (listener!=null)
                    listener.currentState(false,-x,-y,getWidth(),getHeight());
            }
            else
            {
                if (listener!=null)
                    listener.currentState(false,x,y,getWidth(),getHeight());
            }
            return;
        }
        ar_point_on_screen_x = (int)x;
        ar_point_on_screen_y = (int)y;
        if (listener!=null)
            listener.currentState(true,x,y,getWidth(),getHeight());
        if (animation_frame>=10)
            animation_frame = 0;
        int movement;
        if (animation_frame<5)
            movement = (animation_frame)*3;
        else
            movement = (10 - animation_frame)*3;
        animation_frame++;

        //canvas.drawBitmap(bm_icon, x - (bm_icon.getWidth() / 2f), y - (bm_icon.getHeight() / 2f), paint);
        canvas.drawBitmap(bm_arrow_t, x - (bm_arrow_t.getWidth() / 2f), y - (bm_arrow_t.getHeight() / 2f - movement), paint);
        canvas.drawBitmap(bm_arrow_r, x - (bm_arrow_r.getWidth() / 2f) - movement, y - (bm_arrow_r.getHeight() / 2f), paint);
        canvas.drawBitmap(bm_arrow_b, x - (bm_arrow_b.getWidth() / 2f), y - (bm_arrow_b.getHeight() / 2f + movement), paint);
        canvas.drawBitmap(bm_arrow_l, x - (bm_arrow_l.getWidth() / 2f) + movement, y - (bm_arrow_l.getHeight() / 2f), paint);
        canvas.drawText(current_ar_point.getName(), x - (13 * current_ar_point.getName().length() / 2f), y - 75, paint);
        canvas.drawText(distance_text, x - (15 * distance_text.length() / 2f), y + 100, paint);

    }

    public interface AROverlayListener
    {
        void currentState(boolean visible, float x, float y, float width, float height);
    }
}
