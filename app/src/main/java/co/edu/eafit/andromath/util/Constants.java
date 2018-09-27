package co.edu.eafit.andromath.util;

public class Constants {

    public static final String VARIABLE = "x";
    public static final String EQUATION = "equation";

    public enum ErrorCodes {
        X_ROOT("The entered X is a root", false),
        INVALID_DELTA("Delta is not valid", false),
        INVALID_ITER("Wrong number of iterations", false);

        private String message;
        private boolean displayProcedure;

        ErrorCodes(String message, boolean displayProcedure) {
            this.message = message;
            this.displayProcedure = displayProcedure;
        }

        public String getMessage() {
            return message;
        }

        public boolean isDisplayProcedure() {
            return displayProcedure;
        }
    }
}