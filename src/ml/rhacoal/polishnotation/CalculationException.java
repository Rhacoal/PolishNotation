package ml.rhacoal.polishnotation;

/**
 * The class {@link CalculationException} aims at providing users with clear error messages.
 */
public class CalculationException extends Exception {

    /**
     * Construcs a {@link CalculationException} instance
     * @param msg the exception message
     */
    public CalculationException(String msg) {
        super(msg);
    }

}
