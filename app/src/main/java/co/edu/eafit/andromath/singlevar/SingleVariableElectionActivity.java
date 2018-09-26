package co.edu.eafit.andromath.singlevar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Objects;

import co.edu.eafit.andromath.R;
import co.edu.eafit.andromath.singlevar.methods.IncrementalSearchActivity;

public class SingleVariableElectionActivity extends AppCompatActivity {

    private Intent intent;

    private String function;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_variable_election);
        Objects.requireNonNull(getSupportActionBar()).hide();

        function = getIntent().getStringExtra("equation");
    }

    public void incrementalSearch(View v) {
        intent = new Intent(this, IncrementalSearchActivity.class);
        intent.putExtra("equation", function);
        startActivity(intent);
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