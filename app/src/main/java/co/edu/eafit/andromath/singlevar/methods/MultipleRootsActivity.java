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

public class MultipleRootsActivity extends AppCompatActivity {

    EditText xa_et, gx_et, jx_et, tol_et, niter_et;
    TextView func, results;
    Expression expr, gexpr, jexpr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_roots);
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
    }

    public void runMultipleRoot(View v){

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
        jexpr = new Expression(jx_et.getText().toString());
        if (niter < 1) {
            results.setText("Wrong number of iterations");
            return;
        }
        //Method Begins
        BigDecimal y = expr.with("x",x0).eval();
        BigDecimal dy = gexpr.with("x",x0).eval();
        BigDecimal ddy = jexpr.with("x",x0).eval();
        //dy^2 - y*ddy
        BigDecimal den = dy.pow(2).subtract(y.multiply(ddy));
        int count = 0;
        BigDecimal error = tol.add(BigDecimal.ONE);
        while(error.compareTo(tol) > 0 && y.compareTo(BigDecimal.ZERO) != 0 && den.compareTo(BigDecimal.ZERO) != 0 && count < niter){
            //x1 = x0 - (y*dy)/den
            x1 = x0.subtract(y.multiply(dy).divide(den, BigDecimal.ROUND_HALF_EVEN));
            y = expr.with("x",x1).eval();
            dy = gexpr.with("x",x1).eval();
            ddy = jexpr.with("x",x1).eval();
            den = dy.pow(2).subtract(y.multiply(ddy));
            error = x1.subtract(x0).abs();
            x0 = x1;
            count++;
        }
        if(y.compareTo(BigDecimal.ZERO) == 0){
            temp = "x = " + x0.toString() + " is a root";
            results.setText(temp);
        } else if(error.compareTo(tol) < 0) {
            temp = "x = " + x0.toString() + " is an approximated root\nwith E = " + error.toString();
            results.setText(temp);
        } else if(den.compareTo(BigDecimal.ZERO) == 0) {
            //TODO: Buscar la explicacion de este caso
            temp = "at x = " + x0.toString() + " the function behaves incorrectly";
            results.setText(temp);
        } else {
            temp = "the method failed after " + niter + " iterations";
            results.setText(temp);
        }
    }
}