package com.java.uidemo.demos;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;

import com.java.uidemo.R;
import com.java.uidemo.model.DropdownItem;
import com.java.uidemo.util.Constants;
import com.java.uidemo.util.CustomFocusChangeListener;
import com.java.uidemo.util.DemoActivity;
import com.java.uidemo.util.Util;
import com.java.uidemo.view.DropdownButton;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This {@link DemoActivity} displays an interface that allows the user to scan and generate barcodes and QR codes.
 */
public class QRActivity extends DemoActivity
{
    private EditText et_scan_result, et_generate;
    private RelativeLayout rl_generated_qr;
    private ImageView iv_bt_copy, iv_bt_web;
    private String selected_type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        initDemo();
        final ImageView iv_bt_scan = findViewById(R.id.iv_bt_scan_qr);
        iv_bt_copy = findViewById(R.id.iv_bt_copy_qr);
        iv_bt_web = findViewById(R.id.iv_bt_web_qr);

        selected_type = "QR_CODE";

        rl_generated_qr = findViewById(R.id.rl_generated_qr);
        et_scan_result = findViewById(R.id.et_scan_result_qr);
        et_scan_result.setOnFocusChangeListener(new CustomFocusChangeListener());
        et_scan_result.setTypeface(demo.getTf_monserrat_light());
        iv_bt_scan.setOnClickListener(view ->
        {
            Util.hideKeyboard(QRActivity.this);
            clearAllFocus();
            et_scan_result.setText("");
            iv_bt_web.setVisibility(View.GONE);
            iv_bt_copy.setVisibility(View.GONE);
            IntentIntegrator integrator = new IntentIntegrator(QRActivity.this);
            integrator.setDesiredBarcodeFormats(Collections.singleton(selected_type));
            integrator.setBeepEnabled(false);
            integrator.setOrientationLocked(false);
            integrator.initiateScan();
        });
        iv_bt_web.setVisibility(View.GONE);
        iv_bt_copy.setVisibility(View.GONE);

        iv_bt_copy.setOnClickListener(view ->
        {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("BARCODE", et_scan_result.getText().toString());
            clipboard.setPrimaryClip(clip);
        });

