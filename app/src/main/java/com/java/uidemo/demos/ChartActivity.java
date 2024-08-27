package com.java.uidemo.demos;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.java.uidemo.R;
import com.java.uidemo.util.DemoActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * This {@link DemoActivity} displays four examples of <a href="https://github.com/PhilJay/MPAndroidChart">MPAndroidChart</a>.
 * </p><p>
 * <ul>
 *     <li>A {@link LineChart} with a custom display window when a node is clicked.</li>
 *     <li>A {@link BarChart}.</li>
 *     <li>A {@link PieChart} that slowly rotates and shows the value of the selected slice in the center.</li>
 *     <li>A {@link LineChart} which simulates an ECG and is updated constantly.</li>
 * </ul>
 */
public class ChartActivity extends DemoActivity
{
    private LineChart lc_linechart, lc_ecg;
    private BarChart bc_barchart;
    private PieChart pc_piechart;


    private ArrayList<Entry> ecg_entries;
    private long time_first_value;

    private Handler h_sim_ecg, h_tick_ecg;

    private final float[] ecg_values = {20, 20, 22, 22.5f, 23, 22.5f, 22, 20, 20, 20, 10, 5, 20, 50, 20, 0, 10, 20, 20, 20, 20, 24, 25, 26, 27, 27, 27, 26, 25, 24, 20, 20};
    private int current_ecg_value;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        initDemo();

        lc_linechart = findViewById(R.id.lc_linechart_chart);
        bc_barchart = findViewById(R.id.bc_barchart_chart);
        pc_piechart = findViewById(R.id.pc_piechart_chart);
        lc_ecg = findViewById(R.id.lc_ecg_chart);

