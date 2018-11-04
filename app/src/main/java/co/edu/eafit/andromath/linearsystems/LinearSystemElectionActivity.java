package co.edu.eafit.andromath.linearsystems;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.math.BigDecimal;
import java.util.Objects;

import co.edu.eafit.andromath.R;

import static co.edu.eafit.andromath.util.Constants.MATRIX;

public class LinearSystemElectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_system_election);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Intent intent = getIntent();

        BigDecimal[][] matrixValues = (BigDecimal[][]) intent.
                getExtras().getSerializable(MATRIX);
    }
}
