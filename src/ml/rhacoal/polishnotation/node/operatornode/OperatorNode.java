package ml.rhacoal.polishnotation.node.operatornode;

import ml.rhacoal.polishnotation.node.NodeBase;
import ml.rhacoal.polishnotation.node.NodeType;

/**
 * Operator.
 */
public abstract class OperatorNode implements NodeBase {

    public NodeBase getLeftChild() {
        return leftChild;
    }

    public NodeBase getRightChild() {
        return rightChild;
    }

    public OperatorNode getFather() {
        return father;
    }

    public NodeBase updateLeftChild(NodeBase leftChild) {
        NodeBase originalLeft = this.leftChild;
        this.leftChild = leftChild;
        return originalLeft;
    }

    public NodeBase updateRightChild(NodeBase rightChild) {
        NodeBase originalRight = this.rightChild;
        this.rightChild = rightChild;
        return originalRight;
    }

    public OperatorNode updateFather(OperatorNode father) {
        OperatorNode originalFather = this.father;
        //originalFather.updateRightChild(this);
        this.father = father;
        return originalFather;
    }

    public abstract String name();
    
    @Override
    public String toString() {
        return toExpression();
    }

    private NodeBase leftChild, rightChild;
    private OperatorNode father;
    //private String name;

    public static final Class<? extends OperatorNode> createOperator(final OperatorInfo opInfo) {
        if (opInfo.type == NodeType.BINARY) {
            return new DefaultBinaryImpl(opInfo){

            }.getClass();
        }
        if (opInfo.type == NodeType.UNARY) {
            return new DefaultBinaryImpl(opInfo) {

            }.getClass();
        }
        throw new UnsupportedOperationException("It is not allowed to create an operator with the type of "
                + String.valueOf(opInfo.type));
    }


}