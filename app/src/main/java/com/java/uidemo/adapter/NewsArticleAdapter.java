package com.java.uidemo.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.java.uidemo.R;
import com.java.uidemo.application.UIDemo;
import com.java.uidemo.demos.ViewPagerActivity;
import com.java.uidemo.demos.fragment.FragmentNews;
import com.java.uidemo.model.NewsArticle;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} for articles in {@link ViewPagerActivity}/{@link FragmentNews}
 */
public class NewsArticleAdapter extends RecyclerView.Adapter
{
    private final ArrayList<NewsArticle> news_articles;
    private final Context context;
    private final UIDemo demo;
    private boolean loading;

    public NewsArticleAdapter(ArrayList<NewsArticle> news_articles, Context context)
    {
        this.news_articles = news_articles;
        this.context = context;
        this.demo = (UIDemo) context.getApplicationContext();
        loading = false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new NewsArticleHolder(inflater.inflate(R.layout.item_news_article, parent, false));
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        final NewsArticleHolder h = (NewsArticleHolder) holder;
        final NewsArticle na = news_articles.get(position);
        h.iv_item.setImageResource(na.getImage_resource());
        h.tv_item.setText(Html.fromHtml(context.getString(na.getText_resource()), Html.FROM_HTML_MODE_COMPACT));
        h.tv_item.setTypeface(demo.getTf_monserrat_light());
        if (na.isLoading())
        {
            h.iv_item.setVisibility(View.INVISIBLE);
            h.tv_item.setVisibility(View.INVISIBLE);
            h.sfl_load_item.setVisibility(View.VISIBLE);
            h.sfl_load_item.startShimmerAnimation();
            h.rl_item.setOnClickListener(null);
            Handler ha = new Handler(Looper.getMainLooper());
            ha.postDelayed(() ->
            {
                na.setLoading(false);
                notifyItemChanged(h.getAdapterPosition());
            },2000);
        }
        else
        {
            h.iv_item.setVisibility(View.VISIBLE);
            h.tv_item.setVisibility(View.VISIBLE);
            h.sfl_load_item.setVisibility(View.INVISIBLE);
            h.sfl_load_item.stopShimmerAnimation();
            h.rl_item.setOnClickListener(view -> ((ViewPagerActivity)context).display_full_article(na, h.getAdapterPosition()));
            if (position>=news_articles.size()-1&&!loading)
            {
                loading = true;
                ((ViewPagerActivity)context).loadMoreNews();
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return news_articles.size();
    }

    public void setLoading(boolean loading)
    {
        this.loading = loading;
    }

    private static class NewsArticleHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout rl_item;
        TextView tv_item;
        ImageView iv_item;
        ShimmerFrameLayout sfl_load_item;
        NewsArticleHolder(View v)
        {
            super(v);
            rl_item = v.findViewById(R.id.rl_item_news_article);
            iv_item = v.findViewById(R.id.iv_item_news_article);
            tv_item = v.findViewById(R.id.tv_item_news_article);
            sfl_load_item = v.findViewById(R.id.sfl_load_item_news_article);
        }
    }
}
