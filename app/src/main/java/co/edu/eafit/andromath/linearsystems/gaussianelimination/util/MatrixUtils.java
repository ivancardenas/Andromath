package co.edu.eafit.andromath.linearsystems.gaussianelimination.util;

import java.math.BigDecimal;

import static co.edu.eafit.andromath.util.Constants.DECIMALS_QUANTITY;
import static co.edu.eafit.andromath.util.Constants.ROUNDING_MODE;

public class MatrixUtils {

    public static BigDecimal[] regressiveSubstitution(BigDecimal[][] Ab) {

        int n = Ab.length;
        BigDecimal accumulator;

        BigDecimal solution[] = new BigDecimal[n];

        try {
            solution[n - 1] = (Ab[n - 1][n]).divide(Ab[n - 1][n - 1],
                    DECIMALS_QUANTITY, ROUNDING_MODE);

            for (int i = n - 2; i > -1; i--) {
                accumulator = BigDecimal.ZERO;

                for (int p = i + 1; p < n; p++)
                    accumulator = accumulator.add(
                            Ab[i][p].multiply(solution[p]));

                solution[i] = (Ab[i][n].subtract(accumulator)).divide(Ab[i][i],
                        DECIMALS_QUANTITY, ROUNDING_MODE);
            }
        } catch(ArithmeticException e) {
            // TODO: throw message.
            System.out.println("Division por cero");
        }

        return solution;
    }

    public static BigDecimal[] progressiveSubstitution(BigDecimal[][] Ab){

        int n = Ab.length;
        BigDecimal accumulator;

        BigDecimal solution[] = new BigDecimal[n];

        try {
            solution[0] = Ab[0][n].divide(Ab[0][0],
                    DECIMALS_QUANTITY, ROUNDING_MODE);

            for(int i = 1; i < n; i++){
                accumulator = BigDecimal.ZERO;

                for (int p = 0; p < i; p++)
                    accumulator = accumulator.add(
                            Ab[i][p].multiply(solution[p]));

                solution[i] = (Ab[i][n].subtract(accumulator)).
                        divide(Ab[i][i], DECIMALS_QUANTITY, ROUNDING_MODE);
            }
        } catch(ArithmeticException e) {
            // TODO: throw message.
            System.out.println("Zero division");
        }

        return solution;
    }
}