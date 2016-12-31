package bro.tm;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.DashPathEffect;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.util.ArrayList;
import java.util.Arrays;

import models.WeeklyMaxLogsHelper;


/**
 * Created by root on 06/12/16.
 */

public class SetupFragment extends Fragment {

    public static SetupFragment newInstance(){
        SetupFragment thisFragment = new SetupFragment();
        return thisFragment;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        SharedPreferences prefs = this.getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.setup_layout , container, false);
        TextView v = (TextView) rootView.findViewById(R.id.squat_max);
        v.setText(prefs.getString("squat_max","0"));
        v = (TextView) rootView.findViewById(R.id.dead_lift_max);
        v.setText(prefs.getString("dead_lift_max","0"));
        v = (TextView) rootView.findViewById(R.id.bench_max);
        v.setText(prefs.getString("bench_max","0"));


        //draw the graph

        XYPlot plot = (XYPlot) rootView.findViewById(R.id.plot);

        //get the values from database
        WeeklyMaxLogsHelper logs = new WeeklyMaxLogsHelper(this.getActivity());
        SQLiteDatabase db = logs.getReadableDatabase();

        String[] projection = {
                "squat_max",
                "bench_max",
                "deadlift_max"
        };
        Cursor cursor = db.rawQuery(
        "select * from logs",null);

        // create a couple arrays of y-values to plot:
        final Number[] domainLabels = {1, 2, 3, 6, 7, 8, 9, 10, 13, 14};
        ArrayList series1Numbers = new ArrayList<Double>();
        ArrayList series2Numbers = new ArrayList<Double>();
        ArrayList series3Numbers = new ArrayList<Double>();

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            series1Numbers.add(cursor.getDouble(0));
            series2Numbers.add(cursor.getDouble(1));
            series3Numbers.add(cursor.getDouble(2));
            cursor.moveToNext();
        }

        XYSeries series1 = new SimpleXYSeries(
                series1Numbers, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1");
        XYSeries series2 = new SimpleXYSeries(
                series2Numbers, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2");
        XYSeries series3 = new SimpleXYSeries(
                series3Numbers, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series3");

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(this.getActivity(), R.xml.line_point_formatter_with_labels);

        LineAndPointFormatter series2Format =
                new LineAndPointFormatter(this.getActivity(), R.xml.line_point_formatter_with_labels_2);

        LineAndPointFormatter series3Format =
                new LineAndPointFormatter(this.getActivity(), R.xml.asdf);

        PixelUtils.init(this.getActivity());
        // add an "dash" effect to the series2 line:
        series2Format.getLinePaint().setPathEffect(new DashPathEffect(new float[] {
                PixelUtils.dpToPix(20),
                PixelUtils.dpToPix(15)}, 0));

        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/
        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        series2Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));
        series3Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);
        plot.addSeries(series2, series2Format);
        plot.addSeries(series3, series3Format);

        //return root

        return rootView;
    }

}
