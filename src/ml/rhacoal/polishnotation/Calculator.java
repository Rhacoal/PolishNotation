package ml.rhacoal.polishnotation;

import ml.rhacoal.polishnotation.function.CharToBoolean;
import ml.rhacoal.polishnotation.node.NodeBase;
import ml.rhacoal.polishnotation.node.NodeType;
import ml.rhacoal.polishnotation.node.operatornode.OperatorNode;
import ml.rhacoal.polishnotation.node.operatornode.ParenthesisNode;
import ml.rhacoal.polishnotation.node.operatornode.UnaryOperator;
import ml.rhacoal.polishnotation.node.valuenode.PropositionIdentifierNode;

import java.util.*;

/**
 * The class {@code Calculator} provides an interface for calculation.
 * An instance of calculator uses {@link String} as input and returns a root node for further calculation.
 */
public class Calculator {

    private final String input;
    private final char[] sequence;
    private final int size;
    private int index;
    private OperatorNode former;
    private UnaryOperator root;

    private enum ExpectationType {
        UNARY_OR_PROPOSITION, BINARY_OR_RIGHT_PARENTHESES, NULL
    }

    /**
     * Provides a standalone namespace for the expression.
     */
    private final HashMap<String, PropositionIdentifierNode> identifierMap;

    private Calculator(String input) {
        this.input = input;
        this.sequence = input.toCharArray();
        this.size = this.input.length();
        this.identifierMap = new HashMap<>();
        this.root = new UnaryOperator() {
            @Override
            public String name() {
                return "";
            }

            @Override
            public int precedence() {
                return -1;
            }

            @Override
            public boolean calculate() {
                return getRightChild().calculate();
            }
        };
    }

    /**
     * Reads a identifier that matches the regex: {@code [a-zA-Z0-9_]+}.
     * It's not forbidden to start the identifier with numbers.
     * @return the identifier
     */
    private PropositionIdentifierNode readProposition() {
        skipIf(Not(Calculator::isIdentifierCharacter));
        int startIndex = index;
        skipIf(Calculator::isIdentifierCharacter);
        //System.out.println("start-end: " + startIndex + " " + index);
        return PropositionIdentifierNode.getIdentifier(identifierMap, input.substring(startIndex, index));
    }

    private ExpectationType expectUnaryOrProposition() throws CalculationException {
        skipIf(Calculator::isBlank);
        if (index == size) {
            throw new CalculationException("Unexpected end of expression.");
        }

        if (isIdentifierCharacter(sequence[index])) { // proposition identifier
            PropositionIdentifierNode node = readProposition();
            former.updateRightChild(node);
            return ExpectationType.BINARY_OR_RIGHT_PARENTHESES;
        } else  { // unary operator (including parenthesis)
            OperatorNode op = OperatorRegistry.getOperator(sequence[index++], NodeType.UNARY);
            former.updateRightChild(op);
            op.updateFather(former);
            former = op;
            return ExpectationType.UNARY_OR_PROPOSITION;
        }
    }

    private ExpectationType expectBinaryOrRightParenthesis() throws CalculationException {
        skipIf(Calculator::isBlank);
        if (index == size) {
            return ExpectationType.NULL;
        }

        if (sequence[index] == ')') { // right parenthesis
            //find the paired left parenthesis
            while (!(former instanceof ParenthesisNode) || ((ParenthesisNode) former).isClose()) {
                former = former.getFather();
                if (former == null) {
                    throw new CalculationException("No matching left parenthesis found for ')' at " + index);
                }
            }
            ((ParenthesisNode) former).close();
            former = former.getFather();
            index ++;
            return ExpectationType.BINARY_OR_RIGHT_PARENTHESES;
        } else {
            OperatorNode node = OperatorRegistry.getOperator(sequence[index++], NodeType.BINARY);
            //OperatorNode on = former;
            while (former.precedence() >= node.precedence()) {
                // if the precedence of the former operator is lower, this operator should be calculated earlier
                // if the precedence of the former operator is the same, the former operator should be calculated earlier

                // find the father that holds a lower precedence
                former = former.getFather();
            }
            /*    former      former
                   /  \   ->   /  \
                  fl  fr      fl node
                                 /  \
                                fr  null
            */
            NodeBase formerRight = former.updateRightChild(node);
            node.updateLeftChild(formerRight);
            node.updateFather(former);
            if (formerRight instanceof OperatorNode) {
                ((OperatorNode) formerRight).updateFather(node);
            }
            former = node;
            return ExpectationType.UNARY_OR_PROPOSITION;
        }
    }

    private void skipIf(CharToBoolean func) {
        while (index != size && func.eval(sequence[index])) {
            index ++;
        }
    }

