package co.edu.eafit.andromath.LinealSystems.methods;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import co.edu.eafit.andromath.R;
import co.edu.eafit.andromath.LinealSystems.ResultsActivity;
import co.edu.eafit.andromath.LinealSystems.util.Utils;

public class LUGaussActivity extends AppCompatActivity {

    double a[][], b[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugauss);
        Intent intent = getIntent();
        a = (double[][]) intent.getExtras().getSerializable("a");
        b = (double[]) intent.getExtras().getSerializable("b");

        runLUGauss();
    }

    public void runLUGauss() {
        Utils.LU mlu = Utils.LUGauss(a);
        double z[] = Utils.progressiveSubstitution(Utils.augmentMatrix(mlu.L,b));
        double x[] = Utils.regressiveSubstitution(Utils.augmentMatrix(mlu.U,z));
        Log.d("XOUTPUT",x.toString());
        Intent i = new Intent(this, ResultsActivity.class);
        i.putExtra("x",x);
        startActivity(i);
    }
}
