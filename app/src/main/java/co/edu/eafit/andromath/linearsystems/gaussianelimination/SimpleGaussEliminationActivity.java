package co.edu.eafit.andromath.linearsystems.gaussianelimination;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

import co.edu.eafit.andromath.R;
import co.edu.eafit.andromath.linearsystems.gaussianelimination.util.MatrixUtils;

import static co.edu.eafit.andromath.util.Constants.MATRIX;

public class SimpleGaussEliminationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_gauss_elimination);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Intent intent = getIntent();

        BigDecimal[][] matrixValues = (BigDecimal[][]) intent.
                getExtras().getSerializable(MATRIX);

        gaussElimination(matrixValues);
    }

    private BigDecimal[] gaussElimination(BigDecimal[][] matrixValues) {

        int n = matrixValues.length; // Get A length.
        BigDecimal multiplier;

        for (int k = 0; k < n - 1; k++) {
            for (int i = k + 1; i < n; i++) {
                multiplier = (matrixValues[i][k]).
                        divide(matrixValues[k][k]);
                for (int j = k; j < n + 1; j++) {
                    matrixValues[i][j] = (matrixValues[i][j]).subtract(
                            multiplier.multiply(matrixValues[k][j]));
                }
            }
        }

        return MatrixUtils.regressiveSubstitution(matrixValues);
    }
}