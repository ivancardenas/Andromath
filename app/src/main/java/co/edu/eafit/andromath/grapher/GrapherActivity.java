package co.edu.eafit.andromath.grapher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;

import co.edu.eafit.andromath.R;
import co.edu.eafit.andromath.util.Constants;

public class GrapherActivity extends AppCompatActivity {

    private GraphView graphView;

    private Intent intent;

    private double xAxisValueMax = 50d;
    private double xAxisValueMin = -50d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grapher);

        getSupportActionBar().hide();

        intent = getIntent();
        graphView = (GraphView) findViewById(R.id.graph);

        graph();
    }

    private void graph() {

        BigDecimal x0 = new BigDecimal(xAxisValueMin);

        BigDecimal delta = new BigDecimal(0.1);
        DataPoint dataPoints[] = new DataPoint[1000];

        Expression expression = new Expression(
                intent.getStringExtra("equation"));

        double highest = 0.0d, lowest = 0.0d, x, y;

        for (int i = 0; i < 1000; i++) {
            try {
                x = x0.add(delta.multiply(BigDecimal.
                        valueOf((double) i))).doubleValue();

                y = expression.with(Constants.VARIABLE, BigDecimal.
                        valueOf(x)).eval().doubleValue();

                dataPoints[i] = new DataPoint(x, y);
                if (y > highest && y < xAxisValueMax) highest = y;
                if (y < lowest && y > xAxisValueMin) lowest = y;
            } catch (Expression.ExpressionException e){
                dataPoints[i] = null;
            }
        }

        LineGraphSeries lineGraphSeries = new
                LineGraphSeries(dataPoints);

        graphView.addSeries(lineGraphSeries);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMaxX(xAxisValueMax);
        graphView.getViewport().setMinX(xAxisValueMin);

        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMaxY(highest);
        graphView.getViewport().setMinY(lowest);
    }
}