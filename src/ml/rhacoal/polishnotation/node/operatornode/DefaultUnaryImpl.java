package ml.rhacoal.polishnotation.node.operatornode;

public class DefaultUnaryImpl extends UnaryOperator {

    private final OperatorInfo opInfo;

    public DefaultUnaryImpl(OperatorInfo opInfo) {
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
