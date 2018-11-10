package co.edu.eafit.andromath.linearsystems.gaussianelimination.util;

import java.math.BigDecimal;

public class MatrixUtils {

    public static BigDecimal[] regressiveSubstitution(BigDecimal[][] Ab) {

        int n = Ab.length;
        BigDecimal accumulator;

        BigDecimal solution[] =
                new BigDecimal[n];

        solution[n - 1] = (Ab[n - 1][n]).
                divide(Ab[n - 1][n - 1]);

        for (int i = n - 2; i > -1; i--) {
            accumulator = BigDecimal.ZERO;

            for (int p = i + 1; p < n; p++)
                accumulator = accumulator.add(
                        Ab[i][p].multiply(solution[p]));

            solution[i] = (Ab[i][n].subtract(
                    accumulator)).divide(Ab[i][i]);
        }

        return solution;
    }

    public static BigDecimal[] progressiveSubstitution(BigDecimal[][] Ab){

        int n = Ab.length;

        BigDecimal accumulator;

        BigDecimal solution[] = new BigDecimal[n];
        solution[0] = Ab[0][n].divide(Ab[0][0]);

        for(int i = 1; i < n; i++){
            accumulator = BigDecimal.ZERO;

            for (int p = 0; p < i; p++)
                accumulator = accumulator.add(
                        Ab[i][p].multiply(solution[p]));

            solution[i] = (Ab[i][n].subtract(
                    accumulator)).divide(Ab[i][i]);
        }

        return solution;
    }
}