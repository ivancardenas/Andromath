package co.edu.eafit.andromath.singlevar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.jjoe64.graphview.series.DataPoint;
import com.udojava.evalex.Expression;
import java.math.BigDecimal;
import java.util.Objects;

import co.edu.eafit.andromath.R;
import co.edu.eafit.andromath.grapher.GrapherActivity;
import co.edu.eafit.andromath.util.Constants;
import co.edu.eafit.andromath.util.Messages;

public class SingleVariableLandingActivity extends AppCompatActivity {

    private static final String tag = SingleVariableLandingActivity.class.getSimpleName();

    private EditText function;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_variable_landing);
        Objects.requireNonNull(getSupportActionBar()).hide();

        function = (EditText) findViewById(R.id.editTextEquation);
    }

    public void graph(View v) {

        Expression expression = new Expression(
                function.getText().toString());

        intent = new Intent(this,
                GrapherActivity.class);

        DataPoint[] dataPoints = getGraphPoints(expression);

        if (dataPoints != null) {
            intent.putExtra("points", dataPoints);
            startActivity(intent);
        } else {
            Messages.invalidEquation(tag, getApplicationContext());
        }
    }

    public void evaluate(View v) {

        Expression expression = new Expression(
                function.getText().toString());

        intent = new Intent(this,
                SingleVariableElectionActivity.class);

        DataPoint[] dataPoints = getGraphPoints(expression);

        if (dataPoints != null) {
            intent.putExtra(Constants.EQUATION,
                    function.getText().toString());
            startActivity(intent);
        } else {
            Messages.invalidEquation(tag, getApplicationContext());
        }
    }

    private DataPoint[] getGraphPoints(Expression expression) {

        double xAxisValueMax = 50d, xAxisValueMin = -50d;
        double highestY = 0.0d, lowestY = 0.0d, x, y;

        BigDecimal x0 = new BigDecimal(xAxisValueMin);
        BigDecimal delta = new BigDecimal(0.1);

        DataPoint dataPoints[] = new DataPoint[1000];

        for (int i = 0; i < 1000; i++) {

            try {
                x = x0.add(delta.multiply(BigDecimal.
                        valueOf((double) i))).doubleValue();

                y = expression.with(Constants.VARIABLE, BigDecimal.
                        valueOf(x)).eval().doubleValue();

                dataPoints[i] = new DataPoint(x, y);

                if (y > highestY && y < xAxisValueMax) highestY = y;
                if (y < lowestY && y > xAxisValueMin) lowestY = y;

            } catch (Expression.ExpressionException
                    | ArithmeticException | NumberFormatException e) {

                return null;
            }
        }

        intent.putExtra("highestY", highestY);
        intent.putExtra("lowestY", lowestY);

        intent.putExtra("xAxisValueMax", xAxisValueMax);
        intent.putExtra("xAxisValueMin", xAxisValueMin);

        return dataPoints;
    }
}