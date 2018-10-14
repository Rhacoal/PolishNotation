package ml.rhacoal.polishnotation.lamba;

/**
 * Used by {@link ml.rhacoal.polishnotation.Calculator}
 */
@FunctionalInterface
public interface CharToBoolean {

    boolean eval(char c);

}
