package com.java.uidemo.demos;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.hardware.SensorManager.AXIS_MINUS_X;
import static android.hardware.SensorManager.AXIS_MINUS_Y;
import static android.hardware.SensorManager.AXIS_X;
import static android.hardware.SensorManager.AXIS_Y;
import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;
import static android.hardware.SensorManager.getOrientation;
import static android.hardware.SensorManager.getRotationMatrixFromVector;
import static android.hardware.SensorManager.remapCoordinateSystem;
import static android.view.Surface.ROTATION_0;
import static android.view.Surface.ROTATION_180;
import static android.view.Surface.ROTATION_270;
import static android.view.Surface.ROTATION_90;

import static com.java.uidemo.util.Constants.REQUEST_CAMERA;
import static com.java.uidemo.util.Constants.REQUEST_LOCATION;

import android.Manifest;
import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.opengl.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.java.uidemo.R;
import com.java.uidemo.util.Animations;
import com.java.uidemo.util.DemoActivity;
import com.java.uidemo.view.AROverlayView;

import java.util.concurrent.ExecutionException;

/**
 * This {@link DemoActivity} displays the preview from the device's camera and places an {@link AROverlayView} on top of it.
 * </p><p>
 * Combining the GPS location with the gyroscope information, it can display a virtual representation of a point in another location over the camera preview, in the direction that it would be.
 * </p><p>
 * If the point is behind the camera, an arrow will appear instead, pointing in the closest direction towards the point.
 */
public class ARActivity extends DemoActivity implements SensorEventListener, LocationListener
{
    private SensorManager sensor_manager;
    private boolean init;

    private AROverlayView ar_overlay_view;
    private ImageView iv_arrow;
    private PreviewView preview_view;
    private ProcessCameraProvider camera_provider;
    private RelativeLayout camera_container;
    private float[] projection_matrix;

    private int touch_x, touch_y;

