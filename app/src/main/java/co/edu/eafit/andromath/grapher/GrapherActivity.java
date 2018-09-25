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

    private LineGraphSeries lineGraphSeries;
    private DataPoint dataPoints[];
    private Expression expression;
    private GraphView graphView;
    private BigDecimal delta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grapher);

        graphView = (GraphView) findViewById(R.id.graph);

        Intent intent = getIntent();
        expression = new Expression(intent.getStringExtra("equation"));

        BigDecimal x0 = new BigDecimal(-10d);

        delta = new BigDecimal(0.05);
        dataPoints = new DataPoint[400];

        double highest = 0.0d, lowest = 0.0d, x, y;

        for (int i = 0; i < 400; i++) {
            try {
                x = x0.add(delta.multiply(BigDecimal.
                        valueOf((double) i))).doubleValue();

                y = expression.with(Constants.VARIABLE, BigDecimal.
                        valueOf(x)).eval().doubleValue();

                dataPoints[i] = new DataPoint(x, y);
                if (y > highest) highest = y;
                if (y < lowest) lowest = y;
            } catch (Exception e){
                dataPoints[i] = null;
            }
        }

        lineGraphSeries = new LineGraphSeries(dataPoints);
        graphView.addSeries(lineGraphSeries);

        graphView.getViewport().setXAxisBoundsManual(true);

        graphView.getViewport().setMaxX(x0.doubleValue() + 20d);
        graphView.getViewport().setMinX(x0.doubleValue());

        graphView.getViewport().setMaxY(highest);
        graphView.getViewport().setMinY(lowest);
    }
}