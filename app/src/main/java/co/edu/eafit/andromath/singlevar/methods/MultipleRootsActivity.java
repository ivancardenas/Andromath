package co.edu.eafit.andromath.singlevar.methods;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import static co.edu.eafit.andromath.util.Constants.ErrorCodes.INVALID_ITER;

public class MultipleRootsActivity extends AppCompatActivity {

        private static final String tag = MultipleRootsActivity.class.getSimpleName();
        EditText xa_et, gx_et, jx_et, tol_et, niter_et;
        TextView func, results, iterations,xa, ya, dya,ddya, tol;
        Expression expr, gexpr, jexpr;
        int scale=5;
        private List<TableRow> tableIterations;
        TableLayout procedure;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_multiple_roots);

            Objects.requireNonNull(getSupportActionBar()).hide();   //
            procedure = (TableLayout) findViewById(R.id.tableLayoutProcedure);



            xa_et = (EditText)findViewById(R.id.multiple_root_xa);
            gx_et = (EditText)findViewById(R.id.multiple_root_gx);
            jx_et = (EditText)findViewById(R.id.multiple_root_jx);
            tol_et  = (EditText)findViewById(R.id.multiple_root_tolerance);
            niter_et = (EditText)findViewById(R.id.multiple_root_niter);
            func = (TextView)findViewById(R.id.multiple_root_func);
            results = (TextView)findViewById(R.id.multiple_root_result);
        }

        @Override
        protected void onStart(){
            super.onStart();
            Intent i = getIntent();
            String s = "f(x) = " + i.getStringExtra("equation");
            func.setText(s);
            expr = new Expression(i.getStringExtra("equation"));

            procedure.setStretchAllColumns(true);
        }

        public void runMultipleRoot(View v) { //
            tableIterations = new ArrayList<>();

            procedure.removeViews(1,
                    procedure.getChildCount() - 1);

            Pair<String, Boolean> solution =
                    MultipleRoot(tableIterations);

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

        public Pair<String, Boolean> MultipleRoot(List<TableRow> tableIterations){

            String message="NOT SUITABLE RANGE";
            boolean displayProcedure = true;


            InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            results.setVisibility(View.VISIBLE);
            try {
                String temp;
                BigDecimal x0 = BigDecimal.valueOf(Double.parseDouble(xa_et.getText().toString()));
                BigDecimal tol = BigDecimal.valueOf(Double.parseDouble(tol_et.getText().toString()));
                BigDecimal x1;
                int niter = Integer.parseInt(niter_et.getText().toString());
                //We have to trust in whoever set up the expressionF to not screw things up
                //TODO: We have to add checks to this function, otherwise this might crash the app.
                gexpr = new Expression(gx_et.getText().toString());
                jexpr = new Expression(jx_et.getText().toString());
                //expressionF = new Expression("(x^3)+(4*x^2)-10");
                //expressionG = new Expression("(3*x^2)+(8*x)");
                //jexpr = new Expression("(6*x)+8");
                if (niter < 1) {
                    message = INVALID_ITER.getMessage();
                    displayProcedure = INVALID_ITER.isDisplayProcedure();

                }
                //Method Begins
                BigDecimal y = expr.with("x", x0).eval();
                BigDecimal dy = gexpr.with("x", x0).eval();
                BigDecimal ddy = jexpr.with("x", x0).eval();
                //dy^2 - y*ddy
                BigDecimal den = dy.pow(2).subtract(y.multiply(ddy));
                int count = 0;
                BigDecimal error = tol.add(BigDecimal.ONE);
                String tempscale = tol_et.getText().toString();
                scale = tempscale.substring(tempscale.indexOf('.')).length();
                String x00 = conversion(x0.setScale(scale, BigDecimal.ROUND_HALF_EVEN),0);
                String yy = conversion(y.setScale(scale, BigDecimal.ROUND_HALF_EVEN),0);
                String dyy = conversion(dy.setScale(scale, BigDecimal.ROUND_HALF_EVEN),0);
                String ddyy = conversion(ddy.setScale(scale, BigDecimal.ROUND_HALF_EVEN),0);
                String errorr = conversion(error.setScale(scale, BigDecimal.ROUND_HALF_EVEN),0);

                tableIterations.add(createProcedureIteration(count + 1, x00, yy, dyy, ddyy, errorr));

                while (error.compareTo(tol) > 0 && y.compareTo(BigDecimal.ZERO) != 0 && den.compareTo(BigDecimal.ZERO) != 0 && count < niter) {
                    //x1 = x0 - (y*dy)/den
                    x1 = x0.subtract(y.multiply(dy).divide(den, 5, BigDecimal.ROUND_HALF_EVEN));
                    y = expr.with("x", x1).eval();
                    dy = gexpr.with("x", x1).eval();
                    ddy = jexpr.with("x", x1).eval();
                    den = dy.pow(2).subtract(y.multiply(ddy));
                    error = x1.subtract(x0).abs();
                    x0 = x1;
                    count++;

                     x00 = conversion(x0.setScale(scale, BigDecimal.ROUND_HALF_EVEN),0);
                     yy = conversion(y.setScale(scale, BigDecimal.ROUND_HALF_EVEN),0);
                     dyy = conversion(dy.setScale(scale, BigDecimal.ROUND_HALF_EVEN),0);
                     ddyy = conversion(ddy.setScale(scale, BigDecimal.ROUND_HALF_EVEN),0);
                     errorr = conversion(error.setScale(scale, BigDecimal.ROUND_HALF_EVEN),0);

                    tableIterations.add(createProcedureIteration(count + 1, x00, yy, dyy, ddyy, errorr));

                }
                if (y.compareTo(BigDecimal.ZERO) == 0) {
                    message = "x = " + x0.toString() + " is a root";
                    tableIterations.add(createProcedureIteration(count + 1, x00, yy, dyy, ddyy, errorr));
                    displayProcedure = true;
                } else if (error.compareTo(tol) < 0) {
                    message = "x = " + x0.toString() + " is an approximated root\nwith E = " + error.toString();
                    tableIterations.add(createProcedureIteration(count + 1, x00, yy, dyy, ddyy, errorr));
                    displayProcedure = true;
                } else if (den.compareTo(BigDecimal.ZERO) == 0) {
                    //TODO: Buscar la explicacion de este caso
                    message = "at x = " + x0.toString() + " the function behaves incorrectly";
                } else {
                    temp = "the method failed after " + niter + " iterations";
                    displayProcedure = false;
                }
            }catch (Expression.ExpressionException e) {
                return null;
            }catch ( ArithmeticException | NumberFormatException e){
                displayProcedure=false;
                message="OUT RANGE OR ARE MISSING DATA FIELDS";

            }
            return new Pair(message, displayProcedure);
        }
        private TableRow createProcedureIteration(int count, String x,
                                                  String y, String dy ,String ddy, String Error) {
            TableRow iterationResult = new TableRow(this);

            iterations = new TextView(this);
            iterations.setGravity(Gravity.CENTER);
            iterations.setText(String.valueOf(count));

            xa = new TextView(this);
            xa.setGravity(Gravity.CENTER);
            xa.setText(x.toString());

            ya = new TextView(this);
            ya.setGravity(Gravity.CENTER);
            ya.setText(y.toString());


            dya = new TextView(this);
            dya.setGravity(Gravity.CENTER);
            dya.setText(dy.toString());

            ddya = new TextView(this);
            ddya.setGravity(Gravity.CENTER);
            ddya.setText(dy.toString());

            tol = new TextView(this);
            tol.setGravity(Gravity.CENTER);
            tol.setText(Error.toString());

            iterationResult.addView(iterations);
            iterationResult.addView(xa);
            iterationResult.addView(ya);
            iterationResult.addView(dya);
            iterationResult.addView(ddya);
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
