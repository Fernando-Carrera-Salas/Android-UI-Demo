package com.java.uidemo.demos.fragment;

import static com.java.uidemo.util.Constants.BIONIC_LENGTHS_HEAVY;
import static com.java.uidemo.util.Constants.BIONIC_LENGTHS_LIGHT;
import static com.java.uidemo.util.Constants.BIONIC_LENGTHS_STANDARD;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.ybq.android.spinkit.SpinKitView;
import com.java.uidemo.R;
import com.java.uidemo.adapter.NewsArticleAdapter;
import com.java.uidemo.application.UIDemo;
import com.java.uidemo.model.NewsArticle;
import com.java.uidemo.util.Animations;
import com.java.uidemo.util.DemoActivity;
import com.java.uidemo.util.Util;

import java.util.ArrayList;
import java.util.Random;

/**
 * This {@link Fragment} displays a {@link RecyclerView} with four articles.
 * </p><p>
 * If an article is clicked, the RecyclerView will scroll so that the article is at the top, a transparent View will prevent moving it and a layout with the full article will appear over it.
 * </p><p>
 * The RecyclerView has a dynamic bottom padding of the height of the screen minus 420 density pixels (300 for an article, 60 for the top bar and 60 for the bottom bar),
 * guaranteeing that there will be enough space for the last article to be placed at the top of the screen.
 * </p><p>
 * If the last article is loaded into view, four more articles will be added to the list.
 */
public class FragmentNews extends Fragment
{
    private Activity activity;
    private UIDemo demo;
    private NewsArticleAdapter news_article_adapter;
    private LinearLayoutManager news_article_manager;
    private RelativeLayout rl_full_article;
    private ScrollView sv_full_article;
    private DisplayMetrics display_metrics;
    private RecyclerView.SmoothScroller smooth_scroller;
    private View v_prevent_click;
    private ImageView iv_bt_bionic;
    private ImageView iv_full_article;
    private TextView tv_full_article;
    private int bionic_level;
    private ArrayList<NewsArticle> news;
    private SpinKitView skv_load_articles;
    private NewsArticle full_article;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View v = inflater.inflate(R.layout.fragment_news, container, false);
        activity = getActivity();
        demo = (UIDemo) activity.getApplicationContext();
        bionic_level = 0;
        news = new ArrayList<>();
        news.add(new NewsArticle(R.drawable.news_ph_1,R.string.placeholder_news_1));
        news.add(new NewsArticle(R.drawable.news_ph_2,R.string.placeholder_news_2));
        news.add(new NewsArticle(R.drawable.news_ph_3,R.string.placeholder_news_3));
        news.add(new NewsArticle(R.drawable.news_ph_4,R.string.placeholder_news_4));

