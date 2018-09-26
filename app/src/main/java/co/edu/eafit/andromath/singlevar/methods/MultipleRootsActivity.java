package co.edu.eafit.andromath.singlevar.methods;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Objects;

import co.edu.eafit.andromath.R;

public class MultipleRootsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_roots);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}