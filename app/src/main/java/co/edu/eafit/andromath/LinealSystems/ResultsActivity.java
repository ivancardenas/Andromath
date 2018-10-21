package co.edu.eafit.andromath.LinealSystems;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import co.edu.eafit.andromath.MainActivity;
import co.edu.eafit.andromath.R;

public class ResultsActivity extends AppCompatActivity {

    double x[];
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Intent i = getIntent();
        x = i.getDoubleArrayExtra("x");
        tv = (TextView)findViewById(R.id.result_tv);
    }

    @Override
    protected void onStart(){
        super.onStart();
        String r = "x =\n[\n";
        for (int i = 0; i < x.length-1; i++){
            if(Double.isNaN(x[i])){
                tv.setText("There has been an error during the execution of the method. \nThis may be due to the parameters for the method or the system may not have a unique solution.");
                return;
            }
            r += x[i] + ",\n";
        }
        r += x[x.length-1] + "\n]";
        tv.setText(r);
    }

    public void ok(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}