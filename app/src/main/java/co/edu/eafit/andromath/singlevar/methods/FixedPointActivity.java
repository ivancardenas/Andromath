package co.edu.eafit.andromath.singlevar.methods;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;

import co.edu.eafit.andromath.R;

public class FixedPointActivity extends AppCompatActivity {

    EditText xa_et, gx_et, tol_et, niter_et;
    TextView func,results;
    Expression expr, gexpr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_point);
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
    }

    public void runFixedPoint(View v){

        InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        results.setVisibility(View.VISIBLE);

        String temp;
        BigDecimal xa = BigDecimal.valueOf(Double.parseDouble(xa_et.getText().toString()));
        BigDecimal tol = BigDecimal.valueOf(Double.parseDouble(tol_et.getText().toString()));
        BigDecimal xn;
        int niter = Integer.parseInt(niter_et.getText().toString());
        //We have to trust in whoever set up the expression to not screw things up
        //TODO: We have to add checks to this function, otherwise this might crash the app.
        gexpr = new Expression(gx_et.getText().toString());
        if (niter < 1) {
            results.setText("Wrong number of iterations");
            return;
        }
        //Method Begins
        BigDecimal y = expr.with("x",xa).eval();
        int count = 0;
        BigDecimal error = tol.add(BigDecimal.ONE);
        while(error.compareTo(tol) > 0 && y.compareTo(BigDecimal.ZERO) != 0 && count < niter){
            xn = gexpr.with("x",xa).eval();
            y = expr.with("x",xn).eval();
            error = xn.subtract(xa).abs();
            xa = xn;
            count++;
        }
        if(y.compareTo(BigDecimal.ZERO) == 0){
            temp = "x = " + xa.toString() + " is a root";
            results.setText(temp);
        }else if(error.compareTo(tol) < 0) {
            temp = "x = " + xa.toString() + " is an approximated root\nwith E = " + error.toString();
            results.setText(temp);
        }else{
            temp = "the method failed after" + niter + " iterations";
            results.setText(temp);
        }
    }
}