package co.edu.eafit.andromath.LinealSystems.methods;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import co.edu.eafit.andromath.R;
import co.edu.eafit.andromath.LinealSystems.ResultsActivity;
import co.edu.eafit.andromath.LinealSystems.util.Utils;

public class JacobiActivity extends AppCompatActivity {

    double a[][], b[], x[], lambda = 0, tol;
    int choice, i, iter;

    Button bNormal, bRelax, bRun, bBack, bInsert, bOK, bTol, bIter;
    EditText lambda_et, xii_et, tol_et, iter_et;
    TextView xii_tv, lambda_tv, xiihelp_tv, tol_tv, iter_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jacobi);
        Intent intent = getIntent();
        a = (double[][]) intent.getExtras().getSerializable("a");
        b = (double[]) intent.getExtras().getSerializable("b");
        x = new double[a.length];
        i = 0;
        bNormal = (Button)findViewById(R.id.jacobi_bnormal);
        bRelax = (Button)findViewById(R.id.jacobi_brelax);
        bRun = (Button)findViewById(R.id.jacobi_brun);
        bBack = (Button)findViewById(R.id.jacobi_back_btn);
        bInsert = (Button)findViewById(R.id.jacobi_insert_btn);
        bOK = (Button)findViewById(R.id.jacobi_ok_btn);
        bTol = (Button)findViewById(R.id.jacobi_tol_btn);
        bIter = (Button)findViewById(R.id.jacobi_iter_btn);
        lambda_et = (EditText)findViewById(R.id.jacobi_lambdaet);
        xii_et = (EditText) findViewById(R.id.jacobi_xii_et);
        tol_et = (EditText)findViewById(R.id.jacobi_tol_et);
        iter_et = (EditText)findViewById(R.id.jacobi_iter_et);
        lambda_tv = (TextView)findViewById(R.id.jacobi_lambda_tv);
        xii_tv = (TextView)findViewById(R.id.jacobi_xii_tv);
        xiihelp_tv = (TextView)findViewById(R.id.jacobi_xiihelp_tv);
        tol_tv = (TextView)findViewById(R.id.jacobi_tol_tv);
        iter_tv = (TextView)findViewById(R.id.jacobi_iter_tv);
    }

    public void Normal(View v){
        choice = 1;
        bNormal.setEnabled(false);
        bRelax.setEnabled(false);
        lambda_et.setEnabled(false);
        lambda_tv.setEnabled(false);
        bOK.setEnabled(false);
        xiihelp_tv.setVisibility(View.VISIBLE);
        xii_tv.setVisibility(View.VISIBLE);
        xii_et.setVisibility(View.VISIBLE);
        bInsert.setVisibility(View.VISIBLE);
        bBack.setVisibility(View.VISIBLE);
    }

    public void Relax(View v){
        choice = 2;
        bNormal.setEnabled(false);
        bRelax.setEnabled(false);
        lambda_et.setEnabled(true);
        bOK.setEnabled(true);
    }

    public void OK(View v){
        lambda = Double.parseDouble(lambda_et.getText().toString());
        bOK.setEnabled(false);
        lambda_et.setEnabled(false);
        lambda_tv.setEnabled(false);
        xiihelp_tv.setVisibility(View.VISIBLE);
        xii_tv.setVisibility(View.VISIBLE);
        xii_et.setVisibility(View.VISIBLE);
        bInsert.setVisibility(View.VISIBLE);
        bBack.setVisibility(View.VISIBLE);
    }

    public void Insert(View v){
        int n = x.length;
        x[i] = Double.parseDouble(xii_et.getText().toString());
        String s = "x"+(i+1)+"=";
        xii_tv.setText(s);
        xii_et.setText("");
        i++;
        if(i == n){
            bInsert.setEnabled(false);
            bBack.setEnabled(false);
            xii_tv.setEnabled(false);
            tol_tv.setVisibility(View.VISIBLE);
            tol_et.setVisibility(View.VISIBLE);
            bTol.setVisibility(View.VISIBLE);
        }
    }

    public void Back(View v){
        finish();
    }

    public void Tol(View v){
        tol = Double.parseDouble(tol_et.getText().toString());
        tol_et.setEnabled(false);
        tol_tv.setEnabled(false);
        bTol.setEnabled(false);
        iter_et.setVisibility(View.VISIBLE);
        iter_tv.setVisibility(View.VISIBLE);
        bIter.setVisibility(View.VISIBLE);
    }

    public void Iter(View v){
        iter = Integer.parseInt(iter_et.getText().toString());
        iter_et.setEnabled(false);
        iter_tv.setEnabled(false);
        bIter.setEnabled(false);
        bRun.setEnabled(true);
        bRun.setVisibility(View.VISIBLE);
    }

    public void Run(View v){
        int n = x.length;
        double[] res = new double[n];
        switch (choice) {
            default:
            case 1:{
                res = Utils.doJacobi(a,b,x,tol,iter);
                break;
            }
            case 2:{
                res = Utils.doJacobiRelaxed(a,b,x,tol,lambda,iter);
                break;
            }
        }
        Log.d("x",res.toString());
        Intent i = new Intent(this, ResultsActivity.class);
        i.putExtra("x",x);
        startActivity(i);
    }
}
