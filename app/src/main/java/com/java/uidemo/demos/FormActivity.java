package com.java.uidemo.demos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.java.uidemo.R;
import com.java.uidemo.model.DropdownItem;
import com.java.uidemo.util.Animations;
import com.java.uidemo.util.DemoActivity;
import com.java.uidemo.util.LinkTouchMovementMethod;
import com.java.uidemo.util.Util;
import com.java.uidemo.view.DrawSignature;
import com.java.uidemo.view.DropdownButton;
import com.java.uidemo.view.ProgressBarButton;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This {@link DemoActivity} displays several standard form elements.
 * <ul>
 *     <li>A button that allows the user to select an image or take a photo with their camera. Using <a href="https://github.com/Dhaval2404/ImagePicker">Dhaval2404's ImagePicker library.</a></li>
 *     <li>A text field.</li>
 *     <li>A date selector, based on <a href="https://github.com/ShawnLin013/NumberPicker">ShawLin013's NumberPicker library.</a></li>
 *     <li>A set of custom radio buttons.</li>
 *     <li>A custom checkbox with dynamic links within its text.</li>
 *     <li>A {@link ProgressBarButton} that displays a {@link DrawSignature} canvas.</li>
 * </ul>
 */
public class FormActivity extends DemoActivity
{
    private RelativeLayout rl_signature, rl_terms;

    private DrawSignature signature;

    private NumberPicker np_day, np_month, np_year;

    private boolean checked_terms;

    private ImageView iv_picture, iv_bt_picture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        initDemo();

        final EditText et_text_field = findViewById(R.id.et_text_field_form);
        et_text_field.setTypeface(demo.getTf_monserrat_light());

        rl_signature = findViewById(R.id.rl_signature_form);
        rl_signature.setVisibility(View.INVISIBLE);

        rl_terms = findViewById(R.id.rl_terms_form);
        rl_terms.setVisibility(View.INVISIBLE);

        final TextView tv_name_form = findViewById(R.id.tv_name_form);
        final TextView tv_date_form = findViewById(R.id.tv_date_of_birth_form);
        final TextView tv_country_form = findViewById(R.id.tv_country_form);
        final TextView tv_gender_form = findViewById(R.id.tv_gender_form);
        tv_name_form.setTypeface(demo.getTf_monserrat_light());
        tv_date_form.setTypeface(demo.getTf_monserrat_light());
        tv_country_form.setTypeface(demo.getTf_monserrat_light());
        tv_gender_form.setTypeface(demo.getTf_monserrat_light());

        final TextView tv_bt_signature = findViewById(R.id.tv_bt_signature_form);
        tv_bt_signature.setTypeface(demo.getTf_ashley_semibold());
        final TextView tv_bt_cancel_signature = findViewById(R.id.tv_bt_cancel_signature_form);
        tv_bt_cancel_signature.setTypeface(demo.getTf_ashley_semibold());
        final TextView tv_bt_confirm_signature = findViewById(R.id.tv_bt_confirm_signature_form);
        tv_bt_confirm_signature.setTypeface(demo.getTf_ashley_semibold());
        final TextView tv_signature = findViewById(R.id.tv_signature_form);
        tv_signature.setTypeface(demo.getTf_monserrat_light());

        final ProgressBarButton pbb_signature = findViewById(R.id.pbb_signature_form);
        pbb_signature.setup(findViewById(R.id.pb_bt_signature_form),findViewById(R.id.rl_bt_signature_form),750);
        pbb_signature.setListener(new ProgressBarButton.ProgressBarButtonEventListener()
        {
            @Override
            public void onProgressUpdate(int progress)
            {

            }

            @Override
            public void onPress()
            {

            }

            @Override
            public void onCancel()
            {

            }

            @Override
            public void onEnd()
            {
                displaySignature();
            }
        });

        signature = findViewById(R.id.ds_signature_form);

