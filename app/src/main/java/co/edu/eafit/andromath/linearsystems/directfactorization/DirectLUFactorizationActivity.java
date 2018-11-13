package co.edu.eafit.andromath.linearsystems.directfactorization;

import android.content.Intent;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.math.BigDecimal;
import java.util.Objects;

import co.edu.eafit.andromath.R;
import co.edu.eafit.andromath.linearsystems.gaussianelimination.util.MatrixUtils;

import static co.edu.eafit.andromath.util.Constants.MATRIX;
import static co.edu.eafit.andromath.util.Constants.SELECTION;

public class DirectLUFactorizationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_lufactorization);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Intent intent = getIntent();

        BigDecimal[][] augmentedMatrix = (BigDecimal[][]) intent.
                getExtras().getSerializable(MATRIX);

        int methodSelection = (int) intent.getExtras().
                getSerializable(SELECTION);

        directLUFactorization(augmentedMatrix, methodSelection);
    }

    private void directLUFactorization(BigDecimal[][] augmentedMatrix,
                                       int methodSelection) {

        MatrixUtils.LUMatrix luMatrix;

        switch(methodSelection) {
            default:
            case 0:
                luMatrix = MatrixUtils.
                        LUDoolitle(augmentedMatrix);
                break;
            case 1:
                luMatrix = MatrixUtils.
                        LUCholesky(augmentedMatrix);
                break;
            case 2:
                luMatrix = MatrixUtils.
                        LUCroult(augmentedMatrix);
                break;
        }

        BigDecimal z[] = MatrixUtils.progressiveSubstitution(MatrixUtils.
                getAugmentedMatrix(luMatrix.L, MatrixUtils.getBVector(augmentedMatrix)));

        BigDecimal x[] = MatrixUtils.regressiveSubstitution(
                MatrixUtils.getAugmentedMatrix(luMatrix.U, z));
    }
}