package ml.rhacoal.polishnotation.node.operatornode;

/**
 * Default implementation of <tt>BinaryOperator</tt>.
 * Provides a constructor using {@link OperatorInfo}.
 */
public class DefaultBinaryImpl extends BinaryOperator {

    private final OperatorInfo opInfo;

    public DefaultBinaryImpl(OperatorInfo opInfo) {
        this.opInfo = opInfo;
    }

    @Override
    public String name() {
        return opInfo.name;
    }

    @Override
    public int precedence() {
        return opInfo.precedence;
    }

    @Override
    public boolean calculate() {
        return opInfo.calculation.calculate(getLeftChild(), getRightChild());
    }

}
