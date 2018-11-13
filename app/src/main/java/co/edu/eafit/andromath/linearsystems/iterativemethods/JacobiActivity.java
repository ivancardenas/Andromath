package co.edu.eafit.andromath.linearsystems.iterativemethods;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import java.math.BigDecimal;
import java.util.Objects;

import co.edu.eafit.andromath.R;

import static co.edu.eafit.andromath.util.Constants.MATRIX;

public class JacobiActivity extends AppCompatActivity {

    LinearLayout linearLayoutSolutionStages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jacobi);
        Objects.requireNonNull(getSupportActionBar()).hide();

        linearLayoutSolutionStages = (LinearLayout) findViewById(
                R.id.linearLayoutSolutionStages);

        Intent intent = getIntent();

        BigDecimal[][] augmentedMatrix = (BigDecimal[][]) intent.
                getExtras().getSerializable(MATRIX);
    }
}
