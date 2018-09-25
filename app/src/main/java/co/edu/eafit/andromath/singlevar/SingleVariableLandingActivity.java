package co.edu.eafit.andromath.singlevar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;

import co.edu.eafit.andromath.R;

public class SingleVariableLandingActivity extends AppCompatActivity {

    String tag = SingleVariableLandingActivity.class.getName();

    private static final String VARIABLE = "x";

    EditText function;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_variable_landing);

        getSupportActionBar().hide();

        function = findViewById(R.id.editTextEquation);
    }

    public void graph(View v) {

        Expression expression = new Expression(function.getText().toString());
        BigDecimal x0 = BigDecimal.valueOf(-10d), x1 = null;
        BigDecimal delta = BigDecimal.valueOf(0.05);

        boolean isEvaluationFinished = false;
        boolean isEquationValid = false;

        for (int i = 0; i < 1000 && !isEvaluationFinished; i++) {

            double x = x0.add(delta.multiply(BigDecimal.
                    valueOf((double) i))).doubleValue();
            try {
                expression.with(VARIABLE,
                        BigDecimal.valueOf(x)).eval();
                isEvaluationFinished = true;
                isEquationValid = true;
            } catch (Expression.ExpressionException e) {
                Log.d(tag, "Invalid equation");
                Toast.makeText(getApplicationContext(),
                        "Invalid equation", Toast.LENGTH_SHORT).show();

                isEvaluationFinished = true;
                isEquationValid = false;
            }
        }

        if (isEquationValid) {
            
        }
    }

    public void parseAndVerify(View v) {

    }
}