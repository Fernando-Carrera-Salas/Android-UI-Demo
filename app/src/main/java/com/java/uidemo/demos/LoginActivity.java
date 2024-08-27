package com.java.uidemo.demos;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;

import com.github.ybq.android.spinkit.SpinKitView;
import com.java.uidemo.R;
import com.java.uidemo.util.Animations;
import com.java.uidemo.util.CustomFocusChangeListener;
import com.java.uidemo.util.DemoActivity;
import com.java.uidemo.util.Util;

/**
 * This {@link DemoActivity} displays a sample login screen.
 * </p><p>
 * Password recovery and register screens are included in the same interface.
 * </p><p>
 * The logo has a glare effect which reacts to the user tilting their device.
 */
public class LoginActivity extends DemoActivity implements SensorEventListener
{
    private ImageView iv_glare;
    private boolean show_password;
    private RelativeLayout rl_password;
    private RelativeLayout rl_repeat_password;
    private RelativeLayout rl_bt_login;
    private RelativeLayout rl_code;
    private TextView tv_forgot, tv_register, tv_code, tv_bt_login;
    private EditText et_user, et_password, et_repeat_password, et_code;
    private View v_click;
    private SpinKitView skv_load;

    private int screen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        initDemo();
        iv_glare = findViewById(R.id.iv_glare_login);
        ImageView iv_user = findViewById(R.id.iv_user_login);
        ImageView iv_password = findViewById(R.id.iv_password_login);
        ImageView iv_repeat_password = findViewById(R.id.iv_repeat_password_login);
        ImageView iv_code = findViewById(R.id.ivCodeLogin);
        ImageView iv_bt_show_password = findViewById(R.id.iv_bt_show_password_login);
        ImageView iv_bt_show_repeat_password = findViewById(R.id.iv_bt_show_repeat_password_login);
        et_user = findViewById(R.id.et_user_login);
        et_user.setTypeface(demo.getTf_monserrat_light());
        et_password = findViewById(R.id.et_password_login);
        et_password.setTypeface(demo.getTf_monserrat_light());
        et_repeat_password = findViewById(R.id.et_repeat_password_login);
        et_repeat_password.setTypeface(demo.getTf_monserrat_light());
        et_code = findViewById(R.id.etCodeLogin);
        et_code.setTypeface(demo.getTf_monserrat_light());

        v_click = findViewById(R.id.v_click_login);
        skv_load = findViewById(R.id.skv_load_login);
        v_click.setVisibility(View.INVISIBLE);
        skv_load.setVisibility(View.INVISIBLE);

