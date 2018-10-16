package ml.rhacoal.polishnotation.node.operatornode;

import ml.rhacoal.polishnotation.node.NodeBase;
import ml.rhacoal.polishnotation.node.NodeType;

/**
 * This class provides an easy way to create a operator during runtime.
 */
public class OperatorInfo {

    public final String name;
    public final OperatorCalculation calculation;
    public final NodeType type;
    public final int precedence;
    public final boolean special;
    public final Class<? extends OperatorNode> specialClass;

    private OperatorInfo(String name, OperatorCalculation calculation, NodeType type, int precedence) {
        this.name = name;
        this.calculation = calculation;
        this.type = type;
        this.precedence = precedence;
        this.special = false;
        this.specialClass = null;
    }

    private OperatorInfo(String name, NodeType type, Class<? extends OperatorNode> specialClass) {
        this.name = name;
        this.calculation = null;
        this.type = type;
        this.precedence = 0;
        this.special = true;
        this.specialClass = specialClass;
    }

    @FunctionalInterface
    public interface OperatorCalculation {
        boolean calculate(NodeBase leftChild, NodeBase rightChild);
    }

    /**
     * Creates an {@link OperatorInfo} instance.
     * @param name the name of the operator. Used when converting the expression to polish notation
     * @param calculation defines how the operator do the calculation
     * @param type the type of the operator. Should be NodeType.UNARY or NodeType.BINARY
     * @param precedence the precedence of the operator
     * @return an {@link OperatorInfo} instance
     */
    public static OperatorInfo createOperatorInfo(String name, OperatorCalculation calculation, NodeType type, int precedence) {
        return new OperatorInfo(name, calculation, type, precedence);
    }

    /**
     * Creates an {@link OperatorInfo} instance.
     * This specifies the class.
     * @param name the name of the operator. Used when converting the expression to polish notation
     * @param type the type of the operator. Should be NodeType.UNARY or NodeType.BINARY
     * @param specialClass the class to
     * @return an {@link OperatorInfo} instance
     */
    public static OperatorInfo createOperatorInfo(String name, NodeType type, Class<? extends OperatorNode> specialClass) {
        return new OperatorInfo(name, type, specialClass);
    }
}
