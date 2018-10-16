package ml.rhacoal.polishnotation.node;

import ml.rhacoal.polishnotation.node.operatornode.OperatorNode;

/**
 * Base interface of nodes.
 */
public interface NodeBase {


    /**
     * Returns the type of the node.
     * @see NodeType
     * @return the type of the node
     */
    NodeType type();

    /**
     * Returns the precedence of the node.
     * <p>The precedence determines the order of calculation. Normally, it should follow these rules:</p>
     * <p>Binary Operators &lt; Unary Operators &lt; Parenthesis &lt; Values</p>
     * @return the precedence of the node
     */
    int precedence();

    /**
     * Returns the string representation of the node.
     * This function is designed to be called recursively.
     * @see NodeBase#toExpression() it is suggested to use toExpression() instead of this method for performance concerns
     * @return the string representation of the node.
     */
    @Deprecated
    String toString();

    /**
     * Returns the string representation of the node.
     * This function is designed to be called recursively.
     * This should works the same as toString(), but with better performance.
     * @return the string representation of the node.
     */
    default String toExpression() {
        return toExpression(new StringBuilder()).toString();
    }

    /**
     * Appends to the {@link StringBuilder} the string representation of the node.
     * This function is designed to be called recursively.
     * This is designed to avoid frequent string connection for better performance.
     * @param stringBuilder the {@link StringBuilder} to write to
     * @return the original {@link StringBuilder}
     */
    StringBuilder toExpression(StringBuilder stringBuilder);

    /**
     * Returns the polish notation of the node.
     * This function is designed to be called recursively.
     * @return the polish notation of the node.
     */
    default String toPolishNotation() {
        return toPolishNotation(new StringBuilder()).toString();
    }

    /**
     * Appends to the {@link StringBuilder} the polish notation of the node.
     * This function is designed to be called recursively.
     * This is designed to avoid frequent string connection for better performance.
     * @param stringBuilder the {@link StringBuilder} to write to
     * @return the original {@link StringBuilder}
     */
    StringBuilder toPolishNotation(StringBuilder stringBuilder);

    /**
     * Returns the reverse polish notation of the node.
     * This function is designed to be called recursively.
     * @return the reverse polish notation of the node.
     */
    default String toReversePolishNotation() {
        return toReversePolishNotation(new StringBuilder()).toString();
    }

    /**
     * Appends to the {@link StringBuilder} the reverse polish notation of the node.
     * This function is designed to be called recursively.
     * This is designed to avoid frequent string connection for better performance.
     * @param stringBuilder the {@link StringBuilder} to write to
     * @return the original {@link StringBuilder}
     */
    StringBuilder toReversePolishNotation(StringBuilder stringBuilder);


    boolean calculate();



}