        et_user.setOnFocusChangeListener(new CustomFocusChangeListener());
        ((CustomFocusChangeListener)et_user.getOnFocusChangeListener()).setViews(new View[]{iv_user}, null);
        et_password.setOnFocusChangeListener(new CustomFocusChangeListener(focused ->
        {
            if (!focused)
            {
                et_password.setTransformationMethod(new PasswordTransformationMethod());
                et_repeat_password.setTransformationMethod(new PasswordTransformationMethod());
                iv_bt_show_password.setImageResource(R.drawable.hidden);
                iv_bt_show_repeat_password.setImageResource(R.drawable.hidden);
                show_password = false;
            }
        }));
        ((CustomFocusChangeListener)et_password.getOnFocusChangeListener()).setViews(new View[]{iv_password}, new View[]{iv_bt_show_password});
        et_password.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_GO
                    || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND)
            {
                if (screen!=3)
                {
                    rl_bt_login.performClick();
                    return true;
                }
            }
            return false;
        });
        et_repeat_password.setOnFocusChangeListener(new CustomFocusChangeListener(focused ->
        {
            if (!focused)
            {
                et_password.setTransformationMethod(new PasswordTransformationMethod());
                et_repeat_password.setTransformationMethod(new PasswordTransformationMethod());
                iv_bt_show_password.setImageResource(R.drawable.hidden);
                iv_bt_show_repeat_password.setImageResource(R.drawable.hidden);
                show_password = false;
            }
        }));
        ((CustomFocusChangeListener)et_repeat_password.getOnFocusChangeListener()).setViews(new View[]{iv_repeat_password}, new View[]{iv_bt_show_repeat_password});
        et_repeat_password.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_GO
                    || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND)
            {
                rl_bt_login.performClick();
                return true;
            }
            return false;
        });

        et_code.setOnFocusChangeListener(new CustomFocusChangeListener());
        ((CustomFocusChangeListener)et_code.getOnFocusChangeListener()).setViews(new View[]{iv_code}, null);
        et_code.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_GO
                    || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND)
            {
                rl_bt_login.performClick();
                return true;
            }
            return false;
        });
        final float default_code_size = et_code.getTextSize();
        et_code.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (charSequence.length()>0)
                    et_code.setTextSize(TypedValue.COMPLEX_UNIT_PX, default_code_size);
                else
                    et_code.setTextSize(TypedValue.COMPLEX_UNIT_PX, et_password.getTextSize());
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
        et_code.setTextSize(TypedValue.COMPLEX_UNIT_PX, et_password.getTextSize());

        tv_bt_login = findViewById(R.id.tv_bt_login);
        tv_bt_login.setTypeface(demo.getTf_ashley_semibold());
        tv_bt_login.setVisibility(View.VISIBLE);

        rl_bt_login = findViewById(R.id.rl_bt_login);
        rl_bt_login.setOnClickListener(view ->
        {
            Util.hideKeyboard(LoginActivity.this);
            clearAllFocus();

            switch (screen)
            {
                case 0:
                    if (et_user.getText().toString().trim().isEmpty())
                    {
                        et_user.setError(getString(R.string.enter_the_username));
                        return;
                    }
                    else
                    {
                        et_user.setError(null);
                        if (et_password.getText().toString().trim().isEmpty())
                        {
                            et_password.setError(getString(R.string.enter_the_password));
                            return;
                        }
                        else
                        {
                            et_password.setError(null);
                            v_click.setVisibility(View.VISIBLE);
                            skv_load.setVisibility(View.VISIBLE);
                            tv_bt_login.setVisibility(View.INVISIBLE);
                            Handler h = new Handler(Looper.getMainLooper());
                            h.postDelayed(() ->
                            {
                                v_click.setVisibility(View.INVISIBLE);
                                skv_load.setVisibility(View.INVISIBLE);
                                tv_bt_login.setVisibility(View.VISIBLE);
                            },2000);
                        }
                    }
                    break;
                case 1:
                    if (et_user.getText().toString().trim().isEmpty())
                    {
                        et_user.setError(getString(R.string.enter_the_username));
                        return;
                    }
                    else
                    {
                        et_user.setError(null);
                        recovery2();
                    }
                    break;
                case 2:
                    if (et_user.getText().toString().trim().isEmpty())
                    {
                        et_user.setError(getString(R.string.enter_the_username));
                        return;
                    }
                    else
                    {
                        et_user.setError(null);
                        if (et_code.getText().toString().trim().isEmpty())
                        {
                            et_code.setError(getString(R.string.enter_the_recovery_code));
                            return;
                        }
                        else
                        {
                            et_code.setError(null);
                            v_click.setVisibility(View.VISIBLE);
                            skv_load.setVisibility(View.VISIBLE);
                            tv_bt_login.setVisibility(View.INVISIBLE);
                            Handler h = new Handler(Looper.getMainLooper());
                            h.postDelayed(() ->
                            {
                                v_click.setVisibility(View.INVISIBLE);
                                skv_load.setVisibility(View.INVISIBLE);
                                tv_bt_login.setVisibility(View.VISIBLE);
                            },2000);
                        }
                    }
                    break;
                case 3:
                    if (et_user.getText().toString().trim().isEmpty())
                    {
                        et_user.setError(getString(R.string.enter_the_username));
                        return;
                    }
                    else
                    {
                        et_user.setError(null);
                        if (et_password.getText().toString().trim().isEmpty())
                        {
                            et_password.setError(getString(R.string.enter_the_password));
                            return;
                        }
                        else
                        {
                            et_password.setError(null);
                            if (et_repeat_password.getText().toString().trim().isEmpty())
                            {
                                et_repeat_password.setError(getString(R.string.enter_the_password));
                                return;
                            }
                            else
                            {
                                if (!et_password.getText().toString().equals(et_repeat_password.getText().toString()))
                                {
                                    et_password.setError(getString(R.string.passwords_do_not_match));
                                    et_repeat_password.setError(getString(R.string.passwords_do_not_match));
                                    return;
                                }
                                else
                                {
                                    et_repeat_password.setError(null);
                                    v_click.setVisibility(View.VISIBLE);
                                    skv_load.setVisibility(View.VISIBLE);
                                    tv_bt_login.setVisibility(View.INVISIBLE);
                                    Handler h = new Handler(Looper.getMainLooper());
                                    h.postDelayed(() ->
                                    {
                                        v_click.setVisibility(View.INVISIBLE);
                                        skv_load.setVisibility(View.INVISIBLE);
                                        tv_bt_login.setVisibility(View.VISIBLE);
                                    },2000);
                                }
                            }
                            v_click.setVisibility(View.VISIBLE);
                            skv_load.setVisibility(View.VISIBLE);
                            tv_bt_login.setVisibility(View.INVISIBLE);
                            Handler h = new Handler(Looper.getMainLooper());
                            h.postDelayed(() ->
                            {
                                v_click.setVisibility(View.INVISIBLE);
                                skv_load.setVisibility(View.INVISIBLE);
                                tv_bt_login.setVisibility(View.VISIBLE);
                            },2000);
                        }
                    }
                    break;
            }
        });

        tv_forgot = findViewById(R.id.tv_bt_forgot_login);
        tv_forgot.setOnClickListener(view ->
        {
            Util.hideKeyboard(LoginActivity.this);
            clearAllFocus();
            recovery1();
        });
        tv_forgot.setTypeface(demo.getTf_monserrat_light());

        tv_register = findViewById(R.id.tv_bt_register_login);
        tv_register.setOnClickListener(view ->
        {
            Util.hideKeyboard(LoginActivity.this);
            clearAllFocus();
            register();
        });
        tv_register.setTypeface(demo.getTf_monserrat_light());

        tv_code = findViewById(R.id.tv_bt_code_login);
        tv_code.setOnClickListener(view ->
        {
            Util.hideKeyboard(LoginActivity.this);
            clearAllFocus();
            recovery2();
        });
        tv_code.setTypeface(demo.getTf_monserrat_light());

        iv_bt_show_password.setOnClickListener(view ->
        {
            et_password.requestFocus();
            show_password =!show_password;
            if (show_password)
            {
                et_password.setTransformationMethod(null);
                et_repeat_password.setTransformationMethod(null);
                iv_bt_show_password.setImageResource(R.drawable.show);
                iv_bt_show_repeat_password.setImageResource(R.drawable.show);
            }
            else
            {
                et_password.setTransformationMethod(new PasswordTransformationMethod());
                et_repeat_password.setTransformationMethod(new PasswordTransformationMethod());
                iv_bt_show_password.setImageResource(R.drawable.hidden);
                iv_bt_show_repeat_password.setImageResource(R.drawable.hidden);
            }
            et_password.setSelection(et_password.getText().length());
        });

        iv_bt_show_repeat_password.setOnClickListener(view ->
        {
            et_repeat_password.requestFocus();
            show_password=!show_password;
            if (show_password)
            {
                et_password.setTransformationMethod(null);
                et_repeat_password.setTransformationMethod(null);
                iv_bt_show_password.setImageResource(R.drawable.show);
                iv_bt_show_repeat_password.setImageResource(R.drawable.show);
            }
            else
            {
                et_password.setTransformationMethod(new PasswordTransformationMethod());
                et_repeat_password.setTransformationMethod(new PasswordTransformationMethod());
                iv_bt_show_password.setImageResource(R.drawable.hidden);
                iv_bt_show_repeat_password.setImageResource(R.drawable.hidden);
            }
            et_repeat_password.setSelection(et_repeat_password.getText().length());
        });

        RelativeLayout rl_user = findViewById(R.id.rl_user_login);
        rl_user.setVisibility(View.VISIBLE);
        rl_password = findViewById(R.id.rl_password_login);
        rl_password.setVisibility(View.VISIBLE);
        rl_repeat_password = findViewById(R.id.rl_repeat_password_login);
        rl_repeat_password.setVisibility(View.INVISIBLE);
        rl_code = findViewById(R.id.rl_code_login);
        rl_code.setVisibility(View.INVISIBLE);


        SensorManager sensor_manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor_manager.registerListener(this, sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        sensor_manager.registerListener(this, sensor_manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);


        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                if (!checkBack())
                {
                    switch (screen)
                    {
                        case 0:
                            menu();
                            break;
                        case 1:
                            Animations.slideRightOut(tv_code,400);
                            Animations.slideLeftIn(rl_password,500);
                            Animations.slideLeftIn(tv_forgot,600);
                            tv_bt_login.setText(getString(R.string.log_in));
                            tv_register.setVisibility(View.VISIBLE);
                            et_user.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                            et_user.setOnEditorActionListener(null);
                            et_user.setNextFocusDownId(R.id.et_password_login);
                            et_user.setNextFocusLeftId(R.id.et_password_login);
                            et_user.setNextFocusRightId(R.id.et_password_login);
                            et_user.setNextFocusUpId(R.id.et_password_login);
                            et_user.setNextFocusForwardId(R.id.et_password_login);
                            screen = 0;
                            break;
                        case 2:
                            Animations.slideRightOut(rl_code,500);
                            Animations.slideLeftIn(tv_code,600);
                            tv_bt_login.setText(getString(R.string.confirm));
                            tv_register.setVisibility(View.INVISIBLE);
                            et_code.setImeOptions(EditorInfo.IME_ACTION_SEND);
                            et_code.setOnEditorActionListener((v, actionId, event) -> {
                                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_GO
                                        || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND)
                                {
                                    rl_bt_login.performClick();
                                    return true;
                                }
                                return false;
                            });
                            screen = 1;
                            break;
                        case 3:
                            Animations.slideRightOut(rl_repeat_password,500);
                            Animations.slideLeftIn(tv_forgot,600);
                            tv_register.setVisibility(View.VISIBLE);
                            tv_bt_login.setText(getString(R.string.log_in));
                            et_password.setImeOptions(EditorInfo.IME_ACTION_GO);
                            screen = 0;
                            break;
                    }
                }
            }
        });
    }

    float[] gravity;
    float[] geomagnetic;
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            gravity = Util.applyLowPassFilter(event.values.clone(), gravity);
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            geomagnetic = Util.applyLowPassFilter(event.values.clone(), geomagnetic);
        if (gravity != null && geomagnetic != null)
        {
            float[] R = new float[9];
            float[] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic);
            if (success)
            {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R, orientation);
                float roll = orientation[2];
                float x = (float)(roll*Util.convertDpToPixel(800,LoginActivity.this)/3.14)-Util.convertDpToPixel(50,LoginActivity.this);

                iv_glare.setTranslationX(x);
                iv_glare.setVisibility(View.VISIBLE);
            }
            else
            {
                iv_glare.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            iv_glare.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {

    }


    private void recovery1()
    {
        screen = 1;
        Animations.slideLeftOut(tv_forgot,400);
        Animations.slideLeftOut(rl_password,500);
        Animations.slideRightIn(tv_code,600);
        tv_bt_login.setText(getString(R.string.confirm));
        tv_register.setVisibility(View.INVISIBLE);
        et_user.setImeOptions(EditorInfo.IME_ACTION_GO);
        et_user.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_GO
                    || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND)
            {
                rl_bt_login.performClick();
                return true;
            }
            return false;
        });
    }


    private void recovery2()
    {
        screen = 2;
        Animations.slideLeftOut(tv_code,400);
        Animations.slideRightIn(rl_code,500);
        tv_bt_login.setText(getString(R.string.confirm));
        tv_register.setVisibility(View.INVISIBLE);
        et_user.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_user.setOnEditorActionListener(null);
        et_user.setNextFocusDownId(R.id.etCodeLogin);
        et_user.setNextFocusLeftId(R.id.etCodeLogin);
        et_user.setNextFocusRightId(R.id.etCodeLogin);
        et_user.setNextFocusUpId(R.id.etCodeLogin);
        et_user.setNextFocusForwardId(R.id.etCodeLogin);
    }

    private void register()
    {
        screen = 3;
        Animations.slideLeftOut(tv_forgot,400);
        Animations.slideRightIn(rl_repeat_password,500);
        tv_bt_login.setText(getString(R.string.confirm));
        tv_register.setVisibility(View.INVISIBLE);
        et_password.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_password.setNextFocusDownId(R.id.et_repeat_password_login);
        et_password.setNextFocusLeftId(R.id.et_repeat_password_login);
        et_password.setNextFocusRightId(R.id.et_repeat_password_login);
        et_password.setNextFocusUpId(R.id.et_repeat_password_login);
        et_password.setNextFocusForwardId(R.id.et_repeat_password_login);
    }
}
