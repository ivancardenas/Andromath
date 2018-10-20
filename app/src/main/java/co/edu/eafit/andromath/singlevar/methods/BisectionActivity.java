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

public class BisectionActivity extends AppCompatActivity {

    private static final String tag = BisectionActivity.class.getSimpleName();   //

    EditText xmin_et, xmax_et, tol_et, niter_et;
    TextView func, results, iterations, solution, xmin, xmax, xmed, tol, fa, fb;
    Expression expr;
    int scale=5;
    TableLayout procedure;  //
    Expression expression;  //

    private List<TableRow> tableIterations;  //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bisection);
        Objects.requireNonNull(getSupportActionBar()).hide();   //
        procedure = (TableLayout) findViewById(R.id.tableLayoutProcedure);   //

        xmin_et = (EditText)findViewById(R.id.bisection_xmin);
        xmax_et = (EditText)findViewById(R.id.bisection_xmax);
        tol_et  = (EditText)findViewById(R.id.bisection_tolerance);
        niter_et = (EditText)findViewById(R.id.bisection_niter);
        func = (TextView)findViewById(R.id.bisection_func);
        results = (TextView)findViewById(R.id.bisection_result);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent i = getIntent();
        String s = "f(x) = " + i.getStringExtra("equation");
        func.setText(s);
        //System.out.println(i.getStringExtra("equation"));
        expr = new Expression(i.getStringExtra("equation"));

        procedure.setStretchAllColumns(true);   //
    }

    public void Bisection(View v) { //
        tableIterations = new ArrayList<>();

        procedure.removeViews(1,
                procedure.getChildCount() - 1);

        Pair<String, Boolean> solution =
                Bisection(tableIterations);

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
     * @return Pair<String , Boolean>
     * String parameter is the message.
     * Boolean parameter is a flag to show the procedure.
     */

    private Pair<String, Boolean> Bisection(List<TableRow> tableIterations){

        String message;

        boolean displayProcedure=true;
        results.setVisibility(View.VISIBLE);

        InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        BigDecimal xi = BigDecimal.valueOf(Double.parseDouble(xmin_et.getText().toString()));
        BigDecimal xs = BigDecimal.valueOf(Double.parseDouble(xmax_et.getText().toString()));
        BigDecimal tol = BigDecimal.valueOf(Double.parseDouble(tol_et.getText().toString()));
        BigDecimal xaux;
        int niter = Integer.parseInt(niter_et.getText().toString());

        //Method Begins
        //yi = f(xi)
        //ys = f(xs)
        try {

            BigDecimal yi = expr.with("x", xi).eval();
            BigDecimal ys = expr.with("x", xs).eval();
            //yi = 0
            if(niter < 1) {
                message = INVALID_ITER.getMessage();
                displayProcedure = INVALID_ITER.isDisplayProcedure();
            }else if (yi.compareTo(BigDecimal.ZERO) == 0) {
                message = X_ROOT.getMessage();
                displayProcedure = X_ROOT.isDisplayProcedure();
                //ys = 0

            } else if (ys.compareTo(BigDecimal.ZERO) == 0) {
                message = X_ROOT.getMessage();
                displayProcedure = X_ROOT.isDisplayProcedure();
                // ys*yi < 0
            } else if (yi.multiply(ys).compareTo(BigDecimal.ZERO) < 0) {
                //xm = (xi + xs)/2
                BigDecimal xm = (xi.add(xs)).divide(BigDecimal.valueOf(2)/*, BigDecimal.ROUND_HALF_EVEN*/);
                //ym = f(xm)
                BigDecimal ym = expr.with("x", xm).eval();
                int count = 1;
                //error = tol + 1
                BigDecimal error = tol.add(BigDecimal.ONE);
                String tempscale=tol_et.getText().toString();
                scale=tempscale.substring(tempscale.indexOf('.')).length();



                BigDecimal xii= xi.setScale(scale,BigDecimal.ROUND_HALF_EVEN);
                BigDecimal xss= xs.setScale(scale,BigDecimal.ROUND_HALF_EVEN);
                BigDecimal yii= yi.setScale(scale,BigDecimal.ROUND_HALF_EVEN);
                BigDecimal yss= ys.setScale(scale,BigDecimal.ROUND_HALF_EVEN);
                BigDecimal xmm= xm.setScale(scale,BigDecimal.ROUND_HALF_EVEN);
                BigDecimal ymm= ym.setScale(scale,BigDecimal.ROUND_HALF_EVEN);
                BigDecimal errorr= error.setScale(scale,BigDecimal.ROUND_HALF_EVEN);
                //error > tol && ym != 0 && count < niter
                tableIterations.add(createProcedureIteration(count, xii, xss, yii, yss, xmm, ymm, errorr));
                //while ( ym != 0 and e > tol and count < iter) do
                while (ym.compareTo(BigDecimal.ZERO) != 0 && error.compareTo(tol) > 0 && count < niter) {
                    //yi*ys < 0
                    if (yi.multiply(ym).compareTo(BigDecimal.ZERO) < 0) {
                        xs = xm;
                        ys = ym;
                    } else {
                        xi = xm;
                        yi = ym;
                    }
                    xaux = xm;
                    //xm = (xi + xs)/
                    /*, BigDecimal.ROUND_HALF_EVEN*/
                    xm = (xi.add(xs)).divide(BigDecimal.valueOf(2));
                    //ym = f(xm)
                    ym = expr.with("x", xm).eval();
                    //error = abs(xm-xaux)
                    error = xm.subtract(xaux).abs();
                    count++;
                     xii= xi.setScale(scale,BigDecimal.ROUND_HALF_EVEN);
                     xss= xs.setScale(scale,BigDecimal.ROUND_HALF_EVEN);
                     yii= yi.setScale(scale,BigDecimal.ROUND_HALF_EVEN);
                     yss= ys.setScale(scale,BigDecimal.ROUND_HALF_EVEN);
                     xmm= xm.setScale(scale,BigDecimal.ROUND_HALF_EVEN);
                     ymm= ym.setScale(scale,BigDecimal.ROUND_HALF_EVEN);
                     errorr= error.setScale(scale,BigDecimal.ROUND_HALF_EVEN);



                    tableIterations.add(createProcedureIteration(count, xii, xss, yii, yss, xmm, ymm, errorr));
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
                            + count + " iterations";
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

        fa = new TextView(this);
        fa.setGravity(Gravity.CENTER);
        fa.setText(yi.toString());

        xmax = new TextView(this);
        xmax.setGravity(Gravity.CENTER);
        xmax.setText(xs.toString());

        fb = new TextView(this);
        fb.setGravity(Gravity.CENTER);
        fb.setText(ys.toString());

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
        iterationResult.addView(fa);
        iterationResult.addView(xmax);
        iterationResult.addView(fb);
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