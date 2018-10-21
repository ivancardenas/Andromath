package co.edu.eafit.andromath.LinealSystems.methods;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import co.edu.eafit.andromath.R;
import co.edu.eafit.andromath.LinealSystems.ResultsActivity;
import co.edu.eafit.andromath.LinealSystems.util.Utils;

public class DirectLUActivity extends AppCompatActivity {

    double a[][], b[];
    int choice;

    Button bdoolitle, bcroutl, bcholesky, brun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_lu);
        Intent intent = getIntent();
        a = (double[][]) intent.getExtras().getSerializable("a");
        b = (double[]) intent.getExtras().getSerializable("b");
        choice = (int) intent.getExtras().getSerializable("selection");

        runDirectLU();
    }


    public void runDirectLU() {

        Utils.LU mlu;

        switch(choice) {

            default:

            case 0:
                mlu = Utils.LUDoolitle(a);
                break;
            case 1:
                mlu = Utils.LUCholesky(a);
                break;
            case 2:
                mlu = Utils.LUCroult(a);
                break;
        }

        double z[] = Utils.progressiveSubstitution(Utils.augmentMatrix(mlu.L,b));
        double x[] = Utils.regressiveSubstitution(Utils.augmentMatrix(mlu.U,z));
        Log.d("XOUTPUT",x.toString());
        Intent i = new Intent(this, ResultsActivity.class);
        i.putExtra("x",x);
        startActivity(i);
    }
}