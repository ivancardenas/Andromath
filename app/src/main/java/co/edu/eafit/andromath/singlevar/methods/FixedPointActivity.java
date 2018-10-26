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

public class FixedPointActivity extends AppCompatActivity {

    private static final String tag = FixedPointActivity.class.getSimpleName();   //

    EditText xa_et, gx_et, tol_et, niter_et;
    TextView func,results, iterations, tol, x, fx;
    Expression expr, gexpr;
    int scale=5;
    TableLayout procedure;  //
    Expression expression;  //

    private List<TableRow> tableIterations;  //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_point);
        Objects.requireNonNull(getSupportActionBar()).hide();   //
        procedure = (TableLayout) findViewById(R.id.tableLayoutProcedure);   //

        xa_et = (EditText)findViewById(R.id.fixed_point_xa);
        gx_et = (EditText)findViewById(R.id.fixed_point_gx);
        tol_et  = (EditText)findViewById(R.id.fixed_point_tolerance);
        niter_et = (EditText)findViewById(R.id.fixed_point_niter);
        func = (TextView)findViewById(R.id.fixed_point_func);
        results = (TextView) findViewById(R.id.fixed_point_result);
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

    public void FixedPoint(View v) { //
        tableIterations = new ArrayList<>();

        procedure.removeViews(1,
                procedure.getChildCount() - 1);

        Pair<String, Boolean> solution =
                runFixedPoint(tableIterations);

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

    private Pair<String, Boolean> runFixedPoint(List<TableRow> tableIterations){

        String message;

        boolean displayProcedure=true;

        InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        results.setVisibility(View.VISIBLE);


        try{
            BigDecimal xa = BigDecimal.valueOf(Double.parseDouble(xa_et.getText().toString()));
            BigDecimal tol = BigDecimal.valueOf(Double.parseDouble(tol_et.getText().toString()));
            BigDecimal xn;
            int niter = Integer.parseInt(niter_et.getText().toString());
            String tempscale=tol_et.getText().toString();
            scale=tempscale.substring(tempscale.indexOf('.')).length();
            //We have to trust in whoever set up the expression to not screw things up
            //TODO: We have to add checks to this function, otherwise this might crash the app.
            gexpr = new Expression(gx_et.getText().toString());


            BigDecimal y = expr.with("x", xa).eval();
            int count = 0;
            BigDecimal error = tol.add(BigDecimal.ONE);
            String xaa= conversion(xa.setScale(scale,BigDecimal.ROUND_HALF_EVEN),0);
            String yaa= conversion(y.setScale(scale,BigDecimal.ROUND_HALF_EVEN),0);
            String errorr= conversion(error.setScale(scale,BigDecimal.ROUND_HALF_EVEN),0);

            tableIterations.add(createProcedureIteration(count, xaa, yaa, errorr));

            if (niter < 1) {
                message = INVALID_ITER.getMessage();
                displayProcedure = INVALID_ITER.isDisplayProcedure();
            }else if (y.compareTo(BigDecimal.ZERO)==0) {
                message=X_ROOT.getMessage();
                displayProcedure = X_ROOT.isDisplayProcedure();
            }else{
                //Method Begins
                while (y.compareTo(BigDecimal.ZERO) != 0 && error.compareTo(tol) > 0 && count < niter) {
                    xn = gexpr.with("x", xa).eval();
                    y = expr.with("x", xn).eval();
                    error = xn.subtract(xa).abs();
                    xa = xn;
                    count++;

                     xaa= conversion(xa.setScale(scale,BigDecimal.ROUND_HALF_EVEN),0);
                     yaa= conversion(y.setScale(scale,BigDecimal.ROUND_HALF_EVEN),0);
                     errorr= conversion(error.setScale(scale,BigDecimal.ROUND_HALF_EVEN),0);

                    tableIterations.add(createProcedureIteration(count, xaa, yaa, errorr));
                }
                if (y.compareTo(BigDecimal.ZERO) == 0) {
                    tableIterations.add(createProcedureIteration(count, xaa, yaa, errorr));
                    message = "x = " + xa.toString() + " is a root";
                    displayProcedure = true;
                } else if (error.compareTo(tol) < 0) {
                    tableIterations.add(createProcedureIteration(count, xaa, yaa, errorr));
                    message = "x = " + xa.toString() + " is an approximated root\nwith E = " + error.toString();
                    displayProcedure = true;
                } else {
                    message = "The method failed after "
                            + niter + " iterations";
                    displayProcedure = false;
                }
            }
        }catch (Expression.ExpressionException e) {
            return null;
        }catch ( ArithmeticException | NumberFormatException e){
            displayProcedure=false;
            message="OUT RANGE OR ARE MISSING DATA FIELDS";}
        return new Pair(message, displayProcedure);
    }

    private TableRow createProcedureIteration(int count, String xa,
                                              String y, String Error) {
        TableRow iterationResult = new TableRow(this);

        iterations = new TextView(this);
        iterations.setGravity(Gravity.CENTER);
        iterations.setText(String.valueOf(count));

        x = new TextView(this);
        x.setGravity(Gravity.CENTER);
        x.setText(xa.toString());

        fx = new TextView(this);
        fx.setGravity(Gravity.CENTER);
        fx.setText(y.toString());

        tol = new TextView(this);
        tol.setGravity(Gravity.CENTER);
        tol.setText(Error.toString());

        iterationResult.addView(iterations);
        iterationResult.addView(x);
        iterationResult.addView(fx);
        iterationResult.addView(tol);

        return iterationResult;
    }

    private void createTableProcedure(List<TableRow> tableIterations) {

        for (TableRow tableRow : tableIterations) {
            procedure.addView(tableRow);
        }
    }
    int veces=0;
    public  String conversionAux(BigDecimal l){

        if(l.toString().charAt(0)=='0' || (l.toString().length()>1 && l.toString().substring(0,2).equals("-0"))){
            veces++;
            return conversionAux(l.movePointRight(1));
        }
        else{
            if(veces!=0){
                return l+"E-"+veces;}
            else{return l.toString();}
        }

    }
    public String conversion(BigDecimal l, int zero){
        veces=zero;
        return conversionAux(l);

    }
}