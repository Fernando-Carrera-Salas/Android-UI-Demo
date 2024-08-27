package com.java.uidemo.demos;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.java.uidemo.R;
import com.java.uidemo.application.UIDemo;
import com.java.uidemo.util.Animations;
import com.java.uidemo.util.DemoActivity;
import com.java.uidemo.view.WildText;

/**
 * This {@link DemoActivity} just contains my contact information.
 * </p><p>
 * The only element of note is the {@link WildText} which uses several animations from <a href="https://github.com/daimajia/AndroidViewAnimations">Daimajia's library</a>.
 */
public class InfoActivity extends DemoActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final UIDemo demo = (UIDemo) getApplicationContext();
        setContentView(R.layout.activity_info);
        initDemo();

        final RelativeLayout rl_image = findViewById(R.id.rl_image_info);

        final WildText wt_name = findViewById(R.id.wt_name_info);
        wt_name.setText("Fernando  Carrera  Salas",demo.getTf_ashley_semibold());
        wt_name.startAnim();


        final View v_fade_in = findViewById(R.id.v_fade_info);
        v_fade_in.setVisibility(View.VISIBLE);
        Handler h = new Handler(Looper.getMainLooper());
        h.postDelayed(() ->
        {
            Animations.fadeIn(rl_image,1000,null);
            h.postDelayed(() -> Animations.fadeOut(v_fade_in,1000,null),1000);
        },1000);

        final TextView tvVersionLogin = findViewById(R.id.tv_version_info);
        tvVersionLogin.setTypeface(demo.getTf_monserrat_light());
        try
        {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            tvVersionLogin.setText(pInfo.versionName);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            tvVersionLogin.setText("");
        }

        final ImageView iv_bt_mail = findViewById(R.id.iv_bt_mail_info);
        iv_bt_mail.setOnClickListener(view -> launch_mail());

        final ImageView iv_bt_linkedin = findViewById(R.id.iv_bt_linkedin_info);
        iv_bt_linkedin.setOnClickListener(view -> launch_linkedin());

        final ImageView iv_bt_github = findViewById(R.id.iv_bt_github_info);
        iv_bt_github.setOnClickListener(view -> launch_github());
    }


    private void launch_mail()
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {getString(R.string.contact_email)});
        startActivity(Intent.createChooser(intent, ""));
    }

    private void launch_linkedin()
    {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.contact_linkedin))));
    }

    private void launch_github()
    {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.contact_github))));
    }
}
