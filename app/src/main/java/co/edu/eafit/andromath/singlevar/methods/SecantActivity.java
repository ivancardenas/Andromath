package co.edu.eafit.andromath.singlevar.methods;

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

public class SecantActivity extends AppCompatActivity {

    EditText x0_et, x1_et, tol_et, niter_et;
    TextView func, results;
    Expression expr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secant);
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
    }

    public void runSecant(View v){

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
            results.setText("Wrong number of iterations");
            return;
        }
        //Method Begins
        BigDecimal y0 = expr.with("x",x0).eval();
        if(y0.compareTo(BigDecimal.ZERO) == 0){
            temp = "x = " + x0.toString() + " is a root";
            results.setText(temp);
        } else {
            BigDecimal y1 = expr.with("x",x1).eval();
            int count = 0;
            BigDecimal error = tol.add(BigDecimal.ONE);
            BigDecimal den = y1.subtract(y0);
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
            }
            if (y1.compareTo(BigDecimal.ZERO) == 0) {
                temp = "x = " + x1.toString() + " is a root";
                results.setText(temp);
            } else if (error.compareTo(tol) < 0) {
                temp = "x = " + x1.toString() + " is an approximated root\nwith E = " + error.toString();
                results.setText(temp);
            } else if(den.compareTo(BigDecimal.ZERO) == 0){
                temp = "There are possibly multiple roots";
                results.setText(temp);
            } else {
                temp = "the method failed after " + niter + " iterations";
                results.setText(temp);
            }
        }
    }
}