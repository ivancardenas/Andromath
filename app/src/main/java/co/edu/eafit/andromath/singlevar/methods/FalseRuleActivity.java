package co.edu.eafit.andromath.singlevar.methods;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import co.edu.eafit.andromath.R;
import co.edu.eafit.andromath.util.Messages;

import static co.edu.eafit.andromath.util.Constants.EQUATION;
import static co.edu.eafit.andromath.util.Constants.ErrorCodes.INVALID_ITER;
import static co.edu.eafit.andromath.util.Constants.ErrorCodes.INVALID_RANGE;
import static co.edu.eafit.andromath.util.Constants.ErrorCodes.OUT_OF_RANGE;
import static co.edu.eafit.andromath.util.Constants.ErrorCodes.X_ROOT;
import static co.edu.eafit.andromath.util.Constants.NOTATION_FORMAT;
import static co.edu.eafit.andromath.util.Constants.VARIABLE;

public class FalseRuleActivity extends AppCompatActivity {

    private static final String tag = FalseRuleActivity.class.getSimpleName();

    EditText xMinInput, xMaxInput, toleranceInput, iterationsInput;
    TextView function, result, iterations, solution, xMin,
            xMed, xMax, tolerance, solutionA, solutionB;
    Expression expression;
    TableLayout procedure;

