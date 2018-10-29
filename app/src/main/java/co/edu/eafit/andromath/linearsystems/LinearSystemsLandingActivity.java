package co.edu.eafit.andromath.linearsystems;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Objects;

import co.edu.eafit.andromath.R;

public class LinearSystemsLandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_systems_landing);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}
