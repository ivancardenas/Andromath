package co.edu.eafit.andromath;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Objects;

import co.edu.eafit.andromath.cloud.RemoteConnection;
import co.edu.eafit.andromath.singlevar.SingleVariableLandingActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    public void singleVariableButton(View v) {
        Intent intent = new Intent(this, SingleVariableLandingActivity.class);
        startActivity(intent);
    }

    public void establishConnection(View v) {
        RemoteConnection remote = new RemoteConnection();
    }
}