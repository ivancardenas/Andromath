package co.edu.eafit.andromath.singlevar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;

import co.edu.eafit.andromath.R;
import co.edu.eafit.andromath.grapher.GrapherActivity;
import co.edu.eafit.andromath.util.Constants;

public class SingleVariableLandingActivity extends AppCompatActivity {

    private String tag = SingleVariableLandingActivity.class.getSimpleName();

    EditText function;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_variable_landing);

        getSupportActionBar().hide();

        function = (EditText) findViewById(R.id.editTextEquation);
    }

    public void graph(View v) {

        Expression expression = new Expression(function.getText().toString());
        BigDecimal x0 = BigDecimal.valueOf(-10d);
        BigDecimal delta = BigDecimal.valueOf(0.05);

        if (isEquationValid(expression, x0, delta)) {
            Intent intent = new Intent(this, GrapherActivity.class);
            intent.putExtra("equation", function.getText().toString());
            startActivity(intent);
        }
    }

    private boolean isEquationValid(Expression expression, BigDecimal x0,
                                    BigDecimal delta) {

        for (int i = 0; i < 1000; i++) {

            double x = x0.add(delta.multiply(BigDecimal.
                    valueOf((double) i))).doubleValue();
            try {
                expression.with(Constants.VARIABLE,
                        BigDecimal.valueOf(x)).eval();
                return true;
            } catch (Expression.ExpressionException e) {
                Log.e(tag, "Invalid equation");
                Toast.makeText(getApplicationContext(),
                        "Invalid equation", Toast.LENGTH_SHORT).show();

                return false;
            }
        }

        return false;
    }

    public void evaluate(View v) {

    }
}