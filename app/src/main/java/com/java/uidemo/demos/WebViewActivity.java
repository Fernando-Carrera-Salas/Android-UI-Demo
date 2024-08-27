package com.java.uidemo.demos;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.java.uidemo.R;
import com.java.uidemo.util.CustomFocusChangeListener;
import com.java.uidemo.util.DemoActivity;
import com.java.uidemo.util.Util;

/**
 * This {@link DemoActivity} displays a {@link WebView} with a local website.
 * </p><p>
 * The app injects a {@link JavascriptInterface} to the WebView, allowing the JavaScript code to communicate with the Java code.
 * </p><p>
 * The local website can be found in /assets/websample/.
 * </p>
 */
public class WebViewActivity extends DemoActivity
{
    private WebView wv_webview;
    private EditText et_send;
    private RelativeLayout rl_bt_send;
    private TextView tv_receive;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        wv_webview = findViewById(R.id.wv_webview);
        et_send = findViewById(R.id.et_send_webview);
        et_send.setOnFocusChangeListener(new CustomFocusChangeListener());
        et_send.setTypeface(demo.getTf_monserrat_light());
        et_send.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_GO
                    || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND)
            {
                rl_bt_send.performClick();
                return true;
            }
            return false;
        });
        rl_bt_send = findViewById(R.id.rl_bt_send_webview);
        rl_bt_send.setOnClickListener(view ->
        {
            Util.hideKeyboard(WebViewActivity.this);
            clearAllFocus();

            /**
             * JavaScript method that displays the received message in the webpage.
             */
            wv_webview.evaluateJavascript("receiveFromAndroid('"+et_send.getText().toString()+"')",null);
        });
        final TextView tv_bt_send = findViewById(R.id.tv_bt_send_webview);
        tv_bt_send.setTypeface(demo.getTf_ashley_semibold());
        tv_receive = findViewById(R.id.tv_receive_webview);
        tv_receive.setVisibility(View.INVISIBLE);
        tv_receive.setTypeface(demo.getTf_monserrat_light());
        initDemo();
        loadWebView();
    }




    @SuppressLint("SetJavaScriptEnabled")
    public void loadWebView()
    {
        wv_webview.getSettings().setJavaScriptEnabled(true);
        wv_webview.getSettings().setDomStorageEnabled(true);
        wv_webview.getSettings().setLoadWithOverviewMode(false);
        wv_webview.getSettings().setAllowFileAccess(true);
        wv_webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv_webview.getSettings().setSupportMultipleWindows(true);
        wv_webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        wv_webview.getSettings().setLoadWithOverviewMode(true);
        wv_webview.getSettings().setUseWideViewPort(true);
        wv_webview.getSettings().setMediaPlaybackRequiresUserGesture(false);
        wv_webview.setWebChromeClient(new WebChromeClient());
        wv_webview.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                /**
                 * JavaScript method that changes the labels to the selected language.
                 */
                wv_webview.evaluateJavascript("setLanguage('"+getString(R.string.default_language)+"')",null);
                super.onPageFinished(wv_webview, url);
            }

        });
        Handler h = new Handler(Looper.getMainLooper());
        h.postDelayed(() -> wv_webview.loadUrl("file:///android_asset/websample/index.html"), 100);
        wv_webview.setOnLongClickListener(view -> true);
        wv_webview.setLongClickable(false);
        wv_webview.setHapticFeedbackEnabled(false);
        wv_webview.addJavascriptInterface(new WebViewJavaScriptInterface(), "app");
    }


    private class WebViewJavaScriptInterface
    {
        /**
         * Injected JavaScript method that is received in the Java code, displaying the received message in the app.
         */
        @JavascriptInterface
        public void receiveFromJavaScript(final String message)
        {
            Handler h = new Handler(Looper.getMainLooper());
            h.post(() ->
            {
                Util.hideKeyboard(WebViewActivity.this);
                clearAllFocus();
                tv_receive.setVisibility(View.VISIBLE);
                tv_receive.setText(getString(R.string.received_from_webpage)+message);
            });
        }
    }
}
