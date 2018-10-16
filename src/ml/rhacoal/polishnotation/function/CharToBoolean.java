package ml.rhacoal.polishnotation.function;

/**
 * Used by {@link ml.rhacoal.polishnotation.Calculator}
 */
@FunctionalInterface
public interface CharToBoolean {

    boolean eval(char c);

}
