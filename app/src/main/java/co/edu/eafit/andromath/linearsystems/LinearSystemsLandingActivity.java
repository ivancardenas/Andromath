package co.edu.eafit.andromath.linearsystems;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Objects;

import co.edu.eafit.andromath.R;

public class LinearSystemsLandingActivity extends AppCompatActivity {

    TableLayout matrix;
    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_systems_landing);
        Objects.requireNonNull(getSupportActionBar()).hide();

        matrix = (TableLayout) findViewById(R.id.tableLayoutEquationSystem);

        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);

        fillInitialMatrix(2, 3);
    }

    public void fillInitialMatrix(int initialRows, int initialColumns) {

        for (int i = 0; i < initialRows; i++) {

            TableRow rowMatrix = new TableRow(
                    this.getApplicationContext());

            for (int j = 0; j < initialColumns; j++)
                rowMatrix.addView(getCell(i, j),
                        getPxFromDp(60), getPxFromDp(60));

            matrix.addView(rowMatrix);
        }
    }

    public void addColumn(View v) {

        TableRow rowMatrix;

        for (int i = 0; i < matrix.getChildCount(); i++) {
            rowMatrix = (TableRow) matrix.getChildAt(i);

            rowMatrix.addView(getCell(i, ((TableRow) matrix.getChildAt(i))
                    .getChildCount()), getPxFromDp(60), getPxFromDp(60));
        }
    }

    public void addRow(View v) {

        TableRow rowMatrix = new TableRow(
                this.getApplicationContext());

        int rowsQuantity = matrix.getChildCount();

        int columnsQuantity = rowsQuantity == 0 ? 3 : ((TableRow)
                matrix.getChildAt(rowsQuantity - 1)).getChildCount();

        for (int i = 0; i < columnsQuantity; i++)
            rowMatrix.addView(getCell(rowsQuantity, i),
                    getPxFromDp(60), getPxFromDp(60));

        matrix.addView(rowMatrix);
    }

    private EditText getCell(int row, int col) {

        EditText cell = new EditText(
                this.getApplicationContext());

        cell.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED);

        cell.setHint(row + "," + col);

        cell.setPadding(0, getPxFromDp(10), 0, getPxFromDp(20));
        cell.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        cell.setAllCaps(true);

        return cell;
    }

    private int getPxFromDp(int dp) {
        return (int) (dp * Resources.getSystem()
                .getDisplayMetrics().density);
    }

    public void calculate(View v) {

    }
}
