package co.edu.eafit.andromath.singlevar.methods;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.util.Objects;

import co.edu.eafit.andromath.R;
import co.edu.eafit.andromath.util.Constants;
import co.edu.eafit.andromath.util.Messages;

import static com.udojava.evalex.Expression.e;

public class IncrementalSearchActivity extends AppCompatActivity {

    private static final String tag = IncrementalSearchActivity.class.getSimpleName();

    EditText x0Input, deltaInput, iterationsInput;
    TextView function, result;
    Expression expression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incremental_search);
        Objects.requireNonNull(getSupportActionBar()).hide();

        x0Input = (EditText) findViewById(R.id.editTextInitialValue);
        deltaInput = (EditText) findViewById(R.id.editTextDelta);
        iterationsInput = (EditText) findViewById(R.id.editTextIterations);

        result = (TextView)findViewById(R.id.textViewResult);
        function = (TextView) findViewById(R.id.textViewFunction);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        String equation = "f(x) = " + intent.
                getStringExtra(Constants.EQUATION);
        function.setText(equation);

        expression = new Expression(intent.
                getStringExtra(Constants.EQUATION));
    }

    public void incrementalSearch(View v) {

        x0Input.setSelected(false);
        deltaInput.setSelected(false);
        iterationsInput.setSelected(false);
        result.setVisibility(View.VISIBLE);

        String solution = incrementalSearch();

        if (solution != null) {
            result.setText(solution);
        } else {
            Messages.invalidEquation(tag,
                    getApplicationContext());
        }
    }

    private String incrementalSearch() {

        String result;

        BigDecimal x0 = BigDecimal.valueOf(Double.
                parseDouble(x0Input.getText().toString()));

        BigDecimal delta = BigDecimal.valueOf(Double.
                parseDouble(deltaInput.getText().toString()));

        int iterations = Integer.parseInt(
                iterationsInput.getText().toString());

        if (delta.compareTo(BigDecimal.ZERO) == 0) {
            result = "Delta is not valid";
        } else {
            if (iterations < 1) {
                result = "Wrong number of iterations";
            } else {

                try {
                    BigDecimal y0 = expression.with(
                            Constants.VARIABLE, x0).eval();

                    if (y0.compareTo(BigDecimal.ZERO) == 0) {
                        result = "x = " + x0.toString() + " is a root";
                    } else {
                        BigDecimal x1 = x0.add(delta);
                        BigDecimal y1 = expression.with(
                                Constants.VARIABLE, x1).eval();

                        int count = 1;

                        while (y1.compareTo(BigDecimal.ZERO) != 0 && y0.multiply(y1).
                                compareTo(BigDecimal.ZERO) > 0 && count < iterations) {
                            x0 = x1;
                            y0 = y1;
                            x1 = x0.add(delta);
                            y1 = expression.with(Constants.
                                    VARIABLE, x1).eval();
                            count++;
                        }

                        if (y1.compareTo(BigDecimal.ZERO) == 0) {
                            result = "x = " + x1.toString() + " is a root";
                        } else if (y0.multiply(y1).compareTo(BigDecimal.ZERO) < 0) {
                            result = "There is a root between x0 = "
                                    + x0.toString() + " and x1 = " + x1.toString();
                        } else {
                            result = "The method failed after " + iterations + " iterations";
                        }
                    }
                } catch (Expression.ExpressionException
                        | ArithmeticException | NumberFormatException e) {
                    return null;
                }
            }
        }

        return result;
    }
}