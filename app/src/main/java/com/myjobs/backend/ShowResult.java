package com.myjobs.backend;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Polar;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.PolarSeriesType;
import com.anychart.enums.ScaleStackMode;
import com.anychart.enums.ScaleTypes;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.scales.Linear;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShowResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);

        try {
            JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("json"));
            JSONObject score = jsonObject.getJSONObject("score");
            Log.i("ASd", "onCreate: " + score.toString(4));
            AnyChartView anyChartView = findViewById(R.id.chart);
            anyChartView.setProgressBar(findViewById(R.id.progress_bar));
            Polar polar = AnyChart.polar();
            List<DataEntry> data = new ArrayList<>();
            Iterator<String> iter = score.keys();
            int i = 1;
            while (iter.hasNext()) {
                String key = iter.next();
                Object value = score.get(key);
                System.out.println(Float.valueOf((String) value.toString()));
                data.add(new CustomDataEntry(key, Float.valueOf(value.toString())));
            }
            Set set = Set.instantiate();
            set.data(data);
            Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");
            polar.column(series1Data);
            polar.title("Your personality Analysis");
            polar.sortPointsByX(true)
                    .defaultSeriesType(PolarSeriesType.COLUMN)
                    .yAxis(false)
                    .xScale(ScaleTypes.ORDINAL);

            polar.title().margin().bottom(20d);
            ((Linear) polar.yScale(Linear.class)).stackMode(ScaleStackMode.VALUE);
            polar.tooltip()
                    .valuePrefix("$")
                    .displayMode(TooltipDisplayMode.UNION);
            anyChartView.setChart(polar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value) {
            super(x, value);
        }
    }


}