        final ImageView iv_generated_qr = findViewById(R.id.iv_generated_qr);
        iv_generated_qr.setVisibility(View.INVISIBLE);
        final TextView tv_generated_qr = findViewById(R.id.tv_generated_qr);
        final ImageView iv_bt_download_generated_qr = findViewById(R.id.iv_bt_download_generated_qr);
        iv_bt_download_generated_qr.setOnClickListener(view ->
        {
            rl_generated_qr.setDrawingCacheEnabled(true);
            rl_generated_qr.buildDrawingCache();
            Bitmap bm = rl_generated_qr.getDrawingCache();
            Util.saveImage(bm,"BC"+System.currentTimeMillis(),QRActivity.this);
        });
        iv_bt_download_generated_qr.setVisibility(View.INVISIBLE);
        tv_generated_qr.setTypeface(demo.getTf_monserrat_light());
        tv_generated_qr.setVisibility(View.INVISIBLE);
        et_generate = findViewById(R.id.et_generate_qr);
        et_generate.setTypeface(demo.getTf_monserrat_light());
        et_generate.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                iv_generated_qr.setVisibility(View.INVISIBLE);
                tv_generated_qr.setVisibility(View.INVISIBLE);
                iv_bt_download_generated_qr.setVisibility(View.INVISIBLE);
            }
        });
        final ImageView iv_bt_generate = findViewById(R.id.iv_bt_generate_qr);
        iv_bt_generate.setOnClickListener(view ->
        {
            Util.hideKeyboard(QRActivity.this);
            clearAllFocus();
            String origin_code = et_generate.getText().toString();
            String parsed_code = "";
            switch (selected_type)
            {
                case "UPC_A":
                    if (origin_code.length()>12)
                        origin_code = origin_code.substring(0,12);
                    while (origin_code.length()<11)
                        origin_code = "0"+origin_code;
                    parsed_code = origin_code.toUpperCase().replaceAll("[^0-9]", "");
                    if (!parsed_code.isEmpty())
                    {
                        try
                        {
                            iv_generated_qr.setImageBitmap(Util.encodeAsBitmap(parsed_code, BarcodeFormat.UPC_A));
                            iv_generated_qr.setVisibility(View.VISIBLE);
                            tv_generated_qr.setText(parsed_code);
                            tv_generated_qr.setVisibility(View.VISIBLE);
                            iv_bt_download_generated_qr.setVisibility(View.VISIBLE);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "UPC_E":
                    if (origin_code.length()>8)
                        origin_code = origin_code.substring(0,8);
                    while (origin_code.length()<8)
                        origin_code = "0"+origin_code;
                    parsed_code = origin_code.toUpperCase().replaceAll("[^0-9]", "");
                    if (!parsed_code.isEmpty())
                    {
                        try
                        {
                            iv_generated_qr.setImageBitmap(Util.encodeAsBitmap(parsed_code, BarcodeFormat.UPC_E));
                            iv_generated_qr.setVisibility(View.VISIBLE);
                            tv_generated_qr.setText(parsed_code);
                            tv_generated_qr.setVisibility(View.VISIBLE);
                            iv_bt_download_generated_qr.setVisibility(View.VISIBLE);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "EAN_8":
                    if (origin_code.length()>8)
                        origin_code = origin_code.substring(0,8);
                    while (origin_code.length()<8)
                        origin_code = "0"+origin_code;
                    parsed_code = origin_code.toUpperCase().replaceAll("[^0-9]", "");
                    if (!parsed_code.isEmpty())
                    {
                        try
                        {
                            iv_generated_qr.setImageBitmap(Util.encodeAsBitmap(parsed_code, BarcodeFormat.EAN_8));
                            iv_generated_qr.setVisibility(View.VISIBLE);
                            tv_generated_qr.setText(parsed_code);
                            tv_generated_qr.setVisibility(View.VISIBLE);
                            iv_bt_download_generated_qr.setVisibility(View.VISIBLE);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "EAN_13":
                    if (origin_code.length()>13)
                        origin_code = origin_code.substring(0,13);
                    while (origin_code.length()<13)
                        origin_code = "0"+origin_code;
                    parsed_code = origin_code.toUpperCase().replaceAll("[^0-9]", "");
                    if (!parsed_code.isEmpty())
                    {
                        try
                        {
                            iv_generated_qr.setImageBitmap(Util.encodeAsBitmap(parsed_code, BarcodeFormat.EAN_13));
                            iv_generated_qr.setVisibility(View.VISIBLE);
                            tv_generated_qr.setText(parsed_code);
                            tv_generated_qr.setVisibility(View.VISIBLE);
                            iv_bt_download_generated_qr.setVisibility(View.VISIBLE);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "CODE_39":
                    if (origin_code.length()>12)
                        origin_code = origin_code.substring(0,12);
                    parsed_code = origin_code.toUpperCase().replaceAll("[^0-9A-Z.\\-$/+%* ]", "");
                    if (!parsed_code.isEmpty())
                    {
                        try
                        {
                            iv_generated_qr.setImageBitmap(Util.encodeAsBitmap(parsed_code, BarcodeFormat.CODE_39));
                            iv_generated_qr.setVisibility(View.VISIBLE);
                            tv_generated_qr.setText(parsed_code);
                            tv_generated_qr.setVisibility(View.VISIBLE);
                            iv_bt_download_generated_qr.setVisibility(View.VISIBLE);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "CODE_93":
                    if (origin_code.length()>12)
                        origin_code = origin_code.substring(0,12);
                    parsed_code = origin_code.toUpperCase().replaceAll("[^0-9A-Z.\\-$/+% ]", "");
                    if (!parsed_code.isEmpty())
                    {
                        try
                        {
                            iv_generated_qr.setImageBitmap(Util.encodeAsBitmap(parsed_code, BarcodeFormat.CODE_93));
                            iv_generated_qr.setVisibility(View.VISIBLE);
                            tv_generated_qr.setText(parsed_code);
                            tv_generated_qr.setVisibility(View.VISIBLE);
                            iv_bt_download_generated_qr.setVisibility(View.VISIBLE);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "CODE_128":
                    if (origin_code.length()>12)
                        origin_code = origin_code.substring(0,12);
                    if (!origin_code.isEmpty())
                    {
                        try
                        {
                            iv_generated_qr.setImageBitmap(Util.encodeAsBitmap(parsed_code, BarcodeFormat.CODE_128));
                            iv_generated_qr.setVisibility(View.VISIBLE);
                            tv_generated_qr.setText(parsed_code);
                            tv_generated_qr.setVisibility(View.VISIBLE);
                            iv_bt_download_generated_qr.setVisibility(View.VISIBLE);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "QR_CODE":
                    if (!origin_code.isEmpty())
                    {
                        try
                        {
                            iv_generated_qr.setImageBitmap(Util.encodeAsBitmap(et_generate.getText().toString(), BarcodeFormat.QR_CODE));
                            iv_generated_qr.setVisibility(View.VISIBLE);
                            tv_generated_qr.setText(et_generate.getText().toString().trim());
                            tv_generated_qr.setVisibility(View.INVISIBLE);
                            iv_bt_download_generated_qr.setVisibility(View.VISIBLE);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        });




        final DropdownButton db_dropdown = findViewById(R.id.db_dropdown_qr);
        final ImageView iv_dropdown = findViewById(R.id.iv_dropdown_qr);
        final TextView tv_dropdown = findViewById(R.id.tv_dropdown_qr);
        tv_dropdown.setTypeface(demo.getTf_monserrat_light());

        ArrayList<DropdownItem> items = new ArrayList<>();
        for (int i=0; i<Constants.BARCODE_TYPES.length; i++)
        {
            DropdownItem di = new DropdownItem();
            di.setId(i);
            di.setText(Constants.BARCODE_TYPES[i].replace("_"," "));
            if (i<Constants.QR_CODE)
                di.setIcon_resource(R.drawable.barcode);
            else
                di.setIcon_resource(R.drawable.qr);
            items.add(di);
        }

        db_dropdown.setup(iv_dropdown, tv_dropdown, items, Constants.QR_CODE, getString(R.string.select_a_barcode_type), rl_root, new DropdownButton.DropdownButtonEventListener()
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
                selected_type = Constants.BARCODE_TYPES[item_selected.getId()];
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (intent != null)
        {
            String scanContent = intent.getStringExtra("SCAN_RESULT");
            if (scanContent!=null)
            {
                et_scan_result.setText(scanContent);
                iv_bt_copy.setVisibility(View.VISIBLE);
                if (scanContent.startsWith("http")||scanContent.startsWith("www")||scanContent.contains(".com"))
                {
                    String url = scanContent;
                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                        url = "http://" + url;
                    String finalUrl = url;
                    iv_bt_web.setVisibility(View.VISIBLE);
                    iv_bt_web.setOnClickListener(view ->
                    {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl));
                        startActivity(browserIntent);
                    });
                }
            }
        }
        super.onActivityResult(requestCode,resultCode,intent);
    }
}