    private static CharToBoolean Not(CharToBoolean func) {
        return c -> !func.eval(c);
    }

    private static boolean isIdentifierCharacter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || (c == '_');
    }

    private static boolean isBlank(char c) {
        return c == ' ' || c == '\t' || c == '\n';
    }

    private int locateRightParenthesis(int startIndex, int endIndex) throws CalculationException {
        int left_cnt = 0;
        int index = startIndex;
        while (index < endIndex) {
            if (sequence[index] == '(') {
                left_cnt += 1;
            } else if (sequence[index] == ')') {
                left_cnt -= 1;
            }
            if (left_cnt == 0) {
                return index;
            }
            index ++;
        }
        throw new CalculationException("No matching right parenthesis detected for '(' at " + startIndex);
    }

    private Calculator.Result calculate() throws CalculationException {
        index = 0;
        former = root;
        ExpectationType et = ExpectationType.UNARY_OR_PROPOSITION;
        while (et != ExpectationType.NULL) {
            switch (et) {
                case UNARY_OR_PROPOSITION:
                    //System.out.println("U" + index);
                    et = expectUnaryOrProposition();
                    break;
                case BINARY_OR_RIGHT_PARENTHESES:
                    //System.out.println("B" + index);
                    et = expectBinaryOrRightParenthesis();
                    break;
            }
        }
        return new Calculator.Result(root.getRightChild(), identifierMap);
    }

    public static Result calculates(String expression) throws CalculationException {
        Calculator calc = new Calculator(expression);
        return calc.calculate();
    }

    public static class Result {
        public final NodeBase root;
        public final HashMap<String, PropositionIdentifierNode> identifierMap;
        public Result(NodeBase root, HashMap<String, PropositionIdentifierNode> identifierMap) {
            this.root = root;
            this.identifierMap = identifierMap;
        }
    }

    /**
     * Represents a truth table.
     */
    public static class TruthTable {

        private final ArrayList<PropositionIdentifierNode> propositions;
        private final NodeBase nodeBase;
        private ArrayList<ArrayList<Boolean>> values;
        private int order;

        /**
         * A simplified {@link ArrayList} that only holds integers and only support inserting elements.
         */
        public static class IntList {

            private static final int INITIAL_CAPACITY = 2;
            private int[] data;
            private int size;
            private int modCount;

            public IntList() {
                data = new int[INITIAL_CAPACITY];
                size = 0;
            }

            public void add(int element) {
                ensureCapacity(size + 1);
                data[size++] = element;
            }

            public void add(int index, int element) {
                if (index == size) {
                    add(element);
                    return;
                }
                ensureCapacity(size + 1);
                System.arraycopy(data, index, data, index + 1, size - index);
                data[index] = element;
                ++size;
            }

            public int[] toIntArray() {
                int[] result = new int[size];
                System.arraycopy(data, 0, result, 0, size);
                return result;
            }

            public void reverse() {
                for (int i = 0; i < size / 2; ++ i) {
                    int temp = data[i];
                    data[i] = data[size - 1 - i];
                    data[size - 1 - i] = temp;
                }
            }

            private void ensureCapacity(int minCapacity) {
                int currentCapacity = data.length;
                int targetCapacity = Math.max(currentCapacity + (currentCapacity >> 1), minCapacity);
                if (targetCapacity > currentCapacity) {
                    data = Arrays.copyOf(data, targetCapacity);
                }
            }



        }

        /**
         * Constructs a {@link TruthTable}.
         * This constructor accept an {@link ArrayList} of propositions in order and a {@link NodeBase} for calculation.
         * It is suggested to call {@link Calculator#getTruthTable(NodeBase, HashMap, int, Comparator)}.
         * <p><b>WARNING: </b>This class can handle at most 31 propositions.</p>
         * @see Calculator#getTruthTable(NodeBase, HashMap, int, Comparator)
         * @see Calculator#getTruthTable(NodeBase, HashMap, int)
         * @param orderedPropositions list of propositions in order
         * @param nodeBase the {@link NodeBase} for calculation.
         */
        public TruthTable(ArrayList<PropositionIdentifierNode> orderedPropositions, NodeBase nodeBase) {
            // copy the list in case of unwanted changes
            this.propositions = new ArrayList<>(orderedPropositions);
            if (propositions.size() > 31) {
                throw new UnsupportedOperationException("Too many propositions! Only 31 or less is accepted.");
            }
            this.nodeBase = nodeBase;
        }

        /**
         * Generates the whole table by given order.
         * <p>The calculation may take a while before it generates the whole table.</p>
         * <p>{@code order} can be of the following:</p>
         * <p>  0 - increasing order (from all false to all true)</p>
         * <p>  1 - decreasing order (from all true to all false)</p>
         * <p>Any other inputs will be regarded as 0</p>
         * <p><b>WARNING: </b>This method can handle at most 31 propositions.</p>
         * @param order the order of the table
         * @return itself
         */
        public TruthTable calculate(int order) {
            this.order = order;
            values = new ArrayList<>();
            ArrayList<Boolean> emptyLine = new ArrayList<>();
            for (int i = 0; i < propositions.size() + 1; ++i) {
                emptyLine.add(false);
            }
            for (int i = 0, count = propositions.size(), max = 1 << propositions.size(); i < max; ++ i) {
                ArrayList<Boolean> line = new ArrayList<>(emptyLine);
                int current = order == 0 ? i : max - 1 - i;
                for (int j = count - 1; j >= 0; -- j) {
                    boolean val = (current & 1) != 0;
                    propositions.get(j).setValue(val);
                    line.set(j, val);
                    current >>= 1;
                }
                line.set(count, nodeBase.calculate());
                values.add(line);
            }
            return this;
        }

        /**
         * Generates the string representation of the truth table with given symbols.
         * @param trueString the {@link String} representing true
         * @param falseString the {@link String} representing false
         * @param separator the separator between elements in one line.
         *                  Note that a separator will also be added to the end of the line.
         * @param lineSeparator the separator used between lines
         * @return the string representation of the truth table
         */
        public String toString(String trueString, String falseString, String separator, String lineSeparator) {
            StringBuilder builder = new StringBuilder();
            propositions.forEach(e -> builder.append(e).append(separator));
            builder.append(nodeBase.toExpression()).append(lineSeparator);
            values.forEach(line -> {
                line.forEach(e -> builder.append(e ? trueString : falseString).append(separator));
                builder.append(lineSeparator);
            });
            return builder.toString();
        }

        /**
         * Calculates the principal disjunctive normal form of a propositional formula from its truth table.
         * @return indices of minterms
         */
        public int[] calculatePrincipalDisjunctiveNormalForm() {
            IntList list = new IntList();
            for (int i = 0, s = values.size(), max = values.size(), propCnt = propositions.size(); i < values.size(); ++ i) {
                int index = order == 0 ? i : max - 1 - i;
                if (values.get(index).get(propCnt)) {
                    list.add(index);
                }
            }
            if (order == 1) {
                list.reverse();
            }
            return list.toIntArray();
        }

        /**
         * Calculates the principal conjunctive normal form of a propositional formula from its truth table.
         * @return indices of maxterms
         */
        public int[] calculatePrincipalConjunctiveNormalForm() {
            IntList list = new IntList();
            for (int i = 0, s = values.size(), max = values.size(), propCnt = propositions.size(); i < values.size(); ++ i) {
                int index = order == 0 ? i : max - 1 - i;
                if (!values.get(index).get(propCnt)) {
                    list.add(max - 1 - index);
                }
            }
            if (order == 0) {
                list.reverse();
            }
            return list.toIntArray();
        }

        @Override
        public String toString() {
            return toString("T", "F", "\t", "\n");
        }
    }

    /**
     * This method returns the truth table of a given {@link NodeBase} and its identifier map.
     * @param nodeBase the node base
     * @param identifierMap the identifier map
     * @param order the order to calculate and output. See {@link TruthTable#calculate(int)}
     * @param comparator the comparator used to determined the order of the propositions
     * @return the truth table
     */
    public static TruthTable getTruthTable(
            NodeBase nodeBase,
            HashMap<String, PropositionIdentifierNode> identifierMap,
            int order,
            Comparator<Map.Entry<String, PropositionIdentifierNode>> comparator) {
        ArrayList<Map.Entry<String, PropositionIdentifierNode>> list = new ArrayList<>(identifierMap.entrySet());
        list.sort(comparator == null ?
                Comparator.comparing(Map.Entry<String, PropositionIdentifierNode>::getKey)
                : comparator);
        ArrayList<PropositionIdentifierNode> propList = new ArrayList<>();
        list.forEach(e -> propList.add(e.getValue()));
        return new TruthTable(propList, nodeBase).calculate(order);
    }

    /**
     * This method returns the truth table of a given {@link NodeBase} and its identifier map.
     * By default, propositions are ordered in lexicographical order.
     * @param nodeBase the node base
     * @param order the order to calculate and output. See {@link TruthTable#calculate(int)}
     * @param identifierMap the identifier map
     * @return the truth table
     */
    public static TruthTable getTruthTable(
            NodeBase nodeBase,
            HashMap<String, PropositionIdentifierNode> identifierMap,
            int order
            ) {
        return getTruthTable(nodeBase, identifierMap, order, null);
    }






}