    private int click_count;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
        initDemo();
        ar_overlay_view = new AROverlayView(this, (visible, x, y, width, height) ->
        {
            if (visible)
            {
                hideGuideArrow();
            }
            else
            {
                displayGuideArrow(x,y,width,height);
            }
        });
        preview_view = findViewById(R.id.preview_view_ar);
        camera_container = findViewById(R.id.camera_container_ar);
        iv_arrow = findViewById(R.id.iv_arrow_ar);
        iv_arrow.setRotation(0);
        touch_x = 0;
        touch_y = 0;
        iv_arrow.setVisibility(View.INVISIBLE);
        final View v_ripple = findViewById(R.id.v_ripple_ar);
        final TextView tv_click = findViewById(R.id.tv_click_ar);
        tv_click.setTypeface(demo.getTf_ashley_semibold());
        final RelativeLayout rl_click = findViewById(R.id.rl_click_ar);
        rl_click.setOnTouchListener((view, event) ->
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                touch_x = (int) event.getX();
                touch_y = (int) event.getY();
                RelativeLayout.LayoutParams ripple_params = (RelativeLayout.LayoutParams) v_ripple.getLayoutParams();
                ripple_params.setMargins(touch_x-v_ripple.getWidth()/2, touch_y-v_ripple.getHeight()/2, 0, 0);
                v_ripple.setLayoutParams(ripple_params);
                RelativeLayout.LayoutParams text_params = (RelativeLayout.LayoutParams) tv_click.getLayoutParams();
                text_params.setMargins(touch_x-tv_click.getWidth()/2, touch_y-tv_click.getHeight(),0,0);
                tv_click.setLayoutParams(text_params);
                Animations.ripple(v_ripple,300);
            }
            return false;
        });
        rl_click.setOnClickListener(view ->
        {
            if (ar_overlay_view.checkClick(touch_x,touch_y))
            {
                click_count++;
                tv_click.setText(String.valueOf(click_count));
                Animations.ripple(tv_click,600);
                ar_overlay_view.generateNewArPoint();

            }
        });

        final Handler h = new Handler(Looper.getMainLooper());
        h.postDelayed(this::init, 100);
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                if (!checkBack())
                {
                    releaseCamera();
                    menu();
                }
            }
        });
    }

    private void init()
    {
        if (init)
            return;
        init = true;
        sensor_manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        requestPermissions();
    }

    private void requestPermissions()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED)
        {
            initLocationService();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PERMISSION_GRANTED)
            {
                initARCameraView();
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            }
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int request_code, @NonNull String[] permissions, @NonNull int[] grant_results)
    {
        super.onRequestPermissionsResult(request_code, permissions, grant_results);
        final Handler h = new Handler(Looper.getMainLooper());
        h.postDelayed(this::requestPermissions, 1000);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor.getType() != Sensor.TYPE_ROTATION_VECTOR)
            return;

        final float[] rotation_matrix_from_vector = new float[16];
        getRotationMatrixFromVector(rotation_matrix_from_vector, event.values);
        final int screen_rotation = getWindowManager().getDefaultDisplay().getRotation();

        final float[] orientation = new float[3];
        getOrientation(getRotatedProjectionMatrix(screen_rotation, rotation_matrix_from_vector), orientation);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    @Override
    public void onLocationChanged(@NonNull Location location)
    {
        updateLatestLocation(location);
    }

    public void releaseCamera()
    {
        try
        {
            camera_provider.unbindAll();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initARCameraView()
    {
        ListenableFuture<ProcessCameraProvider> camera_provider_future = ProcessCameraProvider.getInstance(this);
        camera_provider_future.addListener(() ->
        {
            try
            {
                camera_provider = camera_provider_future.get();
                final Handler h = new Handler(Looper.getMainLooper());
                h.postDelayed(this::initCamera, 100);
            }
            catch (ExecutionException | InterruptedException e)
            {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
        initProjectionMatrix();
    }

    private void initCamera()
    {
        final CameraSelector camera_selector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        final Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(preview_view.getSurfaceProvider());

        final Handler h = new Handler(Looper.getMainLooper());
        h.postDelayed(() -> bindCamera(camera_selector,preview),100);
    }

    private void bindCamera(CameraSelector camera_selector, Preview preview)
    {
        camera_provider.bindToLifecycle(this, camera_selector, preview);
        final Handler h = new Handler(Looper.getMainLooper());
        h.postDelayed(this::initAROverlayView, 100);
    }

    private void initProjectionMatrix()
    {
        projection_matrix = new float[16];
        float ratio;
        if (preview_view.getWidth() < preview_view.getHeight())
            ratio = (float) preview_view.getWidth() / preview_view.getHeight();
        else
            ratio = (float) preview_view.getHeight() / preview_view.getWidth();
        Matrix.frustumM(projection_matrix, 0, -ratio, ratio, -1, 1, 0.5f, 10000);
    }

    private void initLocationService()
    {
        try
        {
            LocationManager location_manager = (LocationManager) getSystemService(LOCATION_SERVICE);

            final boolean is_gps_enabled = location_manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            String provider;
            if (is_gps_enabled)
                provider = LocationManager.GPS_PROVIDER;
            else
                provider = LocationManager.NETWORK_PROVIDER;

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PERMISSION_DENIED)
                return;

            location_manager.requestLocationUpdates(provider,0,0, this);
            updateLatestLocation(location_manager.getLastKnownLocation(provider));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updateLatestLocation(Location location)
    {
        if (location!=null && ar_overlay_view !=null)
        {
            ar_overlay_view.updateCurrentLocation(location);
        }
    }

    public void initAROverlayView()
    {
        sensor_manager.registerListener(this,
                sensor_manager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SENSOR_DELAY_NORMAL);
        if (ar_overlay_view.getParent() != null)
            ((ViewGroup) ar_overlay_view.getParent()).removeView(ar_overlay_view);
        camera_container.addView(ar_overlay_view);
    }

    /** @noinspection SuspiciousNameCombination*/
    public float[] getRotatedProjectionMatrix(int screen_rotation, float[] rotation_matrix_from_vector)
    {
        final float[] rotation_matrix = new float[16];
        switch (screen_rotation)
        {
            case ROTATION_0:
                remapCoordinateSystem(rotation_matrix_from_vector,
                        AXIS_X, AXIS_Y,
                        rotation_matrix);
                break;
            case ROTATION_90:
                remapCoordinateSystem(rotation_matrix_from_vector,
                        AXIS_Y,
                        AXIS_MINUS_X, rotation_matrix);
                break;
            case ROTATION_270:
                remapCoordinateSystem(rotation_matrix_from_vector,
                        AXIS_MINUS_Y,
                        AXIS_X, rotation_matrix);
                break;
            case ROTATION_180:
                remapCoordinateSystem(rotation_matrix_from_vector,
                        AXIS_MINUS_X, AXIS_MINUS_Y,
                        rotation_matrix);
                break;
        }
        final float[] rotated_projection_matrix = new float[16];
        Matrix.multiplyMM(rotated_projection_matrix, 0, projection_matrix, 0, rotation_matrix, 0);
        if (ar_overlay_view !=null)
            ar_overlay_view.updateRotatedProjectionMatrix(rotated_projection_matrix);

        return rotated_projection_matrix;
    }

    public void displayGuideArrow(float x, float y,float width, float height)
    {
        iv_arrow.setVisibility(View.VISIBLE);
        float x1 = width/2;
        float y1 = height/2;
        double r = Math.atan2(x - x1 / 2, y1 / 2 - (y-500));
        int rotation = (int) Math.toDegrees(r);
        iv_arrow.setRotation(rotation);
    }

    public void hideGuideArrow()
    {
        iv_arrow.setVisibility(View.INVISIBLE);
    }
}