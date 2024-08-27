package com.java.uidemo.demos;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import static com.java.uidemo.util.Constants.REQUEST_LOCATION;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.java.uidemo.R;
import com.java.uidemo.util.DemoActivity;
import com.java.uidemo.util.DoubleArrayEvaluator;
import com.java.uidemo.util.Util;
import com.java.uidemo.view.MapInfoWindowAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

/**
 * This {@link DemoActivity} features an implementation of Google Maps.
 * </p><p>
 * The user's location is marked on the map, several other markers appear around it at random and clicking on them displays a custom {@link MapInfoWindowAdapter}.
 * </p><p>
 * The paintbrush button switches the style of the map. These are loaded from JSON files in /res/raw/.
 * </p>
 * If the gyroscope button is enabled, the map rotates with the device.
 */
public class MapActivity extends DemoActivity implements OnMapReadyCallback, LocationListener, SensorEventListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener
{
    private GoogleMap google_map;

    private Location current_location;
    private boolean first_location;
    private Marker user_marker;

    private RelativeLayout rl_bt_location, rl_bt_gyro;
    private ImageView iv_bt_north;
    private int current_style;


    private static final float DEFAULT_ZOOM = 11.5f;
    private float previous_zoom;
    private boolean user_movement;

    private boolean gyro;
    private boolean camera_idle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initDemo();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);
        current_location = null;
        requestPermissions();
        current_style = 0;
        final RelativeLayout rl_bt_style = findViewById(R.id.rl_bt_style_map);
        rl_bt_style.setOnClickListener(view ->
        {
            if (google_map != null)
            {
                Field[] raw_files = R.raw.class.getFields();
                ArrayList<Integer> map_styles = new ArrayList<>();
                for (Field rawFile : raw_files)
                {
                    try
                    {
                        if (rawFile.getName().startsWith("map_"))
                        {
                            map_styles.add(rawFile.getInt(rawFile));
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                if (!map_styles.isEmpty())
                {
                    if (map_styles.size()>current_style)
                    {
                        current_style++;
                    }
                    else
                    {
                        current_style = 0;
                    }
                    if (current_style==0)
                        google_map.setMapStyle(new MapStyleOptions("[]"));
                    else
                        google_map.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapActivity.this, map_styles.get(current_style-1)));
                }

            }
        });

        rl_bt_location = findViewById(R.id.rl_bt_location_map);
        rl_bt_location.setOnClickListener(view ->
        {
            if (current_location!=null)
            {
                move_map(current_location.getLatitude(), current_location.getLongitude(), 18);
            }
        });
        rl_bt_location.setVisibility(View.INVISIBLE);

        iv_bt_north = findViewById(R.id.iv_bt_north_map);
        final RelativeLayout rl_bt_north = findViewById(R.id.rl_bt_north_map);
        rl_bt_north.setOnClickListener(view ->
        {
            if (google_map!=null)
            {
                camera_idle = false;
                CameraPosition current_camera = google_map.getCameraPosition();
                CameraPosition north_camera = new CameraPosition.Builder(current_camera)
                        .tilt(current_camera.tilt)
                        .bearing(0)
                        .build();
                google_map.animateCamera(CameraUpdateFactory.newCameraPosition(north_camera));
            }
        });

        rl_bt_gyro = findViewById(R.id.rl_bt_gyro_map);
        final ImageView iv_bt_gyro = findViewById(R.id.iv_bt_gyro_map);
        rl_bt_gyro.setOnClickListener(view ->
        {
            gyro = !gyro;
            if (gyro)
            {
                iv_bt_gyro.setImageTintList(getColorStateList(R.color.ui_demo_green));
            }
            else
            {
                iv_bt_gyro.setImageTintList(getColorStateList(R.color.dark_gray_to_light_gray));
            }
        });
        rl_bt_gyro.setVisibility(View.INVISIBLE);

        SensorManager sensor_manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor_manager.registerListener(this, sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        sensor_manager.registerListener(this, sensor_manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
    }
    private void requestPermissions()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED)
        {
            initLocationService();
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
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

    @Override
    public void onRequestPermissionsResult(int request_code, @NonNull String[] permissions, @NonNull int[] grant_results)
    {
        super.onRequestPermissionsResult(request_code, permissions, grant_results);
        final Handler h = new Handler(Looper.getMainLooper());
        h.postDelayed(this::requestPermissions, 1000);
    }

    public void updateLatestLocation(Location location)
    {
        current_location = location;
        rl_bt_location.setVisibility(View.VISIBLE);
        if (google_map != null)
        {
            if (!first_location)
            {
                move_map(current_location.getLatitude(), current_location.getLongitude(), google_map.getCameraPosition().zoom);
                first_location = true;
                addRandomMarkers();
            }
            if (user_marker==null)
            {
                BitmapDrawable bitmap_drawable = (BitmapDrawable) AppCompatResources.getDrawable(MapActivity.this,R.drawable.map_marker);
                Bitmap b = bitmap_drawable.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, (int)Util.convertDpToPixel(30,MapActivity.this), (int)Util.convertDpToPixel(30,MapActivity.this), false);
                MarkerOptions mo = new MarkerOptions();
                mo.position(new LatLng(current_location.getLatitude(), current_location.getLongitude()));
                mo.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                mo.title("USER");
                user_marker = google_map.addMarker(mo);
            }
            else
            {
                double[] startValues = new double[]{user_marker.getPosition().latitude, user_marker.getPosition().longitude};
                double[] endValues = new double[]{current_location.getLatitude(), current_location.getLongitude()};
                ValueAnimator latLngAnimator = ValueAnimator.ofObject(new DoubleArrayEvaluator(), startValues, endValues);
                latLngAnimator.setDuration(600);
                latLngAnimator.setInterpolator(new DecelerateInterpolator());
                latLngAnimator.addUpdateListener(animation ->
                {
                    double[] animatedValue = (double[]) animation.getAnimatedValue();
                    user_marker.setPosition(new LatLng(animatedValue[0], animatedValue[1]));
                });
                latLngAnimator.start();
            }
        }
    }

    private void addRandomMarkers()
    {
        for (int i=0; i<new Random().nextInt(15)+5; i++)
        {
            int drawable = 0;
            int icon = 0;
            switch (new Random().nextInt(3))
            {
                case 0:
                    drawable = R.drawable.poi0;
                    break;
                case 1:
                    drawable = R.drawable.poi1;
                    break;
                case 2:
                    drawable = R.drawable.poi2;
                    break;
            }
            switch (new Random().nextInt(3))
            {
                case 0:
                    icon = R.drawable.lc0;
                    break;
                case 1:
                    icon = R.drawable.lc1;
                    break;
                case 2:
                    icon = R.drawable.lc2;
                    break;
            }

            BitmapDrawable bitmap_drawable = (BitmapDrawable) AppCompatResources.getDrawable(MapActivity.this, drawable);
            Bitmap b = bitmap_drawable.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, (int)Util.convertDpToPixel(30,MapActivity.this), (int)Util.convertDpToPixel(30,MapActivity.this), false);

            MarkerOptions mo = new MarkerOptions();
            mo.position(new LatLng(current_location.getLatitude()+new Random().nextFloat()*0.2-0.1, current_location.getLongitude()+new Random().nextFloat()*0.2-0.1));
            mo.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            mo.title(String.valueOf(i));
            mo.snippet(icon+"|"+getString(R.string.marker)+" "+i+"|"+Util.randomPieceOfLorem(this));
            google_map.addMarker(mo);
        }

    }
    @Override
    public void onMapReady(@NonNull GoogleMap google_map)
    {
        this.google_map = google_map;
        google_map.setMapStyle(new MapStyleOptions("[]"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            google_map.setMyLocationEnabled(false);
        }
        if (current_location !=null)
        {
            move_map(current_location.getLatitude(), current_location.getLongitude(),DEFAULT_ZOOM);
            first_location = true;
            addRandomMarkers();
        }
        google_map.setOnMarkerClickListener(this);
        google_map.setOnInfoWindowClickListener(this);
        google_map.setInfoWindowAdapter(new MapInfoWindowAdapter(MapActivity.this));
        google_map.getUiSettings().setMyLocationButtonEnabled(false);
        google_map.getUiSettings().setCompassEnabled(false);
        google_map.getUiSettings().setMapToolbarEnabled(false);
        google_map.getUiSettings().setZoomControlsEnabled(false);
        google_map.setOnCameraMoveListener(() ->
        {
            CameraPosition current_camera = google_map.getCameraPosition();
            if (user_movement)
            {
                camera_idle = false;
                if (current_camera.zoom!=previous_zoom)
                {
                    int tilt = 0;
                    if (current_camera.zoom>10&&current_camera.zoom<18)
                    {
                        tilt = (int)current_camera.zoom*90/18;
                    }
                    if (current_camera.zoom>18)
                        tilt = 90;
                    CameraPosition tilted_camera = new CameraPosition.Builder(current_camera)
                            .tilt(tilt)
                            .bearing(current_camera.bearing)
                            .build();
                    google_map.animateCamera(CameraUpdateFactory.newCameraPosition(tilted_camera));
                    previous_zoom = current_camera.zoom;
                }
            }
            iv_bt_north.setRotation(-current_camera.bearing);
        });
        google_map.setOnCameraMoveStartedListener(reason ->
        {
            user_movement = reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE;
        });
        google_map.setOnCameraIdleListener(() -> camera_idle = true);
    }
    private void move_map(double lat, double lon, float zoom)
    {
        camera_idle = false;
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), zoom);
        google_map.animateCamera(cu, new GoogleMap.CancelableCallback()
        {
            @Override
            public void onFinish() {
                CameraPosition cameraPosition = new CameraPosition.Builder(google_map.getCameraPosition())
                        .tilt(90)
                        .build();
                google_map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

            @Override
            public void onCancel() {

            }
        });
    }


    @Override
    public void onLocationChanged(@NonNull Location location)
    {
        updateLatestLocation(location);
    }

    float[] mGravity;
    float[] mGeomagnetic;
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = Util.applyLowPassFilter(event.values.clone(), mGravity);
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = Util.applyLowPassFilter(event.values.clone(), mGeomagnetic);
        if (mGravity != null && mGeomagnetic != null)
        {
            rl_bt_gyro.setVisibility(View.VISIBLE);
            float[] R = new float[9];
            float[] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success)
            {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R, orientation);
                float azimut = orientation[0];
                if (gyro&&camera_idle)
                {
                    float gyro_bearing = azimut * 360 / (2 * 3.14159f);
                    CameraPosition current_camera = google_map.getCameraPosition();
                    CameraPosition north_camera = new CameraPosition.Builder(current_camera)
                            .tilt(current_camera.tilt)
                            .bearing(gyro_bearing)
                            .build();
                    google_map.animateCamera(CameraUpdateFactory.newCameraPosition(north_camera));
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {

    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker)
    {
        marker.hideInfoWindow();
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker)
    {
        return marker.getTitle()!=null&&marker.getTitle().equals("USER");
    }
}
