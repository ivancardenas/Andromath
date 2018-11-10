package co.edu.eafit.andromath.linearsystems;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.math.BigDecimal;
import java.util.Objects;

import co.edu.eafit.andromath.R;
import co.edu.eafit.andromath.linearsystems.gaussianelimination.PartialPivotingGaussEliminationActivity;
import co.edu.eafit.andromath.linearsystems.gaussianelimination.SimpleGaussEliminationActivity;
import co.edu.eafit.andromath.linearsystems.gaussianelimination.TotalPivotingGaussEliminationActivity;

import static co.edu.eafit.andromath.util.Constants.MATRIX;

public class LinearSystemElectionActivity extends AppCompatActivity {

    Bundle bundleMatrix;

    AlertDialog alertGaussianElimination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_system_election);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Intent intent = getIntent();

        BigDecimal[][] matrixValues = (BigDecimal[][]) intent.
                getExtras().getSerializable(MATRIX);

        bundleMatrix = new Bundle();
        bundleMatrix.putSerializable(MATRIX, matrixValues);
    }

    public void gaussianElimination(View v) {
        CharSequence[] values = {"SIMPLE ELIMINATION", "PARTIAL PIVOTING", "TOTAL PIVOTING"};
        AlertDialog.Builder builder = new AlertDialog.Builder(LinearSystemElectionActivity.this);
        builder.setTitle("GAUSSIAN ELIMINATION");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                Intent intent;

                switch (item) {
                    case 0:
                        intent = new Intent(LinearSystemElectionActivity.this,
                                SimpleGaussEliminationActivity.class);
                        intent.putExtras(bundleMatrix);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(LinearSystemElectionActivity.this,
                                PartialPivotingGaussEliminationActivity.class);
                        intent.putExtras(bundleMatrix);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(LinearSystemElectionActivity.this,
                                TotalPivotingGaussEliminationActivity.class);
                        intent.putExtras(bundleMatrix);
                        startActivity(intent);
                        break;
                }

                alertGaussianElimination.dismiss();
            }
        });

        alertGaussianElimination = builder.create();
        alertGaussianElimination.show();
    }
}