        final RecyclerView rv_articles = v.findViewById(R.id.rv_articles_news);
        news_article_adapter = new NewsArticleAdapter(news,activity);
        news_article_manager = new LinearLayoutManager(activity);
        rl_full_article = v.findViewById(R.id.rl_full_article_news);
        sv_full_article = v.findViewById(R.id.sv_full_article_news);
        v_prevent_click = v.findViewById(R.id.v_prevent_click_news);
        v_prevent_click.setVisibility(View.GONE);
        skv_load_articles = v.findViewById(R.id.skv_load_articles_news);
        skv_load_articles.setVisibility(View.INVISIBLE);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl_full_article.getLayoutParams();
        params.height = (int) Util.convertDpToPixel(300,activity);
        rl_full_article.setLayoutParams(params);
        rv_articles.setLayoutManager(news_article_manager);
        rv_articles.setAdapter(news_article_adapter);
        display_metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(display_metrics);
        int padding_bottom = display_metrics.heightPixels - ((int) Util.convertDpToPixel(420,activity));
        rv_articles.setPadding(0,0,0,padding_bottom);
        smooth_scroller = new LinearSmoothScroller(activity)
        {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        iv_bt_bionic = v.findViewById(R.id.iv_bt_bionic_news);
        iv_full_article = v.findViewById(R.id.iv_full_article_news);
        iv_full_article.setOnClickListener(view -> ((DemoActivity)activity).displayFullscreenImage(full_article.getImage_resource()));
        full_article = null;
        tv_full_article = v.findViewById(R.id.tv_full_article_news);
        tv_full_article.setTypeface(demo.getTf_monserrat_light());
        iv_bt_bionic.setOnClickListener(view ->
        {
            bionic_level++;
            if (bionic_level>3)
                bionic_level = 0;
            articleText();
        });

        final SwipeRefreshLayout srl_rv_articles = v.findViewById(R.id.srl_rv_articles_news);
        srl_rv_articles.setOnRefreshListener(() -> {
            int size = news.size();
            news.clear();
            news_article_adapter.notifyItemRangeRemoved(0,size);
            v_prevent_click.setVisibility(View.VISIBLE);
            Handler h = new Handler(Looper.getMainLooper());
            h.postDelayed(() ->
            {
                try
                {
                    news.add(new NewsArticle(R.drawable.news_ph_1,R.string.placeholder_news_1));
                    news.add(new NewsArticle(R.drawable.news_ph_2,R.string.placeholder_news_2));
                    news.add(new NewsArticle(R.drawable.news_ph_3,R.string.placeholder_news_3));
                    news.add(new NewsArticle(R.drawable.news_ph_4,R.string.placeholder_news_4));
                    news_article_adapter.notifyItemRangeInserted(0,4);
                    v_prevent_click.setVisibility(View.GONE);
                    srl_rv_articles.setRefreshing(false);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            },(new Random().nextInt(2500)+500));
        });
        srl_rv_articles.setRefreshing(false);

        return v;
    }

    public boolean checkBackFullArticle()
    {
        if (rl_full_article.getVisibility()==View.VISIBLE)
        {
            v_prevent_click.setVisibility(View.GONE);
            rl_full_article.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl_full_article.getLayoutParams();
            params.height = (int) Util.convertDpToPixel(300,activity);
            rl_full_article.setLayoutParams(params);
            return true;
        }
        return false;
    }

    public void displayFullArticle(NewsArticle article, int position)
    {
        v_prevent_click.setVisibility(View.VISIBLE);
        smooth_scroller.setTargetPosition(position);
        news_article_manager.startSmoothScroll(smooth_scroller);
        sv_full_article.fullScroll(ScrollView.FOCUS_UP);
        full_article = article;
        iv_full_article.setImageResource(full_article.getImage_resource());
        articleText();
        Handler h = new Handler(Looper.getMainLooper());
        h.postDelayed(() ->
        {
            rl_full_article.setVisibility(View.VISIBLE);
            Animations.scaleUpToSize(rl_full_article,500,display_metrics.heightPixels);
        },250);
    }

    public void loadMoreNews()
    {
        if (isAdded())
        {
            news.add(new NewsArticle(R.drawable.news_ph_1,R.string.placeholder_news_1));
            news.add(new NewsArticle(R.drawable.news_ph_2,R.string.placeholder_news_2));
            news.add(new NewsArticle(R.drawable.news_ph_3,R.string.placeholder_news_3));
            news.add(new NewsArticle(R.drawable.news_ph_4,R.string.placeholder_news_4));
            v_prevent_click.setVisibility(View.VISIBLE);
            skv_load_articles.setVisibility(View.VISIBLE);
            Handler h = new Handler(Looper.getMainLooper());
            h.postDelayed(() ->
            {
                try
                {
                    news_article_adapter.notifyItemRangeInserted(news.size()-4,4);
                    Handler h1 = new Handler(Looper.getMainLooper());
                    h1.postDelayed(() ->
                    {
                        try
                        {
                            v_prevent_click.setVisibility(View.GONE);
                            skv_load_articles.setVisibility(View.INVISIBLE);
                            news_article_adapter.setLoading(false);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    },1000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            },1000);
        }
    }

    private void articleText()
    {
        switch (bionic_level)
        {
            case 0:
                tv_full_article.setText(Html.fromHtml(getString(full_article.getText_resource()).replace("\n","<br/>"),Html.FROM_HTML_MODE_COMPACT));
                iv_bt_bionic.setBackgroundResource(R.drawable.bg_round_button_0);
                break;
            case 1:
                tv_full_article.setText(Html.fromHtml(Util.bionifyText(getString(full_article.getText_resource()),BIONIC_LENGTHS_LIGHT).replace("\n","<br/>"), Html.FROM_HTML_MODE_COMPACT));
                iv_bt_bionic.setBackgroundResource(R.drawable.bg_round_button_1);
                break;
            case 2:
                tv_full_article.setText(Html.fromHtml(Util.bionifyText(getString(full_article.getText_resource()),BIONIC_LENGTHS_STANDARD).replace("\n","<br/>"), Html.FROM_HTML_MODE_COMPACT));
                iv_bt_bionic.setBackgroundResource(R.drawable.bg_round_button_3);
                break;
            case 3:
                tv_full_article.setText(Html.fromHtml(Util.bionifyText(getString(full_article.getText_resource()),BIONIC_LENGTHS_HEAVY).replace("\n","<br/>"), Html.FROM_HTML_MODE_COMPACT));
                iv_bt_bionic.setBackgroundResource(R.drawable.bg_round_button_5);
                break;
        }
    }
}
