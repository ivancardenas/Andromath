package co.edu.eafit.andromath;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import co.edu.eafit.andromath.singlevar.SingleVariableLandingActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
    }

    public void singleVariableButton(View v) {
        Intent intent = new Intent(this, SingleVariableLandingActivity.class);
        startActivity(intent);
    }
}