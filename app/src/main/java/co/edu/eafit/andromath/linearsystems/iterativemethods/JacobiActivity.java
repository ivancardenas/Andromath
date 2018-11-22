package co.edu.eafit.andromath.linearsystems.iterativemethods;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.math.BigDecimal;
import java.util.Objects;

import co.edu.eafit.andromath.R;

import static co.edu.eafit.andromath.linearsystems.gaussianelimination.util.ViewUtils.getPxFromDp;
import static co.edu.eafit.andromath.util.Constants.MATRIX;

public class JacobiActivity extends AppCompatActivity {

    TableLayout matrix;
    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jacobi);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        matrix = (TableLayout) findViewById(R.id.tableLayoutEquationSystem);

        Intent intent = getIntent();

        BigDecimal[][] augmentedMatrix = (BigDecimal[][]) intent.
                getExtras().getSerializable(MATRIX);

        setInitialValueFields(augmentedMatrix.length);
    }

    private void setInitialValueFields(int variableQuantity) {

        TableRow rowMatrix = new TableRow(
                this.getApplicationContext());

        for (int i = 0; i < variableQuantity; i++)
            rowMatrix.addView(getCell(i),
                    getPxFromDp(60), getPxFromDp(60));

        matrix.addView(rowMatrix);
    }

    private EditText getCell(int indexVariable) {

        EditText cell = new EditText(
                this.getApplicationContext());

        cell.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED);

        cell.setHint("X" + indexVariable);

        cell.setPadding(0, getPxFromDp(10), 0, getPxFromDp(20));
        cell.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        cell.setAllCaps(true);

        return cell;
    }
}
