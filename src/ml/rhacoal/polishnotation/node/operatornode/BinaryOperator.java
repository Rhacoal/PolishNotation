package ml.rhacoal.polishnotation.node.operatornode;

import ml.rhacoal.polishnotation.node.NodeType;

/**
 * Base class of operators with two children.
 * It is suggested to use {@link OperatorInfo} instead of inheriting this class to create an operator.
 * @see OperatorInfo#createOperatorInfo(String, OperatorInfo.OperatorCalculation, NodeType, int)
 */
public abstract class BinaryOperator extends OperatorNode {

    @Override
    public NodeType type() {
        return NodeType.BINARY;
    }

    @Override
    public StringBuilder toExpression(StringBuilder stringBuilder) {
        getLeftChild().toExpression(stringBuilder);
        stringBuilder.append(name());
        getRightChild().toExpression(stringBuilder);
        return stringBuilder;
    }

    @Override
    public StringBuilder toPolishNotation(StringBuilder stringBuilder) {
        stringBuilder.append(name());
        getLeftChild().toPolishNotation(stringBuilder);
        getRightChild().toPolishNotation(stringBuilder);
        return stringBuilder;
    }

    @Override
    public StringBuilder toReversePolishNotation(StringBuilder stringBuilder) {
        getLeftChild().toReversePolishNotation(stringBuilder);
        getRightChild().toReversePolishNotation(stringBuilder);
        stringBuilder.append(name());
        return stringBuilder;
    }

}
