package co.edu.eafit.andromath.linearsystems.iterativemethods;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import static co.edu.eafit.andromath.util.Constants.NORMAL_METHOD;
import static co.edu.eafit.andromath.util.Constants.RELAXED_METHOD;

public class GaussSeidelActivity extends AppCompatActivity {

    TableLayout matrix;
    LinearLayout mainLayout;
    EditText editTextLambda;

    BigDecimal[][] augmentedMatrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gauss_seidel);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        matrix = (TableLayout) findViewById(R.id.tableLayoutEquationSystem);

        editTextLambda = (EditText) findViewById(R.id.editTextLambda);

        Intent intent = getIntent();

        augmentedMatrix = (BigDecimal[][]) intent.
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

    public void execute(View v) {

        int n = augmentedMatrix.length;
        BigDecimal[] result = new BigDecimal[n];

        String lambdaValue = editTextLambda.getText().toString();

        int choice = lambdaValue.isEmpty()
                ? NORMAL_METHOD : RELAXED_METHOD;

        switch (choice) {
            default:
            case NORMAL_METHOD: {
                result = jacobi(augmentedMatrix, getInitialValues(),
                        tolerance, lambda, iterations, false);
                break;
            }
            case RELAXED_METHOD: {
                result = jacobi(augmentedMatrix, getInitialValues(),
                        tolerance, lambda, iterations, true);
                break;
            }
        }
    }

    private BigDecimal[] getInitialValues() {
        return null;
    }

    private BigDecimal[] jacobi(BigDecimal[][] augmentedMatrix, BigDecimal[] initialValues,
                                BigDecimal tolerance, int iterations, boolean relaxed) {


        return null;
    }
}
