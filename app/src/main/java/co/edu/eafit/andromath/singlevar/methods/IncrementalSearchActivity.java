package co.edu.eafit.andromath.singlevar.methods;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import co.edu.eafit.andromath.R;
import co.edu.eafit.andromath.util.Messages;

import static co.edu.eafit.andromath.util.Constants.EQUATION;
import static co.edu.eafit.andromath.util.Constants.VARIABLE;
import static co.edu.eafit.andromath.util.Constants.ErrorCodes.INVALID_DELTA;
import static co.edu.eafit.andromath.util.Constants.ErrorCodes.INVALID_ITER;
import static co.edu.eafit.andromath.util.Constants.ErrorCodes.X_ROOT;

public class IncrementalSearchActivity extends AppCompatActivity {

    private static final String tag = IncrementalSearchActivity.class.getSimpleName();

    TextView function, result, iterations, xValue, solution;
    EditText x0Input, deltaInput, iterationsInput;
    TableLayout procedure;
    Expression expression;

    private List<TableRow> tableIterations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incremental_search);
        Objects.requireNonNull(getSupportActionBar()).hide();

        x0Input = (EditText) findViewById(R.id.editTextInitialValue);
        deltaInput = (EditText) findViewById(R.id.editTextDelta);
        iterationsInput = (EditText) findViewById(R.id.editTextIterations);

        result = (TextView) findViewById(R.id.textViewResult);
        function = (TextView) findViewById(R.id.textViewFunction);

        procedure = (TableLayout) findViewById(R.id.tableLayoutProcedure);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        String equation = "f(x) = " + intent.
                getStringExtra(EQUATION);
        function.setText(equation);

        expression = new Expression(intent.
                getStringExtra(EQUATION));

        procedure.setStretchAllColumns(true);
    }

    public void incrementalSearch(View v) {

        x0Input.setSelected(false);
        deltaInput.setSelected(false);
        iterationsInput.setSelected(false);
        result.setVisibility(View.VISIBLE);

        tableIterations = new ArrayList<>();

        procedure.removeViews(1,
                procedure.getChildCount() - 1);

        Pair<String, Boolean> solution =
                incrementalSearch(tableIterations);

        if (solution != null) {
            result.setText(solution.first);
            createTableProcedure(tableIterations);
            procedure.setVisibility(solution.second ?
                    View.VISIBLE : View.INVISIBLE);
        } else {
            Messages.invalidEquation(tag,
                    getApplicationContext());
        }
    }

    /**
     * @return Pair<String, Boolean>
     *     String parameter is the message.
     *     Boolean parameter is a flag to show the procedure.
     */
    private Pair<String, Boolean> incrementalSearch(List<TableRow> tableIterations) {

        String message;

        boolean displayProcedure;

        BigDecimal x0 = BigDecimal.valueOf(Double.
                parseDouble(x0Input.getText().toString()));

        BigDecimal delta = BigDecimal.valueOf(Double.
                parseDouble(deltaInput.getText().toString()));

        int iterations = Integer.parseInt(
                iterationsInput.getText().toString());

        try {
            if (delta.compareTo(BigDecimal.ZERO) == 0) {
                message = INVALID_DELTA.getMessage();
                displayProcedure = INVALID_DELTA.isDisplayProcedure();
            } else {
                if (iterations < 1) {
                    message = INVALID_ITER.getMessage();
                    displayProcedure = INVALID_ITER.isDisplayProcedure();
                } else {
                    BigDecimal y0 = expression.with(VARIABLE, x0).eval();

                    if (y0.compareTo(BigDecimal.ZERO) == 0) {
                        message = X_ROOT.getMessage();
                        displayProcedure = X_ROOT.isDisplayProcedure();
                    } else {
                        BigDecimal x1 = x0.add(delta);
                        BigDecimal y1 = expression.with(VARIABLE, x1).eval();

                        int count = 1;

                        tableIterations.add(createProcedureIteration(count, x0, y0, y1));

                        while (y1.compareTo(BigDecimal.ZERO) != 0 && y0.multiply(y1).
                                compareTo(BigDecimal.ZERO) > 0 && count < iterations) {



                            x0 = x1;
                            y0 = y1;
                            x1 = x0.add(delta);
                            y1 = expression.with(VARIABLE, x1).eval();
                            count++;

                            tableIterations.add(createProcedureIteration(count, x0, y0, y1));
                        }

                        if (y1.compareTo(BigDecimal.ZERO) == 0) {
                            tableIterations.add(createProcedureIteration(count + 1, x1, y1, y1));
                            message = "x = " + x1.toString() + " is a root";
                            displayProcedure = true;
                        } else if (y0.multiply(y1).compareTo(BigDecimal.ZERO) < 0) {
                            message = "There is a root between x0 = " + x0.
                                    toString() + " and x1 = " + x1.toString();
                            displayProcedure = true;
                        } else {
                            message = "The method failed after "
                                    + iterations + " iterations";
                            displayProcedure = false;
                        }
                    }
                }
            }
        } catch (Expression.ExpressionException
                | ArithmeticException | NumberFormatException e) {
            return null; // The equation is not valid.
        }

        return new Pair(message, displayProcedure);
    }

    private TableRow createProcedureIteration(int count, BigDecimal x1,
                                              BigDecimal y0, BigDecimal y1) {

        TableRow iterationResult = new TableRow(this);

        iterations = new TextView(this);
        iterations.setGravity(Gravity.CENTER);
        iterations.setText(String.valueOf(count));

        xValue = new TextView(this);
        xValue.setGravity(Gravity.CENTER);
        xValue.setText(x1.toString());

        solution = new TextView(this);
        solution.setGravity(Gravity.CENTER);
        solution.setText(y0.toString());

        iterationResult.addView(iterations);
        iterationResult.addView(xValue);
        iterationResult.addView(solution);

        return iterationResult;
    }

    private void createTableProcedure(List<TableRow> tableIterations) {

        for (TableRow tableRow : tableIterations) {
            procedure.addView(tableRow);
        }
    }
}