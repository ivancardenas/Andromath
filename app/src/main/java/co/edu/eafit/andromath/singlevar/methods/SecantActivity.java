package co.edu.eafit.andromath.singlevar.methods;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import android.view.View;
import android.util.Pair;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import co.edu.eafit.andromath.util.Messages;
import static co.edu.eafit.andromath.util.Constants.EQUATION;
import static co.edu.eafit.andromath.util.Constants.VARIABLE;
import static co.edu.eafit.andromath.util.Constants.ErrorCodes.INVALID_ITER;
import static co.edu.eafit.andromath.util.Constants.ErrorCodes.X_ROOT;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;

import co.edu.eafit.andromath.R;

public class SecantActivity extends AppCompatActivity {

    private static final String tag = SecantActivity.class.getSimpleName();   //

    EditText x0_et, x1_et, tol_et, niter_et;
    TextView func, results, x1a, x2a, fx1, fx2, tol, iterations;
    Expression expr;

    TableLayout procedure;  //
    Expression expression;  //

    private List<TableRow> tableIterations;  //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secant);
        Objects.requireNonNull(getSupportActionBar()).hide();   //
        procedure = (TableLayout) findViewById(R.id.tableLayoutProcedure);   //

        x0_et = (EditText)findViewById(R.id.secant_x0);
        x1_et = (EditText)findViewById(R.id.secant_x1);
        tol_et  = (EditText)findViewById(R.id.secant_tolerance);
        niter_et = (EditText)findViewById(R.id.secant_niter);
        func = (TextView)findViewById(R.id.secant_func);
        results = (TextView)findViewById(R.id.secant_result);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent i = getIntent();
        String s = "f(x) = " + i.getStringExtra("equation");
        func.setText(s);
        expr = new Expression(i.getStringExtra("equation"));

        procedure.setStretchAllColumns(true);   //
    }

    public void Secant(View v) { //
        tableIterations = new ArrayList<>();

        procedure.removeViews(1,
                procedure.getChildCount() - 1);

        Pair<String, Boolean> solution =
                runSecant(tableIterations);

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

    public Pair<String, Boolean> runSecant(List<TableRow> tableIterations){

        String message;
        boolean displayProcedure;

        InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        results.setVisibility(View.VISIBLE);

        String temp;
        BigDecimal x0 = BigDecimal.valueOf(Double.parseDouble(x0_et.getText().toString()));
        BigDecimal x1 = BigDecimal.valueOf(Double.parseDouble(x1_et.getText().toString()));
        BigDecimal tol = BigDecimal.valueOf(Double.parseDouble(tol_et.getText().toString()));
        BigDecimal x2;
        int niter = Integer.parseInt(niter_et.getText().toString());
        if (niter < 1) {
            message = INVALID_ITER.getMessage();
            displayProcedure = INVALID_ITER.isDisplayProcedure();
        }
        //Method Begins
        BigDecimal y0 = expr.with("x",x0).eval();
        if(y0.compareTo(BigDecimal.ZERO) == 0){
            message = X_ROOT.getMessage();
            displayProcedure = X_ROOT.isDisplayProcedure();
        } else {
            BigDecimal y1 = expr.with("x",x1).eval();
            int count = 0;
            BigDecimal error = tol.add(BigDecimal.ONE);
            BigDecimal den = y1.subtract(y0);

            tableIterations.add(createProcedureIteration(count, x0, x1, y0, y1, error));
            while(error.compareTo(tol) > 0 && y1.compareTo(BigDecimal.ZERO) != 0 && den.compareTo(BigDecimal.ZERO) != 0 && count < niter){
                //x2 = x1 - (y1*(x1-x0)/den)
                x2 = x1.subtract(y1.multiply(x1.subtract(x0)).divide(den,BigDecimal.ROUND_HALF_EVEN));
                error = x2.subtract(x1).abs();
                x0 = x1;
                y0 = y1;
                x1 = x2;
                y1 = expr.with("x",x1).eval();
                den = y1.subtract(y0);
                count++;

                tableIterations.add(createProcedureIteration(count, x0, x1, y0, y1, error));
            }
            if (y1.compareTo(BigDecimal.ZERO) == 0) {
                message = "x = " + x1.toString() + " is a root";
                displayProcedure = true;
            } else if (error.compareTo(tol) < 0) {
                message = "x = " + x1.toString() + " is an approximated root\nwith E = " + error.toString();
                displayProcedure = true;
            } else if(den.compareTo(BigDecimal.ZERO) == 0){
                message = "There are possibly multiple roots";
                displayProcedure = false;
            } else {
                message = "The method failed after "
                        + count + " iterations";
                displayProcedure = true;
            }
        }
        return new Pair(message, displayProcedure);
    }

    private TableRow createProcedureIteration(int count, BigDecimal x1,
                                              BigDecimal x2, BigDecimal y1, BigDecimal y2,
                                              BigDecimal Error) {
        TableRow iterationResult = new TableRow(this);

        iterations = new TextView(this);
        iterations.setGravity(Gravity.CENTER);
        iterations.setText(String.valueOf(count));

        x1a = new TextView(this);
        x1a.setGravity(Gravity.CENTER);
        x1a.setText(x1.toString());

        fx1 = new TextView(this);
        fx1.setGravity(Gravity.CENTER);
        fx1.setText(y1.toString());

        x2a = new TextView(this);
        x2a.setGravity(Gravity.CENTER);
        x2a.setText(x2.toString());

        fx2 = new TextView(this);
        fx2.setGravity(Gravity.CENTER);
        fx2.setText(y2.toString());

        tol = new TextView(this);
        tol.setGravity(Gravity.CENTER);
        tol.setText(Error.toString());

        iterationResult.addView(iterations);
        iterationResult.addView(x1a);
        iterationResult.addView(fx1);
        iterationResult.addView(x2a);
        iterationResult.addView(fx2);
        iterationResult.addView(tol);

        return iterationResult;
    }

    private void createTableProcedure(List<TableRow> tableIterations) {

        for (TableRow tableRow : tableIterations) {
            procedure.addView(tableRow);
        }
    }
}