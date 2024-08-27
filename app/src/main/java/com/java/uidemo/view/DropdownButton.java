package com.java.uidemo.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.uidemo.adapter.DropdownAdapter;
import com.java.uidemo.model.DropdownItem;
import com.java.uidemo.util.Animations;
import com.java.uidemo.util.DemoActivity;
import com.java.uidemo.util.Util;

import java.util.ArrayList;

/**
 * A custom dropdown button that allows for much more customization than the default {@link android.widget.Spinner}.
 */
public class DropdownButton extends RelativeLayout
{
    private ArrayList<DropdownItem> items;
    private DropdownItem selected_item;
    private CardView cv_dropdown;
    private RecyclerView rv_dropdown;
    private DropdownAdapter adapter;
    private LinearLayoutManager manager;
    private RelativeLayout root_layout;
    private DropdownButtonEventListener listener;

    public DropdownButton(Context context)
    {
        super(context);
        items = null;
        selected_item = null;
        cv_dropdown = null;
        rv_dropdown = null;
        adapter = null;
        manager = null;
        root_layout = null;
    }

    public DropdownButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        items = null;
        selected_item = null;
        cv_dropdown = null;
        rv_dropdown = null;
        adapter = null;
        manager = null;
        root_layout = null;
    }

    public DropdownButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        items = null;
        selected_item = null;
        cv_dropdown = null;
        rv_dropdown = null;
        adapter = null;
        manager = null;
        root_layout = null;
    }

    public void setup(ImageView iv_selected, TextView tv_selected, ArrayList<DropdownItem> items, int default_item, String none_selected, RelativeLayout root_layout, DropdownButtonEventListener listener)
    {
        this.items = items;
        this.root_layout = root_layout;
        this.listener = listener;
        if (default_item>=0&&items.size()>default_item)
        {
            selected_item = items.get(default_item);
        }
        if (selected_item!=null)
        {
            if (iv_selected!=null)
            {
                iv_selected.setImageResource(selected_item.getIcon_resource());
                iv_selected.setVisibility(VISIBLE);
            }
            tv_selected.setText(selected_item.getText());
        }
        else
        {
            if (iv_selected!=null)
            {
                iv_selected.setVisibility(GONE);
            }
            tv_selected.setText(none_selected);
        }
        cv_dropdown = new CardView(getContext());
        cv_dropdown.setRadius(Util.convertDpToPixel(20,getContext()));
        rv_dropdown = new RecyclerView(getContext());
        adapter = new DropdownAdapter(items, getContext(), item ->
        {
            selected_item = item;
            if (iv_selected!=null)
            {
                if (item.getIcon_resource()<=0)
                {
                    iv_selected.setVisibility(GONE);
                }
                else
                {
                    iv_selected.setImageResource(selected_item.getIcon_resource());
                    iv_selected.setVisibility(VISIBLE);
                }
            }
            tv_selected.setText(item.getText());
            if (listener!=null)
                listener.onSelected(selected_item);
            closeDropdown();
        });
        manager = new LinearLayoutManager(getContext());
        rv_dropdown.setLayoutManager(manager);
        rv_dropdown.setAdapter(adapter);
        cv_dropdown.setVisibility(INVISIBLE);
        cv_dropdown.addView(rv_dropdown,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setOnClickListener(view ->
        {
            if (root_layout!=null&&isVisible())
            {
                if (isDropdownOpen())
                {
                    closeDropdown();
                }
                else
                {
                    openDropdown();
                }
            }
        });
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onFocusChanged(boolean focus, int direction, @Nullable Rect previous_rect)
    {
        if (focus&&cv_dropdown.getParent()==null)
        {
            openDropdown();
            Util.hideKeyboard((Activity)getContext());
        }
        if (!focus&&isDropdownOpen())
            closeDropdown();
        super.onFocusChanged(focus, direction, previous_rect);
    }

    private boolean isVisible()
    {
        if (!isShown())
        {
            return false;
        }
        final Rect position = new Rect();
        getGlobalVisibleRect(position);
        final Rect screen = new Rect(0, 0, Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels);
        return position.intersect(screen);
    }

    public boolean isDropdownOpen()
    {
        return cv_dropdown!=null&&cv_dropdown.getVisibility()==VISIBLE;
    }

    public void setItems(ArrayList<DropdownItem> items)
    {
        this.items = items;
        if (adapter!=null)
            adapter.notifyDataSetChanged();
    }

    public ArrayList<DropdownItem> getItems()
    {
        return items;
    }

    public void openDropdown()
    {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        params.leftMargin = (int) Util.convertDpToPixel(20,getContext());
        params.rightMargin = (int) Util.convertDpToPixel(20,getContext());

        int[] location = new int[2];
        getLocationOnScreen(location);
        int y = location[1];
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        boolean is_close_to_bottom = y+Util.convertDpToPixel(300,getContext())>height;
        if (is_close_to_bottom)
        {
            params.bottomMargin = height - y;
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
        else
        {
            params.topMargin = y;
        }

        root_layout.addView(cv_dropdown,params);
        Animations.open(cv_dropdown,200,null,(int)Util.convertDpToPixel(300,getContext()));

        requestFocus();

        ((DemoActivity)getContext()).setGlobal_touch_listener(ev ->
        {
            Rect rect = new Rect();
            cv_dropdown.getGlobalVisibleRect(rect);
            if (!rect.contains((int) ev.getRawX(), (int) ev.getRawY()))
            {
                clearFocus();
            }
        });

        if (listener!=null)
            listener.onOpen();
    }

    public void closeDropdown()
    {
        Animations.close(cv_dropdown,200, () -> root_layout.removeView(cv_dropdown));

        if (listener!=null)
            listener.onClose();
    }

    public interface DropdownButtonEventListener
    {
        void onOpen();

        void onClose();

        void onSelected(DropdownItem item_selected);
    }
}
