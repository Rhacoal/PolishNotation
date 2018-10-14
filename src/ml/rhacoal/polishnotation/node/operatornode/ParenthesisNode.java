package ml.rhacoal.polishnotation.node.operatornode;

public class ParenthesisNode extends UnaryOperator {

    boolean close = false;

    @Override
    public int precedence() {
        return close ? 10000 : 0;
    }

    @Override
    public boolean calculate() {
        return getRightChild().calculate();
    }

    public boolean isClose() {
        return close;
    }

    public void close() {
        close = true;
    }

    @Override
    public StringBuilder toExpression(StringBuilder stringBuilder) {
        stringBuilder.append('(');
        getRightChild().toExpression(stringBuilder);
        stringBuilder.append(')');
        return stringBuilder;
    }

    @Override
    public StringBuilder toPolishNotation(StringBuilder stringBuilder) {
        return getRightChild().toPolishNotation(stringBuilder);
    }

    @Override
    public StringBuilder toReversePolishNotation(StringBuilder stringBuilder) {
        return getRightChild().toReversePolishNotation(stringBuilder);
    }

    @Override
    public String name() {
        return "(";
    }
}
