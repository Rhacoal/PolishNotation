package ml.rhacoal.polishnotation.node.operatornode;

import ml.rhacoal.polishnotation.node.NodeType;

/**
 * Base class of operators with only one child.
 * This is only suitable for prefix operators since there is no need for suffix operators under this situation.
 * Meanwhile, {@link ParenthesisNode} is an implementation of this node.
 * It is suggested to use {@link OperatorInfo} to create an opeartor.
 * @see OperatorInfo#createOperatorInfo(String, OperatorInfo.OperatorCalculation, NodeType, int)
 */
public abstract class UnaryOperator extends OperatorNode {

    @Override
    public NodeType type() {
        return NodeType.UNARY;
    }

    @Override
    public int precedence() {
        return 1000;
    }

    @Override
    public String toString() {
        return toExpression(new StringBuilder()).toString();
    }

    @Override
    public StringBuilder toExpression(StringBuilder stringBuilder) {
        stringBuilder.append(name());
        getRightChild().toExpression(stringBuilder);
        return stringBuilder;
    }

    @Override
    public StringBuilder toPolishNotation(StringBuilder stringBuilder) {
        stringBuilder.append(name());
        getRightChild().toPolishNotation(stringBuilder);
        return stringBuilder;
    }

    @Override
    public StringBuilder toReversePolishNotation(StringBuilder stringBuilder) {
        getRightChild().toReversePolishNotation(stringBuilder);
        stringBuilder.append(name());
        return stringBuilder;
    }
}
