package co.edu.eafit.andromath.linearsystems.gaussianelimination;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Objects;

import co.edu.eafit.andromath.R;

public class TotalPivotingGaussEliminationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_pivoting_gauss_elimination);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}