        setup_charts();
    }

    private void setup_charts()
    {
        IMarker linechart_marker = new CustomMarkerView (this, R.layout.chart_marker_content);
        lc_linechart.setMarker(linechart_marker);
        lc_linechart.setNoDataText("");
        lc_linechart.setNoDataTextTypeface(demo.getTf_monserrat_light());
        lc_linechart.getDescription().setEnabled(false);
        lc_linechart.getLegend().setEnabled(false);
        lc_linechart.getAxisRight().setEnabled(false);
        lc_linechart.setExtraBottomOffset(0);
        lc_linechart.setExtraLeftOffset(0);
        lc_linechart.setExtraRightOffset(0);
        lc_linechart.setViewPortOffsets(0,0,0,100);
        lc_linechart.setXAxisRenderer(new CustomXAxisRenderer(lc_linechart.getViewPortHandler(), lc_linechart.getXAxis(), lc_linechart.getTransformer(YAxis.AxisDependency.LEFT)));
        Paint linechart_paint = lc_linechart.getPaint(Chart.PAINT_INFO);
        linechart_paint.setTextSize(14);
        linechart_paint.setTypeface(demo.getTf_monserrat_light());
        XAxis linechart_x = lc_linechart.getXAxis();
        linechart_x.setLabelRotationAngle(0f);
        linechart_x.setPosition(XAxis.XAxisPosition.BOTTOM);
        linechart_x.setTypeface(demo.getTf_monserrat_light());
        linechart_x.setValueFormatter(new XAxisValueFormatter());
        linechart_x.setDrawGridLines(true);
        YAxis linechart_y = lc_linechart.getAxisLeft();
        linechart_y.setTypeface(demo.getTf_monserrat_light());
        linechart_y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        linechart_y.setValueFormatter(new YAxisValueFormatter());
        linechart_paint.setColor(getColor(R.color.light_gray));
        linechart_x.setAxisLineColor(getColor(R.color.dark_gray));
        linechart_x.setTextColor(getColor(R.color.dark_gray));
        linechart_y.setTextColor(getColor(R.color.dark_gray));
        linechart_y.setAxisLineColor(getColor(R.color.dark_gray));

        ArrayList<Entry> linechart_entries = new ArrayList<>();

        for (long i=System.currentTimeMillis()-(24*60*60*1000); i<System.currentTimeMillis(); i+=(30*60*1000))
        {
            Entry e = new Entry();
            e.setX(i);
            e.setY(new Random().nextFloat() * (100 - 20) + 20);
            linechart_entries.add(e);
        }


        LineDataSet linechart_linedataset = new LineDataSet(linechart_entries, "");
        linechart_linedataset.setValueFormatter(new YAxisValueFormatter());
        linechart_linedataset.setValueTypeface(demo.getTf_monserrat_light());
        linechart_linedataset.setDrawIcons(false);
        linechart_linedataset.setLineWidth(1f);
        linechart_linedataset.setCircleRadius(1f);
        linechart_linedataset.setDrawCircleHole(false);
        linechart_linedataset.setFormLineWidth(1f);
        linechart_linedataset.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        linechart_linedataset.setFormSize(15.f);
        linechart_linedataset.setValueTextSize(9f);
        linechart_linedataset.enableDashedHighlightLine(20f, 2f, 1f);
        linechart_linedataset.setDrawFilled(true);
        linechart_linedataset.setDrawValues(true);
        linechart_linedataset.setHighLightColor(getColor(R.color.light_gray));
        linechart_linedataset.setCircleColor(getColor(R.color.ui_demo_green));
        linechart_linedataset.setColor(getColor(R.color.ui_demo_green));
        linechart_linedataset.setFillColor(getColor(R.color.ui_demo_green));
        linechart_linedataset.setFillFormatter((dataSet, dataProvider) -> lc_linechart.getAxisLeft().getAxisMinimum());
        linechart_linedataset.setHighlightLineWidth(1f);
        linechart_linedataset.setDrawVerticalHighlightIndicator(true);
        linechart_linedataset.setDrawHorizontalHighlightIndicator(false);
        ArrayList<ILineDataSet> linechart_dataset = new ArrayList<>();

        linechart_linedataset.setMode(LineDataSet.Mode.LINEAR);
        linechart_dataset.add(linechart_linedataset);
        LineData linechart_linedata = new LineData(linechart_dataset);
        linechart_linedata.setValueFormatter(new YAxisValueFormatter());
        linechart_linedata.setValueTypeface(demo.getTf_monserrat_light());
        lc_linechart.setData(linechart_linedata);
        Handler h = new Handler(Looper.getMainLooper());
        h.postDelayed(() -> lc_linechart.invalidate(),100);










        bc_barchart.getDescription().setEnabled(false);
        bc_barchart.setBorderColor(Color.WHITE);
        bc_barchart.setBorderWidth(0);
        bc_barchart.setBackgroundColor(Color.WHITE);
        bc_barchart.setDrawGridBackground(false);
        bc_barchart.setDrawBarShadow(false);
        bc_barchart.setHighlightFullBarEnabled(true);
        bc_barchart.setDrawBorders(true);
        bc_barchart.setPinchZoom(false);
        bc_barchart.setScaleEnabled(false);
        bc_barchart.setDoubleTapToZoomEnabled(false);
        bc_barchart.setAutoScaleMinMaxEnabled(false);
        bc_barchart.setViewPortOffsets(0,0,0,50);
        Paint barchart_paint = bc_barchart.getPaint(Chart.PAINT_INFO);
        barchart_paint.setTypeface(demo.getTf_monserrat_light());
        barchart_paint.setColor(getColor(R.color.ui_demo_green));
        barchart_paint.setTextSize(20f);

        Legend barchart_legend = bc_barchart.getLegend();
        barchart_legend.setWordWrapEnabled(true);
        barchart_legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        barchart_legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        barchart_legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        barchart_legend.setDrawInside(false);
        barchart_legend.setEnabled(false);

        YAxis barchart_ry = bc_barchart.getAxisRight();
        barchart_ry.setDrawGridLines(false);
        barchart_ry.setAxisMinimum(0f);
        barchart_ry.setAxisMaximum(120f);
        barchart_ry.setDrawLabels(false);
        barchart_ry.setDrawAxisLine(false);
        barchart_ry.setEnabled(false);
        barchart_ry.setSpaceBottom(0);
        barchart_ry.setSpaceTop(0);
        barchart_ry.setSpaceMax(0);
        barchart_ry.setSpaceMin(0);

        YAxis barchart_ly = bc_barchart.getAxisLeft();
        barchart_ly.setDrawGridLines(true);
        barchart_ly.setAxisMinimum(0f);
        barchart_ly.setAxisMaximum(120f);
        barchart_ly.setDrawLabels(false);
        barchart_ly.setDrawAxisLine(false);
        barchart_ly.setSpaceBottom(0);
        barchart_ly.setSpaceTop(0);
        barchart_ly.setSpaceMax(0);
        barchart_ly.setSpaceMin(0);


        XAxis x_barchart = bc_barchart.getXAxis();
        x_barchart.setPosition(XAxis.XAxisPosition.BOTTOM);
        x_barchart.setAxisMinimum(-1f);
        x_barchart.setAxisMaximum(7f);
        x_barchart.setGranularity(1f);
        x_barchart.setDrawGridLines(false);
        x_barchart.setTypeface(demo.getTf_monserrat_light());
        x_barchart.setTextSize(12f);
        x_barchart.setDrawLabels(true);
        x_barchart.setDrawAxisLine(true);
        x_barchart.setSpaceMax(0);
        x_barchart.setSpaceMin(0);
        x_barchart.setValueFormatter(new ValueFormatter()
        {
            @Override
            public String getFormattedValue(float value)
            {
                switch ((int)value)
                {
                    case 0:
                        return getString(R.string.monday).substring(0,3);
                    case 1:
                        return getString(R.string.tuesday).substring(0,3);
                    case 2:
                        return getString(R.string.wednesday).substring(0,3);
                    case 3:
                        return getString(R.string.thursday).substring(0,3);
                    case 4:
                        return getString(R.string.friday).substring(0,3);
                    case 5:
                        return getString(R.string.saturday).substring(0,3);
                    case 6:
                        return getString(R.string.sunday).substring(0,3);
                }
                return "";
            }
        });
        final ArrayList<Float> barchart_values = new ArrayList<>();
        for (int i=0; i<7; i++)
        {
            barchart_values.add(new Random().nextFloat() * (100 - 20) + 20);
        }
        BarData barchart_bardata = generateBarData(barchart_values);
        barchart_bardata.setBarWidth(0.75f);
        barchart_bardata.setValueTypeface(demo.getTf_monserrat_light());
        barchart_bardata.setValueFormatter(hide_zeros_formatter);
        barchart_bardata.setValueTextSize(12f);
        bc_barchart.setData(barchart_bardata);
        bc_barchart.invalidate();









        ArrayList<PieEntry> piechart_entries = new ArrayList<>();

        Map<String, Integer> type_amount_map = new HashMap<>();
        type_amount_map.put("1",30);
        type_amount_map.put("2",25);
        type_amount_map.put("3",20);
        type_amount_map.put("4",15);
        type_amount_map.put("5",10);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getColor(R.color.ui_demo_green));
        colors.add(getColor(R.color.ui_demo_blue));
        colors.add(getColor(R.color.ui_demo_yellow));
        colors.add(getColor(R.color.ui_demo_purple));
        colors.add(getColor(R.color.ui_demo_red));

        for(String type: type_amount_map.keySet())
        {
            piechart_entries.add(new PieEntry(type_amount_map.get(type).floatValue(), type));
        }

        PieDataSet piechart_piedataset = new PieDataSet(piechart_entries,"");
        piechart_piedataset.setValueTextSize(12f);
        piechart_piedataset.setColors(colors);
        piechart_piedataset.setValueTypeface(demo.getTf_monserrat_light());
        piechart_piedataset.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        piechart_piedataset.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        piechart_piedataset.setValueTextColor(getColor(R.color.dark_gray));
        piechart_piedataset.setValueFormatter(new ValueFormatter()
        {
            @Override
            public String getFormattedValue(float value)
            {
                return ((int)value+"%");
            }
        });
        PieData piechart_piedata = new PieData(piechart_piedataset);
        piechart_piedata.setDrawValues(true);
        piechart_piedata.setValueTextColor(getColor(R.color.dark_gray));
        piechart_piedata.setValueTypeface(demo.getTf_monserrat_light());

        final TextView tv_piechart = findViewById(R.id.tv_piechart_chart);
        tv_piechart.setTypeface(demo.getTf_ashley_semibold());
        tv_piechart.setVisibility(View.INVISIBLE);
        pc_piechart.setHoleColor(getColor(R.color.white));
        pc_piechart.setExtraOffsets(35f,0f,35f,0f);
        pc_piechart.setDrawEntryLabels(false);
        pc_piechart.setData(piechart_piedata);
        pc_piechart.setTransparentCircleRadius(0f);
        pc_piechart.setDragDecelerationFrictionCoef(0.9f);
        pc_piechart.getDescription().setEnabled(false);
        pc_piechart.setRotationEnabled(false);
        pc_piechart.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, Highlight h)
            {
                String label = ((PieEntry)e).getLabel();
                switch (Integer.parseInt(label))
                {
                    case 1:
                        pc_piechart.setHoleColor(getColor(R.color.ui_demo_green));
                        break;
                    case 2:
                        pc_piechart.setHoleColor(getColor(R.color.ui_demo_blue));
                        break;
                    case 3:
                        pc_piechart.setHoleColor(getColor(R.color.ui_demo_yellow));
                        break;
                    case 4:
                        pc_piechart.setHoleColor(getColor(R.color.ui_demo_purple));
                        break;
                    case 5:
                        pc_piechart.setHoleColor(getColor(R.color.ui_demo_red));
                        break;
                }
                tv_piechart.setText((int) e.getY()+"%");
                tv_piechart.setVisibility(View.VISIBLE);
                pc_piechart.invalidate();
            }

            @Override
            public void onNothingSelected()
            {
                pc_piechart.setHoleColor(getColor(R.color.white));
                tv_piechart.setVisibility(View.INVISIBLE);
                pc_piechart.invalidate();
            }
        });

        ValueAnimator va = ValueAnimator.ofFloat(0,360);
        va.setDuration(20000);
        va.setInterpolator(new LinearInterpolator());
        va.addUpdateListener(valueAnimator ->
        {
            float val = (Float) valueAnimator.getAnimatedValue();
            pc_piechart.setRotationAngle(val);
            pc_piechart.invalidate();
        });
        va.setRepeatMode(ValueAnimator.RESTART);
        va.setRepeatCount(ValueAnimator.INFINITE);
        va.start();

        piechart_piedataset.setValueLinePart1Length(0.6f);
        piechart_piedataset.setValueLinePart2Length(0.3f);
        piechart_piedataset.setValueLineWidth(2f);
        piechart_piedataset.setValueLinePart1OffsetPercentage(115f);
        piechart_piedataset.setUsingSliceColorAsValueLineColor(true);
        piechart_piedataset.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        piechart_piedataset.setValueTextSize(16f);
        piechart_piedataset.setValueTypeface(demo.getTf_monserrat_light());
        pc_piechart.setUsePercentValues(true);
        piechart_piedataset.setSelectionShift(3f);
        pc_piechart.setHoleRadius(50f);
        pc_piechart.getLegend().setEnabled(false);
        pc_piechart.getDescription().setEnabled(false);
        pc_piechart.invalidate();









        ecg_entries = new ArrayList<>();
        ecg_entries.add(new Entry(0,0));
        lc_ecg.setNoDataText("");
        lc_ecg.setNoDataTextTypeface(demo.getTf_monserrat_light());
        lc_ecg.getDescription().setEnabled(false);
        lc_ecg.getLegend().setEnabled(false);
        lc_ecg.getAxisRight().setEnabled(false);
        lc_ecg.setExtraBottomOffset(0);
        lc_ecg.setExtraLeftOffset(0);
        lc_ecg.setExtraRightOffset(0);
        lc_ecg.setViewPortOffsets(0,0,0,0);
        Paint paint_ecg = lc_ecg.getPaint(Chart.PAINT_INFO);
        paint_ecg.setTextSize(14);
        paint_ecg.setTypeface(demo.getTf_monserrat_light());
        XAxis x_ecg = lc_ecg.getXAxis();
        x_ecg.setLabelRotationAngle(0f);
        x_ecg.setPosition(XAxis.XAxisPosition.BOTTOM);
        x_ecg.setTypeface(demo.getTf_monserrat_light());
        x_ecg.setDrawGridLines(true);
        x_ecg.setDrawLabels(false);
        YAxis y_ecg = lc_ecg.getAxisLeft();
        y_ecg.setTypeface(demo.getTf_monserrat_light());
        y_ecg.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y_ecg.setValueFormatter(new YAxisValueFormatter());
        y_ecg.setAxisMinimum(-15f);
        y_ecg.setAxisMaximum(15f);
        y_ecg.setDrawLabels(false);
        y_ecg.setDrawGridLines(true);
        paint_ecg.setColor(getColor(R.color.ui_demo_red));
        x_ecg.setAxisLineColor(getColor(R.color.colorPrimaryDark));
        x_ecg.setTextColor(getColor(R.color.colorPrimaryDark));
        y_ecg.setTextColor(getColor(R.color.colorPrimaryDark));
        y_ecg.setAxisLineColor(getColor(R.color.colorPrimaryDark));

        drawECG();
        simECG();
    }


    private void drawECG()
    {
        lc_ecg.setScaleEnabled(false);
        lc_ecg.setDragEnabled(false);
        lc_ecg.setTouchEnabled(false);
        lc_ecg.setOnChartGestureListener(null);
        lc_ecg.fitScreen();
        ecg_entries.sort(new EntryXComparator());

        LineDataSet linedataset_ecg = new LineDataSet(ecg_entries, "");
        linedataset_ecg.setValueFormatter(new YAxisValueFormatter());
        linedataset_ecg.setValueTypeface(demo.getTf_monserrat_light());
        linedataset_ecg.setDrawIcons(false);
        linedataset_ecg.setLineWidth(2f);
        linedataset_ecg.setValueTextSize(9f);
        linedataset_ecg.setDrawFilled(false);
        linedataset_ecg.setDrawValues(false);
        linedataset_ecg.setDrawCircles(false);
        linedataset_ecg.setHighLightColor(getColor(R.color.colorPrimaryDark));
        linedataset_ecg.setCircleColor(getColor(R.color.colorPrimaryDark));
        linedataset_ecg.setColor(getColor(R.color.ui_demo_red));
        linedataset_ecg.setFillColor(getColor(R.color.colorPrimaryDark));
        ArrayList<ILineDataSet> dataSets1 = new ArrayList<>();
        linedataset_ecg.setMode(LineDataSet.Mode.LINEAR);
        dataSets1.add(linedataset_ecg);
        LineData linedata_ecg = new LineData(dataSets1);
        linedata_ecg.setValueFormatter(new YAxisValueFormatter());
        linedata_ecg.setValueTypeface(demo.getTf_monserrat_light());
        linedata_ecg.setHighlightEnabled(false);
        float max_ecg = 75f;
        float min_ecg = -20f;
        lc_ecg.getAxisLeft().setAxisMaximum(max_ecg);
        lc_ecg.getAxisLeft().setAxisMinimum(min_ecg);
        lc_ecg.setData(linedata_ecg);
        lc_ecg.getXAxis().setLabelCount(lc_ecg.getAxisLeft().getLabelCount(),true);
        lc_ecg.invalidate();
    }

    private final Runnable r_sim_ecg = new Runnable()
    {
        @Override
        public void run()
        {
            Random r = new Random();
            long current_time = System.currentTimeMillis();
            long current_x = current_time- time_first_value;
            float ecg_value = ecg_values[current_ecg_value];
            current_ecg_value++;
            if (current_ecg_value>=ecg_values.length)
                current_ecg_value = 0;
            ecg_entries.add(new Entry(current_x,ecg_value));
            try
            {
                h_sim_ecg.removeCallbacks(r_sim_ecg);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            h_sim_ecg = new Handler(Looper.getMainLooper());
            h_sim_ecg.postDelayed(r_sim_ecg,r.nextInt(20)+10);
        }
    };

    private final Runnable r_tick_ecg = new Runnable()
    {
        @Override
        public void run()
        {
            checkClear();
            drawECG();
            try
            {
                h_tick_ecg.removeCallbacks(r_tick_ecg);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            h_tick_ecg = new Handler(Looper.getMainLooper());
            h_tick_ecg.postDelayed(r_tick_ecg,50);
        }
    };

    private void checkClear()
    {
        long current_time = System.currentTimeMillis()- time_first_value;
        boolean remove_old = true;
        while (remove_old)
        {
            if (!ecg_entries.isEmpty())
            {
                if (ecg_entries.get(0).getX()<(current_time-1000))
                {
                    ecg_entries.remove(0);
                }
                else
                {
                    remove_old = false;
                }
            }
            else
            {
                remove_old = false;
            }
        }
    }
    private void simECG()
    {
        time_first_value = System.currentTimeMillis();
        h_sim_ecg = new Handler(Looper.getMainLooper());
        h_sim_ecg.post(r_sim_ecg);
        try
        {
            h_tick_ecg.removeCallbacks(r_tick_ecg);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        h_tick_ecg = new Handler(Looper.getMainLooper());
        h_tick_ecg.post(r_tick_ecg);
    }
    private BarData generateBarData(ArrayList<Float> values)
    {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int index = 0; index < values.size(); index++)
        {
            entries.add(new BarEntry(index, values.get(index)));
        }
        BarDataSet set = new BarDataSet(entries, "");
        set.setColor(getColor(R.color.ui_demo_blue));
        set.setHighLightColor(getColor(R.color.light_gray));
        set.setValueTextColor(getColor(R.color.dark_gray));
        set.setHighLightAlpha(255);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setDrawValues(true);
        set.setHighlightEnabled(true);
        return new BarData(set);
    }

    private final ValueFormatter hide_zeros_formatter = new ValueFormatter()
    {
        @Override
        public String getFormattedValue(float value)
        {
            if (value>0)
            {
                if (value>=10)
                    return String.format(Locale.US,"%.0f",value);
                else
                    return String.format(Locale.US,"%.1f",value);
            }else{
                return "";
            }
        }
    };

    public class CustomMarkerView extends MarkerView
    {
        private final TextView tv_content, tv_date;
        public CustomMarkerView(Context context, int layoutResource)
        {
            super(context, layoutResource);
            tv_content = findViewById(R.id.tv_content_chart_marker);
            tv_content.setTypeface(demo.getTf_monserrat_light());
            tv_date = findViewById(R.id.tv_date_chart_marker);
            tv_date.setTypeface(demo.getTf_monserrat_light());
            setChartView(lc_linechart);
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight)
        {
            Date date = new Date((long)e.getX());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.US);
            String s_date = sdf.format(date);
            tv_date.setText(s_date);

            double value = e.getY();
            String s_value = String.format(Locale.US,"%.2f", value);
            tv_content.setText(s_value);
            super.refreshContent(e, highlight);
        }

        private MPPointF offset;

        @Override
        public MPPointF getOffset()
        {
            if(offset == null)
            {
                offset = new MPPointF(-(getWidth() / 2f), -getHeight());
            }
            return offset;
        }
    }

    public static class XAxisValueFormatter extends ValueFormatter
    {
        @Override
        public String getAxisLabel(float value, AxisBase axis)
        {
            Date date = new Date((long)value);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm\ndd/MM",Locale.US);
            return sdf.format(date);
        }
    }
    private static class YAxisValueFormatter extends ValueFormatter
    {
        @Override
        public String getFormattedValue(float value)
        {
            return String.format(Locale.US,"%.2f", value);
        }
        @Override
        public String getAxisLabel(float value, AxisBase axis)
        {
            return String.format(Locale.US,"%.2f", value);
        }
    }

    public static class CustomXAxisRenderer extends XAxisRenderer
    {
        public CustomXAxisRenderer(ViewPortHandler view_port_handler, XAxis axis, Transformer trans)
        {
            super(view_port_handler, axis, trans);
        }
        @Override
        protected void drawLabel(Canvas c, String label, float x, float y, MPPointF anchor, float angle)
        {
            String[] line = label.split("\n");
            Utils.drawXAxisValue(c, line[0], x + mAxisLabelPaint.getTextSize(), y, mAxisLabelPaint, anchor, angle);
            Utils.drawXAxisValue(c, line[1], x + mAxisLabelPaint.getTextSize(), y + mAxisLabelPaint.getTextSize(), mAxisLabelPaint, anchor, angle);
        }
    }


}