        final RelativeLayout rl_bt_cancel_signature = findViewById(R.id.rl_bt_cancel_signature_form);
        rl_bt_cancel_signature.setOnClickListener(view ->
        {
            signature.clear();
            getOnBackPressedDispatcher().onBackPressed();
        });
        final RelativeLayout rl_bt_confirm_signature = findViewById(R.id.rl_bt_confirm_signature_form);
        rl_bt_confirm_signature.setOnClickListener(view ->
        {
            signature.setDrawingCacheEnabled(true);
            signature.buildDrawingCache();
            Bitmap bm = signature.getDrawingCache();
            Util.saveImage(bm,"SIG"+System.currentTimeMillis(),FormActivity.this);
            getOnBackPressedDispatcher().onBackPressed();
            signature.clear();
        });

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                if (!checkBack())
                {
                    if (rl_terms.getVisibility()==View.VISIBLE)
                    {
                        Animations.slideDownOut(rl_terms,500);
                    }
                    else
                    {
                        if (rl_signature.getVisibility()==View.VISIBLE)
                        {
                            Animations.slideDownOut(rl_signature,500);
                        }
                        else
                        {
                            menu();
                        }
                    }
                }
            }
        });



        final DropdownButton db_dropdown = findViewById(R.id.db_dropdown_form);
        final TextView tv_dropdown = findViewById(R.id.tv_dropdown_form);
        tv_dropdown.setTypeface(demo.getTf_monserrat_light());

        String[] countries = getResources().getStringArray(R.array.countries);
        ArrayList<DropdownItem> items = new ArrayList<>();
        for (int i=0; i<countries.length; i++)
        {
            DropdownItem di = new DropdownItem();
            di.setId(i);
            di.setText(countries[i]);
            di.setIcon_resource(0);
            items.add(di);
        }

        Util.sortDropdown(items);

        db_dropdown.setup(null, tv_dropdown, items, -1, getString(R.string.select_a_country), rl_root, new DropdownButton.DropdownButtonEventListener()
        {
            @Override
            public void onOpen()
            {

            }

            @Override
            public void onClose()
            {

            }

            @Override
            public void onSelected(DropdownItem item_selected)
            {

            }
        });

        final RelativeLayout rl_bt_picture = findViewById(R.id.rl_bt_picture_form);
        rl_bt_picture.setOnClickListener(view ->
        {
            clearAllFocus();
            Util.hideKeyboard(FormActivity.this);
            ImagePicker.with(FormActivity.this)
                    .cropSquare()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });
        iv_picture = findViewById(R.id.iv_picture_form);
        iv_bt_picture = findViewById(R.id.iv_bt_picture_form);



        int current_day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int current_month = Calendar.getInstance().get(Calendar.MONTH)+1;
        int current_year = Calendar.getInstance().get(Calendar.YEAR);
        int first_year = 1900;


        String[] days = new String[31];
        String[] months = getResources().getStringArray(R.array.months);
        String[] years = new String[current_year-first_year];

        for (int i=0; i<days.length; i++)
        {
            days[i] = String.valueOf(i+1);
        }

        for (int i=0; i<years.length; i++)
        {
            years[i] = String.valueOf(first_year+i+1);
        }


        if (getString(R.string.default_language).equals("EN"))
        {
            np_month = findViewById(R.id.np_1_form);
            np_day = findViewById(R.id.np_2_form);
        }
        else
        {
            np_day = findViewById(R.id.np_1_form);
            np_month = findViewById(R.id.np_2_form);
        }



        np_day.setDisplayedValues(days);
        np_day.setTypeface(demo.getTf_monserrat_light());
        np_day.setSelectedTypeface(demo.getTf_ashley_semibold());
        np_day.setWrapSelectorWheel(false);
        checkDay(current_day,current_month,current_year);


        np_year = findViewById(R.id.np_3_form);
        np_year.setDisplayedValues(years);
        np_year.setTypeface(demo.getTf_monserrat_light());
        np_year.setSelectedTypeface(demo.getTf_ashley_semibold());
        np_year.setWrapSelectorWheel(false);
        np_year.setMaxValue(current_year-first_year);
        np_year.setValue(current_year-first_year);
        np_year.setOnValueChangedListener((picker, oldVal, newVal) -> checkDay(np_day.getValue(),np_month.getValue(),newVal));


        np_month.setDisplayedValues(months);
        np_month.setTypeface(demo.getTf_monserrat_light());
        np_month.setSelectedTypeface(demo.getTf_ashley_semibold());
        np_month.setWrapSelectorWheel(false);
        np_month.setMaxValue(12);
        np_month.setValue(current_month);
        np_month.setOnValueChangedListener((picker, oldVal, newVal) -> checkDay(np_day.getValue(),newVal,np_year.getValue()));


        LinearLayout ll_rb_gender_1 = findViewById(R.id.ll_rb_gender_1_form);
        LinearLayout ll_rb_gender_2 = findViewById(R.id.ll_rb_gender_2_form);
        LinearLayout ll_rb_gender_3 = findViewById(R.id.ll_rb_gender_3_form);
        final ImageView iv_rb_gender_1 = findViewById(R.id.iv_rb_gender_1_form);
        final ImageView iv_rb_gender_2 = findViewById(R.id.iv_rb_gender_2_form);
        final ImageView iv_rb_gender_3 = findViewById(R.id.iv_rb_gender_3_form);
        final TextView tv_rb_gender_1 = findViewById(R.id.tv_rb_gender_1_form);
        final TextView tv_rb_gender_2 = findViewById(R.id.tv_rb_gender_2_form);
        final TextView tv_rb_gender_3 = findViewById(R.id.tv_rb_gender_3_form);

        tv_rb_gender_1.setTypeface(demo.getTf_monserrat_light());
        tv_rb_gender_2.setTypeface(demo.getTf_monserrat_light());
        tv_rb_gender_3.setTypeface(demo.getTf_monserrat_light());

        ll_rb_gender_1.setOnClickListener(view ->
        {
            iv_rb_gender_1.setImageResource(R.drawable.bg_radio_button_on);
            iv_rb_gender_2.setImageResource(R.drawable.bg_radio_button_off);
            iv_rb_gender_3.setImageResource(R.drawable.bg_radio_button_off);
        });
        ll_rb_gender_2.setOnClickListener(view ->
        {
            iv_rb_gender_1.setImageResource(R.drawable.bg_radio_button_off);
            iv_rb_gender_2.setImageResource(R.drawable.bg_radio_button_on);
            iv_rb_gender_3.setImageResource(R.drawable.bg_radio_button_off);
        });
        ll_rb_gender_3.setOnClickListener(view ->
        {
            iv_rb_gender_1.setImageResource(R.drawable.bg_radio_button_off);
            iv_rb_gender_2.setImageResource(R.drawable.bg_radio_button_off);
            iv_rb_gender_3.setImageResource(R.drawable.bg_radio_button_on);
        });

        final LinearLayout ll_cb_terms = findViewById(R.id.ll_cb_terms_form);
        final ImageView iv_cb_terms = findViewById(R.id.iv_cb_terms_form);
        final TextView tv_cb_terms = findViewById(R.id.tv_cb_terms_form);


        tv_cb_terms.setTypeface(demo.getTf_monserrat_light());
        String terms_string = getString(R.string.terms_1)+" "+getString(R.string.terms_2)+" "+getString(R.string.terms_3)+" "+getString(R.string.terms_4);
        tv_cb_terms.setText(terms_string);
        tv_cb_terms.setMovementMethod(new LinkTouchMovementMethod());

        List<Pair<String, View.OnClickListener>> links = new ArrayList<>();
        links.add(new Pair<>(getString(R.string.terms_2), v -> displayTermsAndConditions(0)));
        links.add(new Pair<>(getString(R.string.terms_4), v -> displayTermsAndConditions(1)));
        Util.makeLinks(tv_cb_terms,links,getColor(R.color.ui_demo_purple),getColor(R.color.dark_gray));


        ll_cb_terms.setOnClickListener(view ->
        {
            if (checked_terms)
            {
                checked_terms = false;
                iv_cb_terms.setImageResource(R.drawable.bg_checkbox_off);
            }
            else
            {
                checked_terms = true;
                iv_cb_terms.setImageResource(R.drawable.bg_checkbox_on);
            }
        });

    }


    private void displaySignature()
    {
        Animations.slideUpIn(rl_signature,900);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            if (data!=null)
            {
                Uri uri = data.getData();
                if (uri!=null)
                {
                    try
                    {
                        Glide.with(FormActivity.this).load(uri).into(iv_picture);
                        iv_bt_picture.setVisibility(View.GONE);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void checkDay(int day, int month, int year)
    {
        int max = 31;
        if (month==4||month==6||month==9||month==11)
            max = 30;
        else if (month == 2)
            if (Util.isLeapYear(year))
                max = 29;
            else
                max = 28;

        if (day>max)
            day = max;

        np_day.setMaxValue(max);
        np_day.setValue(day);
    }

    private void displayTermsAndConditions(int type)
    {
        final TextView tv_terms_title = findViewById(R.id.tv_terms_title_form);
        switch (type)
        {
            case 0:
                tv_terms_title.setText(getString(R.string.terms_2));
                break;
            case 1:
                tv_terms_title.setText(getString(R.string.terms_4));
                break;
        }
        tv_terms_title.setTypeface(demo.getTf_monserrat_light());
        final TextView tv_terms = findViewById(R.id.tv_terms_form);
        tv_terms.setTypeface(demo.getTf_monserrat_light());
        Animations.slideUpIn(rl_terms,900);
    }

}
