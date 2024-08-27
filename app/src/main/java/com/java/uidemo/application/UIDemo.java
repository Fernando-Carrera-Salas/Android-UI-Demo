package com.java.uidemo.application;

import android.app.Application;
import android.graphics.Typeface;

import androidx.room.Room;

import com.java.uidemo.database.DemoDatabase;
import com.java.uidemo.demos.ARActivity;
import com.java.uidemo.R;
import com.java.uidemo.demos.AnimationActivity;
import com.java.uidemo.demos.ChartActivity;
import com.java.uidemo.demos.ChatActivity;
import com.java.uidemo.demos.FormActivity;
import com.java.uidemo.demos.InfoActivity;
import com.java.uidemo.demos.LanguageActivity;
import com.java.uidemo.demos.LoginActivity;
import com.java.uidemo.demos.MapActivity;
import com.java.uidemo.demos.QRActivity;
import com.java.uidemo.demos.ViewPagerActivity;
import com.java.uidemo.demos.WebViewActivity;
import com.java.uidemo.model.DemoLanguage;
import com.java.uidemo.model.DemoNotification;
import com.java.uidemo.model.DemoScreen;
import com.java.uidemo.util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
/**
 * A class extending Application allows the app to keep elements in memory for the entire life cycle of the app.
 * Activities and services can access these through getApplicationContext.
 */
public class UIDemo extends Application
{
    private boolean b_splash;
    private Typeface tf_monserrat_light, tf_ashley_semibold;
    private DemoScreen demo_screen;
    private ArrayList<DemoScreen> demo_screens;
    private ArrayList<DemoLanguage> languages;
    private ArrayList<DemoNotification> notifications;

    DemoDatabase database;

    @Override
    public void onCreate()
    {
        super.onCreate();
        b_splash = false;
        database = Room.databaseBuilder(getApplicationContext(), DemoDatabase.class, "demo-database").allowMainThreadQueries().build();
        tf_monserrat_light = Typeface.createFromAsset(getAssets(),"font/monserrat-light.ttf");
        tf_ashley_semibold = Typeface.createFromAsset(getAssets(),"font/ashley-semibold.otf");
        demo_screen = null;
        initDemoScreens();
        initLanguages();
        initNotifications();

    }


    public boolean isB_splash() {
        return b_splash;
    }

    public void setB_splash(boolean b_splash) {
        this.b_splash = b_splash;
    }

    public Typeface getTf_monserrat_light() {
        return tf_monserrat_light;
    }

    public Typeface getTf_ashley_semibold() {
        return tf_ashley_semibold;
    }

    public ArrayList<DemoScreen> getDemo_screens()
    {
        return demo_screens;
    }

    private void initDemoScreens()
    {
        demo_screens = new ArrayList<>();

        DemoScreen language_selection = new DemoScreen();
        language_selection.setName_resource(R.string.language_selection);
        language_selection.setClass_reference(LanguageActivity.class);
        language_selection.setColor_resource(R.color.ui_demo_green);
        language_selection.setIcon_resource(R.drawable.translation);
        language_selection.setHelp_resource(R.string.help_language);
        language_selection.setEnabled(true);
        demo_screens.add(language_selection);

        DemoScreen login_screen = new DemoScreen();
        login_screen.setName_resource(R.string.login_screen);
        login_screen.setClass_reference(LoginActivity.class);
        login_screen.setColor_resource(R.color.ui_demo_blue);
        login_screen.setIcon_resource(R.drawable.key);
        login_screen.setHelp_resource(R.string.help_login);
        login_screen.setEnabled(true);
        demo_screens.add(login_screen);

        DemoScreen paging = new DemoScreen();
        paging.setName_resource(R.string.paging);
        paging.setClass_reference(ViewPagerActivity.class);
        paging.setColor_resource(R.color.ui_demo_yellow);
        paging.setIcon_resource(R.drawable.open_book);
        paging.setHelp_resource(R.string.help_paging);
        paging.setEnabled(true);
        demo_screens.add(paging);

        DemoScreen form = new DemoScreen();
        form.setName_resource(R.string.form);
        form.setClass_reference(FormActivity.class);
        form.setColor_resource(R.color.ui_demo_purple);
        form.setIcon_resource(R.drawable.form);
        form.setHelp_resource(R.string.help_form);
        form.setEnabled(true);
        demo_screens.add(form);

        DemoScreen animations = new DemoScreen();
        animations.setName_resource(R.string.animations);
        animations.setClass_reference(AnimationActivity.class);
        animations.setColor_resource(R.color.ui_demo_red);
        animations.setIcon_resource(R.drawable.animation);
        animations.setHelp_resource(R.string.help_animations);
        animations.setEnabled(true);
        demo_screens.add(animations);

        DemoScreen chat = new DemoScreen();
        chat.setName_resource(R.string.chat);
        chat.setClass_reference(ChatActivity.class);
        chat.setColor_resource(R.color.ui_demo_green);
        chat.setIcon_resource(R.drawable.chat);
        chat.setHelp_resource(R.string.help_chat);
        chat.setEnabled(true);
        demo_screens.add(chat);

        DemoScreen embedded_browser = new DemoScreen();
        embedded_browser.setName_resource(R.string.embedded_browser);
        embedded_browser.setClass_reference(WebViewActivity.class);
        embedded_browser.setColor_resource(R.color.ui_demo_blue);
        embedded_browser.setIcon_resource(R.drawable.browser);
        embedded_browser.setHelp_resource(R.string.help_webview);
        embedded_browser.setEnabled(true);
        demo_screens.add(embedded_browser);

        DemoScreen google_maps = new DemoScreen();
        google_maps.setName_resource(R.string.google_maps);
        google_maps.setClass_reference(MapActivity.class);
        google_maps.setColor_resource(R.color.ui_demo_yellow);
        google_maps.setIcon_resource(R.drawable.map);
        google_maps.setHelp_resource(R.string.help_map);
        google_maps.setEnabled(true);
        demo_screens.add(google_maps);

        DemoScreen charts = new DemoScreen();
        charts.setName_resource(R.string.charts);
        charts.setClass_reference(ChartActivity.class);
        charts.setColor_resource(R.color.ui_demo_purple);
        charts.setIcon_resource(R.drawable.chart);
        charts.setHelp_resource(R.string.help_charts);
        charts.setEnabled(true);
        demo_screens.add(charts);

        DemoScreen barcodes = new DemoScreen();
        barcodes.setName_resource(R.string.barcodes);
        barcodes.setClass_reference(QRActivity.class);
        barcodes.setColor_resource(R.color.ui_demo_red);
        barcodes.setIcon_resource(R.drawable.qr_scan);
        barcodes.setHelp_resource(R.string.help_qr);
        barcodes.setEnabled(true);
        demo_screens.add(barcodes);

        DemoScreen augmented_reality = new DemoScreen();
        augmented_reality.setName_resource(R.string.augmented_reality);
        augmented_reality.setClass_reference(ARActivity.class);
        augmented_reality.setColor_resource(R.color.ui_demo_green);
        augmented_reality.setIcon_resource(R.drawable.augmented_reality);
        augmented_reality.setHelp_resource(R.string.help_ar);
        augmented_reality.setEnabled(true);
        demo_screens.add(augmented_reality);

        DemoScreen info = new DemoScreen();
        info.setName_resource(R.string.info);
        info.setClass_reference(InfoActivity.class);
        info.setColor_resource(R.color.ui_demo_blue);
        info.setIcon_resource(R.drawable.info);
        info.setHelp_resource(R.string.help_info);
        info.setEnabled(true);
        demo_screens.add(info);
    }