    private List<TableRow> tableIterations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_false_rule);
        Objects.requireNonNull(getSupportActionBar()).hide();

        xMinInput = (EditText) findViewById(R.id.falserule_xmin);
        xMaxInput = (EditText) findViewById(R.id.falserule_xmax);
        toleranceInput = (EditText) findViewById(R.id.falserule_tolerance);
        iterationsInput = (EditText) findViewById(R.id.falserule_niter);

        function = (TextView) findViewById(R.id.textViewFunction);
        result = (TextView) findViewById(R.id.falserule_result);

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

    public void falseRule(View v) {

        xMinInput.setSelected(false);
        xMaxInput.setSelected(false);
        toleranceInput.setSelected(false);
        iterationsInput.setSelected(false);
        result.setVisibility(View.VISIBLE);

        tableIterations = new ArrayList<>();

        procedure.removeViews(1,
                procedure.getChildCount() - 1);

        Pair<String, Boolean> solution =
                falseRule(tableIterations);

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
    private Pair<String, Boolean> falseRule(List<TableRow> tableIterations) {

        String message;

        boolean displayProcedure;

        try {
            String temp;
            BigDecimal xi = BigDecimal.valueOf(Double.parseDouble(xMinInput.getText().toString()));
            BigDecimal xs = BigDecimal.valueOf(Double.parseDouble(xMaxInput.getText().toString()));
            BigDecimal tol = BigDecimal.valueOf(Double.parseDouble(toleranceInput.getText().toString()));

            BigDecimal xaux;
            int iterations = Integer.parseInt(iterationsInput.getText().toString());
            //yi = f(xi)
            //ys = f(xs)
            BigDecimal yi = expression.with(VARIABLE, xi).eval();
            BigDecimal ys = expression.with(VARIABLE, xs).eval();
            //yi = 0
            if (iterations < 1) {
                message = INVALID_ITER.getMessage();
                displayProcedure = INVALID_ITER.isDisplayProcedure();
            }
            else if (yi.compareTo(BigDecimal.ZERO) == 0) {
                message = X_ROOT.getMessage();
                displayProcedure = X_ROOT.isDisplayProcedure();
                //ys = 0
            } else if (ys.compareTo(BigDecimal.ZERO) == 0) {
                message = X_ROOT.getMessage();
                displayProcedure = X_ROOT.isDisplayProcedure();
                // ys*yi < 0
            } else if (yi.multiply(ys).compareTo(BigDecimal.ZERO) < 0) {
                //xm = xi - ((yi*(xs-xi))/(ys-yi))
                BigDecimal xm = xi.subtract(yi.multiply(xs.subtract(xi)).divide(ys.subtract(yi), BigDecimal.ROUND_HALF_EVEN));
                //ym = f(xm)
                BigDecimal ym = expression.with(VARIABLE, xm).eval();
                int count = 1;
                BigDecimal error = tol.add(BigDecimal.ONE);

                BigDecimal xii= xi;
                BigDecimal xss= xs;
                BigDecimal yii= yi;
                BigDecimal yss= ys;
                BigDecimal xmm= xm;
                BigDecimal ymm= ym;
                BigDecimal errorr= error;

                tableIterations.add(createProcedureIteration(count, xii, xss, yii, yss, xmm, ymm,errorr));

                while (ym.compareTo(BigDecimal.ZERO) != 0 && error.compareTo(tol) > 0 && count < iterations) {
                    //yi*ys < 0
                    if (yi.multiply(ym).compareTo(BigDecimal.ZERO) < 0) {
                        xs = xm;
                        ys = ym;
                    } else {
                        xi = xm;
                        yi = ym;
                    }
                    xaux = xm;
                    //xm = (xi + xs)/2
                    xm = xi.subtract(yi.multiply(xs.subtract(xi)).divide(ys.subtract(yi), BigDecimal.ROUND_HALF_EVEN));
                    //ym = f(xm)
                    ym = expression.with(VARIABLE, xm).eval();
                    //error = abs(xm-xaux)
                    error = xm.subtract(xaux).abs();
                    count++;

                     xii= xi;
                     xss= xs;
                     yii= yi;
                     yss= ys;
                     xmm= xm;
                     ymm= ym;
                     errorr= error;

                    tableIterations.add(createProcedureIteration(count, xii, xss, yii, yss, xmm, ymm, errorr));
                }
                if (ym.compareTo(BigDecimal.ZERO) == 0) {
                    tableIterations.add(createProcedureIteration(count + 1, xii, xss, yii, yss, xmm, ymm, errorr));
                    message = "x = " + xm.toString() + " is a root";
                    displayProcedure = true;
                } else if (error.compareTo(tol) < 0) {
                    tableIterations.add(createProcedureIteration(count + 1, xii, xss, yii, yss, xmm, ymm, errorr));
                    message = "x = " + xm.toString() + " is an approximated root\nwith E = " + error.toString();
                    displayProcedure = true;
                } else {
                    message = "The method failed after "
                            + count + " iterations";
                    displayProcedure = false;
                }
            } else {
                displayProcedure = INVALID_RANGE.isDisplayProcedure();
                message = INVALID_RANGE.getMessage();
            }
        } catch (Expression.ExpressionException e) {
            return null; // The equation is not valid.
        } catch (ArithmeticException | NumberFormatException e){
            displayProcedure = OUT_OF_RANGE.isDisplayProcedure();
            message = OUT_OF_RANGE.getMessage();
        }

        return new Pair(message, displayProcedure);
    }

    private TableRow createProcedureIteration(int count, BigDecimal xi,
                                              BigDecimal xs, BigDecimal yi, BigDecimal ys,
                                              BigDecimal xm, BigDecimal ym, BigDecimal error) {

        TableRow iterationResult = new TableRow(this);

        NumberFormat formatter = new DecimalFormat(NOTATION_FORMAT);
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        formatter.setMinimumFractionDigits(3);

        iterations = new TextView(this);
        iterations.setPadding(15, 10, 15, 10);
        iterations.setGravity(Gravity.CENTER);
        iterations.setText(String.valueOf(count));

        xMin = new TextView(this);
        xMin.setPadding(15, 10, 15, 10);
        xMin.setGravity(Gravity.CENTER);
        xMin.setText(xi.toString());

        solutionA = new TextView(this);
        solutionA.setPadding(15, 10, 15, 10);
        solutionA.setGravity(Gravity.CENTER);
        solutionA.setText(formatter.format(yi));

        xMax = new TextView(this);
        xMax.setPadding(15, 10, 15, 10);
        xMax.setGravity(Gravity.CENTER);
        xMax.setText(xs.toString());

        solutionB = new TextView(this);
        solutionB.setPadding(15, 10, 15, 10);
        solutionB.setGravity(Gravity.CENTER);
        solutionB.setText(formatter.format(ys));

        xMed = new TextView(this);
        xMed.setPadding(15, 10, 15, 10);
        xMed.setGravity(Gravity.CENTER);
        xMed.setText(xm.toString());

        solution = new TextView(this);
        solution.setPadding(15, 10, 15, 10);
        solution.setGravity(Gravity.CENTER);
        solution.setText(formatter.format(ym));

        tolerance = new TextView(this);
        tolerance.setPadding(15, 10, 15, 10);
        tolerance.setGravity(Gravity.CENTER);
        tolerance.setText(formatter.format(error));

        iterationResult.addView(iterations);
        iterationResult.addView(xMin);
        iterationResult.addView(solutionA);
        iterationResult.addView(xMax);
        iterationResult.addView(solutionB);
        iterationResult.addView(xMed);
        iterationResult.addView(solution);
        iterationResult.addView(tolerance);

        return iterationResult;
    }

    private void createTableProcedure(List<TableRow> tableIterations) {

        for (TableRow tableRow : tableIterations) {
            procedure.addView(tableRow);
        }
    }
}