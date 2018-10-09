package co.edu.eafit.andromath.singlevar.methods;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import co.edu.eafit.andromath.R;

import co.edu.eafit.andromath.R;
import co.edu.eafit.andromath.util.Messages;
import static co.edu.eafit.andromath.util.Constants.ErrorCodes.INVALID_ITER;
import static co.edu.eafit.andromath.util.Constants.ErrorCodes.X_ROOT;
import static com.udojava.evalex.Expression.e;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class NewtonActivity extends AppCompatActivity {

    private static final String tag = NewtonActivity.class.getSimpleName();

    EditText xa_et, gx_et, tol_et, niter_et;
        TextView func, results, iterations, xa,ya, dya, tol;
        Expression expr, gexpr;

        private List<TableRow> tableIterations;
        TableLayout procedure;  //


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_newton);

            Objects.requireNonNull(getSupportActionBar()).hide();   //
            procedure = (TableLayout) findViewById(R.id.tableLayoutProcedure);

            xa_et = (EditText)findViewById(R.id.newton_xa);
            gx_et = (EditText)findViewById(R.id.newton_gx);
            tol_et  = (EditText)findViewById(R.id.newton_tolerance);
            niter_et = (EditText)findViewById(R.id.newton_niter);
            func = (TextView)findViewById(R.id.newton_func);
            results = (TextView)findViewById(R.id.newton_result);
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

        public void Newton(View v) { //
            tableIterations = new ArrayList<>();

            procedure.removeViews(1,
                procedure.getChildCount() - 1);

             Pair<String, Boolean> solution =
                runNewton(tableIterations);

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

        public Pair<String, Boolean> runNewton(List<TableRow> tableIterations){

            String message="NOT SUITABLE RANGE";
            boolean displayProcedure = true;


            InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            results.setVisibility(View.VISIBLE);

            String temp;
            BigDecimal x0 = BigDecimal.valueOf(Double.parseDouble(xa_et.getText().toString()));
            BigDecimal tol = BigDecimal.valueOf(Double.parseDouble(tol_et.getText().toString()));
            BigDecimal x1;
            int niter = Integer.parseInt(niter_et.getText().toString());
            //We have to trust in whoever set up the expression to not screw things up
            //TODO: We have to add checks to this function, otherwise this might crash the app.
            gexpr = new Expression(gx_et.getText().toString());
            if (niter < 1) {
                message = INVALID_ITER.getMessage();
                displayProcedure = INVALID_ITER.isDisplayProcedure();
            }
            //Method Begins
            try {
                BigDecimal y = expr.with("x", x0).eval();
                BigDecimal dy = gexpr.with("x", x0).eval();
                int count = 0;
                BigDecimal error = tol.add(BigDecimal.ONE);

                if(y.compareTo(BigDecimal.ZERO)==0){
                    message=X_ROOT.getMessage();
                    displayProcedure = X_ROOT.isDisplayProcedure();
                }

                tableIterations.add(createProcedureIteration(count+1, x0, y, dy, error));
                while (error.compareTo(tol) > 0 && y.compareTo(BigDecimal.ZERO) != 0 && dy.compareTo(BigDecimal.ZERO) != 0 && count < niter) {
                    //x1 = x0 - (y/dy)
                    x1 = x0.subtract(y.divide(dy, BigDecimal.ROUND_HALF_EVEN));
                    y = expr.with("x", x1).eval();
                    dy = gexpr.with("x", x1).eval();
                    error = x1.subtract(x0).abs();
                    x0 = x1;
                    count++;

                    tableIterations.add(createProcedureIteration(count+1, x0, y, dy, error));
                }

                if (y.compareTo(BigDecimal.ZERO) == 0) {
                    tableIterations.add(createProcedureIteration(count+1, x0, y, dy, error));
                    message = "x = " + x0.toString() + " is a root";
                    displayProcedure = true;
                } else if (error.compareTo(tol) < 0) {
                    message = "x = " + x0.toString() + " is an approximated root\nwith E = " + error.toString();
                    tableIterations.add(createProcedureIteration(count+1, x0, y, dy, error));
                    displayProcedure = true;
                } else if (dy.compareTo(BigDecimal.ZERO) == 0) {
                    message = "at x = " + x0.toString() + " there are possibly multiple roots";
                    displayProcedure = false;
                } else {
                    message = "The method failed after "
                            + count + " iterations";
                    displayProcedure = false;
                }

            }catch (Expression.ExpressionException
                    | ArithmeticException | NumberFormatException e) {
                return null;
            }
            return new Pair(message, displayProcedure);
        }


    private TableRow createProcedureIteration(int count, BigDecimal x,
                                              BigDecimal y, BigDecimal dy , BigDecimal Error) {
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


        tol = new TextView(this);
        tol.setGravity(Gravity.CENTER);
        tol.setText(Error.toString());

        iterationResult.addView(iterations);
        iterationResult.addView(xa);
        iterationResult.addView(ya);
        iterationResult.addView(dya);
        iterationResult.addView(tol);

        return iterationResult;
    }

    private void createTableProcedure(List<TableRow> tableIterations) {

        for (TableRow tableRow : tableIterations) {
            procedure.addView(tableRow);
        }
    }
    }
