package co.edu.eafit.andromath.LinealSystems;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import co.edu.eafit.andromath.R;

public class LinearSystemLandingActivity extends AppCompatActivity {

    Button size_btn, back_btn, insert_btn;
    EditText size_et, aij_et;
    TextView aijhelp_tv, aij_tv, textView2;
    Space spc, spctxtview;

    double a[][], b[];
    int i, j, n, bi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_system_landing);
        size_btn = (Button)findViewById(R.id.matrix_landing_sizebtn);
        back_btn = (Button)findViewById(R.id.matrix_landing_back);
        insert_btn = (Button)findViewById(R.id.matrix_landing_insertbtn);
        size_et = (EditText)findViewById(R.id.matrix_landing_sizeet);
        aij_et = (EditText)findViewById(R.id.matrix_landing_realinputet);
        aijhelp_tv = (TextView)findViewById(R.id.matrix_landing_aijhelptv);
        aij_tv = (TextView)findViewById(R.id.matrix_landing_aijtv);
        spc = (Space) findViewById(R.id.spcmlbtn);
        spctxtview = (Space) findViewById(R.id.spctxtview);
        textView2 = (TextView) findViewById(R.id.textView2);
    }

    public void mSize(View v) {

        n = Integer.parseInt(size_et.getText().toString());
        if(n > 0){

            size_btn.setVisibility(View.GONE);
            spc.setVisibility(View.GONE);
            size_et.setVisibility(View.GONE);
            spctxtview.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);

            //a[i][j] i=Rows j=Columns
            //It's auto filled with 0.
            a = new double[n][n];
            b = new double[n];
            size_et.setEnabled(false);
            size_btn.setEnabled(false);
            aijhelp_tv.setVisibility(View.VISIBLE);
            aij_tv.setVisibility(View.VISIBLE);
            aij_et.setVisibility(View.VISIBLE);
            back_btn.setVisibility(View.VISIBLE);
            insert_btn.setVisibility(View.VISIBLE);
            aij_tv.setText("a[1][1] =");
            i = 0;
            j = 0;
        } else {
            Toast.makeText(getApplicationContext(), "Incorrect size", Toast.LENGTH_SHORT).show();
        }

    }

    public void mBack(View v){
        finish();
    }

    public void mInsert(View v){
        double x = Double.parseDouble(aij_et.getText().toString());
        String s;
        if(i != n) {
            //a_i+1,j+1 = x
            a[i][j] = x;
            if (j == n - 1) {
                i++;
                j = 0;
            } else {
                j++;
            }
            s = "a["+(i+1)+"]["+(j+1)+"] =";
            aij_tv.setText(s);
            aij_et.setText("");
            if(i == n){
                back_btn.setEnabled(false);
                aijhelp_tv.setText("input the RHS vector of Ax = B");
                aij_tv.setText("b[1] =");
                bi = 0;
            }
        } else {
            //Once it gets here it now entering b[].
            b[bi] = x;
            bi++;
            s = "b["+(bi+1)+"] =";
            aij_tv.setText(s);
            aij_et.setText("");
            if(bi == n){
                Intent i = new Intent(this,LinearSystemChooseMethodActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("a",a);
                bundle.putSerializable("b",b);
                i.putExtras(bundle);
                startActivity(i);
            }
        }
    }
}
