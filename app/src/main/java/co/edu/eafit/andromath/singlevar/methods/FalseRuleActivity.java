package co.edu.eafit.andromath.singlevar.methods;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Objects;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;

import co.edu.eafit.andromath.R;

public class FalseRuleActivity extends AppCompatActivity {

    EditText xmin_et, xmax_et, tol_et, niter_et;
    TextView func, results;
    Expression expr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_false_rule);
        xmin_et = (EditText)findViewById(R.id.falserule_xmin);
        xmax_et = (EditText)findViewById(R.id.falserule_xmax);
        tol_et  = (EditText)findViewById(R.id.falserule_tolerance);
        niter_et = (EditText)findViewById(R.id.falserule_niter);
        func = (TextView)findViewById(R.id.falserule_func);
        results = (TextView)findViewById(R.id.falserule_result);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent i = getIntent();
        String s = "f(x) = " + i.getStringExtra("equation");
        func.setText(s);
        expr = new Expression(i.getStringExtra("equation"));
    }

    public void runFalseRule(View v) {

        InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        results.setVisibility(View.VISIBLE);

        String temp;
        BigDecimal xi = BigDecimal.valueOf(Double.parseDouble(xmin_et.getText().toString()));
        BigDecimal xs = BigDecimal.valueOf(Double.parseDouble(xmax_et.getText().toString()));
        BigDecimal tol = BigDecimal.valueOf(Double.parseDouble(tol_et.getText().toString()));
        BigDecimal xaux;
        int niter = Integer.parseInt(niter_et.getText().toString());
        if (niter < 1) {
            results.setText("Wrong number of iterations");
            return;
        }
        //Method Begins
        //yi = f(xi)
        //ys = f(xs)
        BigDecimal yi = expr.with("x", xi).eval();
        BigDecimal ys = expr.with("x", xs).eval();
        //yi = 0
        if (yi.compareTo(BigDecimal.ZERO) == 0) {
            temp = "x = " + xi.toString() + " is a root";
            results.setText(temp);
            //ys = 0
        } else if (ys.compareTo(BigDecimal.ZERO) == 0) {
            temp = "x = " + xs.toString() + " is a root";
            results.setText(temp);
            // ys*yi < 0
        } else if (yi.multiply(ys).compareTo(BigDecimal.ZERO) < 0) {
            //xm = xi - ((yi*(xs-xi))/(ys-yi))
            BigDecimal xm = xi.subtract(yi.multiply(xs.subtract(xi)).divide(ys.subtract(yi),BigDecimal.ROUND_HALF_EVEN));
            //ym = f(xm)
            BigDecimal ym = expr.with("x", xm).eval();
            int count = 1;
            //error = tol + 1
            BigDecimal error = tol.add(BigDecimal.ONE);
            //error > tol && ym != 0 && count < niter
            while (error.compareTo(tol) > 0 && ym.compareTo(BigDecimal.ZERO) != 0 && count < niter) {
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
                xm = xi.subtract(yi.multiply(xs.subtract(xi)).divide(ys.subtract(yi),BigDecimal.ROUND_HALF_EVEN));
                //ym = f(xm)
                ym = expr.with("x", xm).eval();
                //error = abs(xm-xaux)
                error = xm.subtract(xaux).abs();
                count++;
            }
            if (ym.compareTo(BigDecimal.ZERO) == 0) {
                temp = "x = " + xm.toString() + " is a root";
                results.setText(temp);
            } else if (error.compareTo(tol) < 0) {
                temp = "x = " + xm.toString() + " is an approximated root\nwith E = " + error.toString();
                results.setText(temp);
            } else {
                temp = "the method failed after " + niter + " iterations";
                results.setText(temp);
            }
        } else {
            temp = "NOT SUITABLE RANGE";
            results.setText(temp);
        }
    }
}