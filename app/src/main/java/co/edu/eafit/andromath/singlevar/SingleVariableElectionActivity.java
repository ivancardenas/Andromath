package co.edu.eafit.andromath.singlevar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Objects;

import co.edu.eafit.andromath.R;

public class SingleVariableElectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_variable_election);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    public void incrementalSearch(View v) {

    }

    public void bisection(View v) {

    }

    public void falseRule(View v) {

    }

    public void fixedPoint(View v) {

    }

    public void newton(View v) {

    }

    public void secant(View v) {

    }

    public void multipleRoots(View v) {

    }
}