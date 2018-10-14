package ml.rhacoal.polishnotation;

import ml.rhacoal.polishnotation.node.NodeType;
import ml.rhacoal.polishnotation.node.operatornode.*;

import java.util.HashMap;

import static ml.rhacoal.polishnotation.node.operatornode.OperatorInfo.createOperatorInfo;

/**
 * Provides the service of supporting customized operators.
 */
public class OperatorRegistry {

    private static final HashMap<String, OperatorInfo> prefixMap;
    private static final HashMap<String, OperatorInfo> binaryMap;

    static {
        prefixMap = new HashMap<>();
        binaryMap = new HashMap<>();
        registerOperator(createOperatorInfo("|", (l,r) -> l.calculate() || r.calculate(), NodeType.BINARY, 10));
        registerOperator(createOperatorInfo("&", (l,r) -> l.calculate() && r.calculate(), NodeType.BINARY, 15));
        registerOperator(createOperatorInfo("^", (l,r) -> !l.calculate() || r.calculate(), NodeType.BINARY, 7));
        registerOperator(createOperatorInfo("~", (l,r) -> l.calculate() == r.calculate(), NodeType.BINARY, 3));
        registerOperator(createOperatorInfo("↑", (l,r) -> !(l.calculate() && r.calculate()), NodeType.BINARY, 2));
        registerOperator(createOperatorInfo("↓", (l,r) -> !(l.calculate() || r.calculate()), NodeType.BINARY, 1));
        registerOperator(createOperatorInfo("!", (n,r) -> ! r.calculate(), NodeType.UNARY, 1000));
        registerOperator(createOperatorInfo("(", NodeType.UNARY, ParenthesisNode.class));
    }

    public static OperatorNode getOperator(String name, NodeType type) throws CalculationException {
        OperatorInfo opInfo = null;
        switch (type) {
            case BINARY:
                opInfo = binaryMap.get(name);
                break;
            case UNARY:
                opInfo = prefixMap.get(name);
                break;
            case VALUE:
                throw new CalculationException("No number as operators.");
        }
        if (opInfo == null)
            throw new CalculationException(
                    (type == NodeType.BINARY ? "Binary" : "Unary") + " operator \"" + name + "\" doesn't exist."
            );
        else {
            OperatorNode node;
            if (opInfo.special) {
                try {
                    return opInfo.specialClass.newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    throw new CalculationException(
                            "Operator \"" + name + "\" is not instantiable."
                    );
                }
            } else {
                switch (type) {
                    case BINARY:
                        return new DefaultBinaryImpl(opInfo);
                    case UNARY:
                        return new DefaultUnaryImpl(opInfo);
                    default: // this should not be called
                        return null;
                }
            }
        }
    }

    public static OperatorNode getOperator(char name, NodeType type) throws CalculationException {
        return getOperator(new String(new char[]{name}), type);
    }

    public static void registerOperator(OperatorInfo info) {
        registerOperator(info.name, info, info.type);
    }

    public static void registerOperator(String name, OperatorInfo opInfo, NodeType type) {
        switch (type) {
            case BINARY:
                binaryMap.put(name, opInfo);
                break;
            case UNARY:
                prefixMap.put(name, opInfo);
                break;
            case VALUE:
                throw new IllegalArgumentException("It is not allowed to register number nodes.");
        }
    }



}
