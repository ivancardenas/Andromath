package co.edu.eafit.andromath.singlevar.methods;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Objects;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Pair;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import static co.edu.eafit.andromath.util.Constants.ErrorCodes.INVALID_ITER;
import static co.edu.eafit.andromath.util.Constants.ErrorCodes.X_ROOT;

public class FalseRuleActivity extends AppCompatActivity {

    private static final String tag = FalseRuleActivity.class.getSimpleName();   //

    EditText xmin_et, xmax_et, tol_et, niter_et;
    TextView func, results, iterations, solution, xmin, xmax, xmed, tol; //
    Expression expr;

    TableLayout procedure;  //
    Expression expression;  //

    private List<TableRow> tableIterations;  //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_false_rule);
        Objects.requireNonNull(getSupportActionBar()).hide();   //
        procedure = (TableLayout) findViewById(R.id.tableLayoutProcedure);   //

        xmin_et = (EditText) findViewById(R.id.falserule_xmin);
        xmax_et = (EditText) findViewById(R.id.falserule_xmax);
        tol_et = (EditText) findViewById(R.id.falserule_tolerance);
        niter_et = (EditText) findViewById(R.id.falserule_niter);
        func = (TextView) findViewById(R.id.falserule_func);
        results = (TextView) findViewById(R.id.falserule_result);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = getIntent();
        String s = "f(x) = " + i.getStringExtra("equation");
        func.setText(s);
        expr = new Expression(i.getStringExtra("equation"));

        procedure.setStretchAllColumns(true);   //
    }

    public void FalseRule(View v) { //
        tableIterations = new ArrayList<>();

        procedure.removeViews(1,
                procedure.getChildCount() - 1);

        Pair<String, Boolean> solution =
                FalseRule(tableIterations);

        if (solution != null) {
            results.setText(solution.first);
            createTableProcedure(tableIterations);
            procedure.setVisibility(solution.second ?
                    View.VISIBLE : View.INVISIBLE);
        } else {
            Messages.invalidEquation(tag,
                    getApplicationContext());
        }
    }

    /**
     * @return Pair<String ,   B oolean>
     * String parameter is the message.
     * Boolean parameter is a flag to show the procedure.
     */

    private Pair<String, Boolean> FalseRule(List<TableRow> tableIterations) {

        String message;

        boolean displayProcedure;

        InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        //results.setVisibility(View.VISIBLE);

        String temp;
        BigDecimal xi = BigDecimal.valueOf(Double.parseDouble(xmin_et.getText().toString()));
        BigDecimal xs = BigDecimal.valueOf(Double.parseDouble(xmax_et.getText().toString()));
        BigDecimal tol = BigDecimal.valueOf(Double.parseDouble(tol_et.getText().toString()));
        BigDecimal xaux;
        int iterations = Integer.parseInt(niter_et.getText().toString());
        if (iterations < 1) {
            message = INVALID_ITER.getMessage();
            displayProcedure = INVALID_ITER.isDisplayProcedure();
        }
        //Method Begins
        //yi = f(xi)
        //ys = f(xs)
        BigDecimal yi = expr.with("x", xi).eval();
        BigDecimal ys = expr.with("x", xs).eval();
        //yi = 0
        try {
            if (yi.compareTo(BigDecimal.ZERO) == 0) {
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
                BigDecimal ym = expr.with("x", xm).eval();
                int count = 1;
                BigDecimal error = tol.add(BigDecimal.ONE);
                tableIterations.add(createProcedureIteration(count, xi, xs, yi, ys, xm, ym, error));
                //error = tol + 1
                //error > tol && ym != 0 && count < niter
                while (error.compareTo(tol) > 0 && ym.compareTo(BigDecimal.ZERO) != 0 && count < iterations) {
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
                    ym = expr.with("x", xm).eval();
                    //error = abs(xm-xaux)
                    error = xm.subtract(xaux).abs();
                    count++;

                    tableIterations.add(createProcedureIteration(count, xi, xs, yi, ys, xm, ym, error));
                }
                if (ym.compareTo(BigDecimal.ZERO) == 0) {
                    tableIterations.add(createProcedureIteration(count + 1, xi, xs, yi, ys, xm, ym, error));
                    message = "x = " + xm.toString() + " is a root";
                    displayProcedure = true;
                } else if (error.compareTo(tol) < 0) {
                    tableIterations.add(createProcedureIteration(count + 1, xi, xs, yi, ys, xm, ym, error));
                    message = "x = " + xm.toString() + " is an approximated root\nwith E = " + error.toString();
                    displayProcedure = true;
                } else {
                    message = "The method failed after "
                            + iterations + " iterations";
                    displayProcedure = false;
                }
            } else {
                message = "NOT SUITABLE RANGE";
                displayProcedure = false;
            }
        }catch (Expression.ExpressionException
                | ArithmeticException | NumberFormatException e) {
            return null; // The equation is not valid.
        }
        return new Pair(message, displayProcedure);
    }

    private TableRow createProcedureIteration(int count, BigDecimal xi,
                                              BigDecimal xs, BigDecimal yi, BigDecimal ys,
                                              BigDecimal xm, BigDecimal ym, BigDecimal Error) {
        TableRow iterationResult = new TableRow(this);

        iterations = new TextView(this);
        iterations.setGravity(Gravity.CENTER);
        iterations.setText(String.valueOf(count));

        xmin = new TextView(this);
        xmin.setGravity(Gravity.CENTER);
        xmin.setText(xi.toString());

        /*solution = new TextView(this);
        solution.setGravity(Gravity.CENTER);
        solution.setText(yi.toString());*/

        xmax = new TextView(this);
        xmax.setGravity(Gravity.CENTER);
        xmax.setText(xs.toString());

       /* solution = new TextView(this);
        solution.setGravity(Gravity.CENTER);
        solution.setText(ys.toString());*/

        xmed = new TextView(this);
        xmed.setGravity(Gravity.CENTER);
        xmed.setText(xm.toString());

        solution = new TextView(this);
        solution.setGravity(Gravity.CENTER);
        solution.setText(ym.toString());

        tol = new TextView(this);
        tol.setGravity(Gravity.CENTER);
        tol.setText(Error.toString());

        iterationResult.addView(iterations);
        iterationResult.addView(xmin);
        iterationResult.addView(xmax);
        iterationResult.addView(xmed);
        iterationResult.addView(solution);
        iterationResult.addView(tol);

        return iterationResult;
    }

        private void createTableProcedure(List<TableRow> tableIterations) {

            for (TableRow tableRow : tableIterations) {
                procedure.addView(tableRow);
            }
        }
    }

