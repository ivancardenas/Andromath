package co.edu.eafit.andromath.LinealSystems;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import co.edu.eafit.andromath.LinealSystems.LinearSystemLandingActivity;
import co.edu.eafit.andromath.R;
import co.edu.eafit.andromath.LinealSystems.methods.DirectLUActivity;
import co.edu.eafit.andromath.LinealSystems.methods.GaussActivity;
import co.edu.eafit.andromath.LinealSystems.methods.GaussSeidelActivity;
import co.edu.eafit.andromath.LinealSystems.methods.JacobiActivity;
import co.edu.eafit.andromath.LinealSystems.methods.LUGaussActivity;
import co.edu.eafit.andromath.LinealSystems.methods.LUPivotGaussActivity;
import co.edu.eafit.andromath.LinealSystems.methods.PartialPivotGaussActivity;
import co.edu.eafit.andromath.LinealSystems.methods.TotalPivotGaussActivity;

public class LinearSystemChooseMethodActivity extends AppCompatActivity {

    double a[][], b[];
    Bundle bun;

    AlertDialog alertDialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_system_choose_method);
        Intent intent = getIntent();
        a = (double[][]) intent.getExtras().getSerializable("a");
        b = (double[]) intent.getExtras().getSerializable("b");
        bun = new Bundle();
        bun.putSerializable("a", a);
        bun.putSerializable("b", b);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(LinearSystemChooseMethodActivity.this, LinearSystemLandingActivity.class);
        startActivity(i);
    }

    public void gaussianElimination(View v) {

        CharSequence[] values = {"SIMPLE ELIMINATION","PARTIAL PIVOTING","TOTAL PIVOTING"};
        AlertDialog.Builder builder = new AlertDialog.Builder(LinearSystemChooseMethodActivity.this);
        builder.setTitle("GAUSSIAN ELIMINATION");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                Intent i;

                switch(item) {
                    case 0:
                        i = new Intent(LinearSystemChooseMethodActivity.this, GaussActivity.class);
                        i.putExtras(bun);
                        startActivity(i);
                        break;
                    case 1:
                        i = new Intent(LinearSystemChooseMethodActivity.this, PartialPivotGaussActivity.class);
                        i.putExtras(bun);
                        startActivity(i);
                        break;
                    case 2:
                        i = new Intent(LinearSystemChooseMethodActivity.this, TotalPivotGaussActivity.class);
                        i.putExtras(bun);
                        startActivity(i);
                        break;
                }
                alertDialog1.dismiss();
            }
        });

        alertDialog1 = builder.create();
        alertDialog1.show();
    }

    public void luFactorization(View v) {

        CharSequence[] values = {"SIMPLE ELIMINATION","PARTIAL PIVOTING"};
        AlertDialog.Builder builder = new AlertDialog.Builder(LinearSystemChooseMethodActivity.this);
        builder.setTitle("LU FACTORIZATION");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                Intent i;

                switch(item) {
                    case 0:
                        i = new Intent(LinearSystemChooseMethodActivity.this, LUGaussActivity.class);
                        i.putExtras(bun);
                        startActivity(i);
                        break;
                    case 1:
                        i = new Intent(LinearSystemChooseMethodActivity.this, LUPivotGaussActivity.class);
                        i.putExtras(bun);
                        startActivity(i);
                        break;
                }
                alertDialog1.dismiss();
            }
        });

        alertDialog1 = builder.create();
        alertDialog1.show();
    }

    public void directFactorization(View v) {

        CharSequence[] values = {"DOOLITTLE","CHOLESKY","CROUT"};
        AlertDialog.Builder builder = new AlertDialog.Builder(LinearSystemChooseMethodActivity.this);
        builder.setTitle("DIRECT FACTORIZATION");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                Intent i;

                switch(item) {
                    case 0:
                        bun.putSerializable("selection", 0);
                        i = new Intent(LinearSystemChooseMethodActivity.this, DirectLUActivity.class);
                        i.putExtras(bun);
                        startActivity(i);
                        break;
                    case 1:
                        bun.putSerializable("selection", 1);
                        i = new Intent(LinearSystemChooseMethodActivity.this, DirectLUActivity.class);
                        i.putExtras(bun);
                        startActivity(i);
                        break;
                    case 2:
                        bun.putSerializable("selection", 2);
                        i = new Intent(LinearSystemChooseMethodActivity.this, DirectLUActivity.class);
                        i.putExtras(bun);
                        startActivity(i);
                        break;
                }
                alertDialog1.dismiss();
            }
        });

        alertDialog1 = builder.create();
        alertDialog1.show();
    }

    public void iterativeMethods(View v) {

        CharSequence[] values = {"JACOBI METHOD","GAUSS SEIDEL"};
        AlertDialog.Builder builder = new AlertDialog.Builder(LinearSystemChooseMethodActivity.this);
        builder.setTitle("ITERATIVE METHODS");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                Intent i;

                switch(item) {
                    case 0:
                        i = new Intent(LinearSystemChooseMethodActivity.this, JacobiActivity.class);
                        i.putExtras(bun);
                        startActivity(i);
                        break;
                    case 1:
                        i = new Intent(LinearSystemChooseMethodActivity.this, GaussSeidelActivity.class);
                        i.putExtras(bun);
                        startActivity(i);
                        break;
                }
                alertDialog1.dismiss();
            }
        });

        alertDialog1 = builder.create();
        alertDialog1.show();
    }
}