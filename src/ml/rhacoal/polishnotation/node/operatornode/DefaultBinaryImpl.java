package ml.rhacoal.polishnotation.node.operatornode;

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