    private void initLanguages()
    {
        languages = new ArrayList<>();

        DemoLanguage english = new DemoLanguage();
        english.setIso_code("EN");
        english.setIcon_resource(R.drawable.uk);
        english.setName_resource(R.string.english);
        languages.add(english);

        DemoLanguage spanish = new DemoLanguage();
        spanish.setIso_code("ES");
        spanish.setIcon_resource(R.drawable.spain);
        spanish.setName_resource(R.string.spanish);
        languages.add(spanish);

        DemoLanguage french = new DemoLanguage();
        french.setIso_code("FR");
        french.setIcon_resource(R.drawable.france);
        french.setName_resource(R.string.french);
        languages.add(french);

        DemoLanguage german = new DemoLanguage();
        german.setIso_code("DE");
        german.setIcon_resource(R.drawable.germany);
        german.setName_resource(R.string.german);
        languages.add(german);

        DemoLanguage italian = new DemoLanguage();
        italian.setIso_code("IT");
        italian.setIcon_resource(R.drawable.italy);
        italian.setName_resource(R.string.italian);
        languages.add(italian);

        DemoLanguage portuguese = new DemoLanguage();
        portuguese.setIso_code("PT");
        portuguese.setIcon_resource(R.drawable.portugal);
        portuguese.setName_resource(R.string.portuguese);
        languages.add(portuguese);
    }

    private void initNotifications()
    {
        notifications = new ArrayList<>(database.notificationDAO().getAll());
        if (notifications.isEmpty())
        {
            for (int i=0; i<(new Random().nextInt(5)+3); i++)
            {
                String text_id = "000"+i;
                DemoNotification notification = new DemoNotification();
                notification.setId(i);
                notification.setDate(new Date());
                notification.setTitle(getString(R.string.notification_title)+" "+text_id);
                notification.setContent(Util.randomPieceOfLorem(this));
                notifications.add(0,notification);
                database.notificationDAO().insertAll(notification);
            }
        }
    }

    public void insertNotification(String title, String content)
    {
        DemoNotification notification = new DemoNotification();
        notification.setId(database.notificationDAO().getLast().get(0).getId()+1);
        notification.setDate(new Date());
        notification.setTitle(title);
        notification.setContent(content);
        notifications.add(0,notification);
        database.notificationDAO().insertAll(notification);
    }

    public int getLastNotificationId()
    {
        return database.notificationDAO().getLast().get(0).getId();
    }

    public DemoScreen getDemo_screen()
    {
        return demo_screen;
    }

    public void setDemo_screen(DemoScreen demo_screen)
    {
        this.demo_screen = demo_screen;
    }

    public ArrayList<DemoLanguage> getLanguages()
    {
        return languages;
    }

    public ArrayList<DemoNotification> getNotifications()
    {
        return notifications;
    }

}
