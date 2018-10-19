package ml.rhacoal.polishnotation.node.valuenode;

import ml.rhacoal.polishnotation.node.NodeBase;
import ml.rhacoal.polishnotation.node.NodeType;

import java.util.HashMap;
import java.util.Map;

/**
 * Proposition identifier node.
 * Instances of this class can only be constructed with {@link PropositionIdentifierNode#getIdentifier(Map, String)}. If the identifier
 * already exists, it returns the previous existing identifier.
 */
public class PropositionIdentifierNode implements NodeBase {

    /**
     * Initializes the {@link HashMap<String,   PropositionIdentifierNode  >} that holds the mapping between identifier strings and
     * actual identifier nodes.
     */
    /*static {
        identifierMap = new HashMap<>();
    }*/

    /**
     * Returns a {@link PropositionIdentifierNode} instance.
     * <p>If a identifier is previously queried, the same identifier node as queried is returned instead of a new instance. This
     * avoids multiple {@link PropositionIdentifierNode} instances sharing the same identifier string. </p>
     * <p>Only legal identifiers or legal keywords of Java should be provided. This function DOES NOT check the
     * legality of the identifier. However, "T" or "F" is not suggested.</p>
     * @param identifierMap the identifier map for unique namespaces between different formulas
     * @param identifier the identifier string to be queried
     * @return a {@link PropositionIdentifierNode} instance corresponding to the identifier string
     */
    public static PropositionIdentifierNode getIdentifier(Map<String, PropositionIdentifierNode> identifierMap, String identifier) {
        //check special identifiers
        if (identifier.equals("T")) {
            return TRUE;
        }
        if (identifier.equals("F")) {
            return FALSE;
        }
        //check whether the identifier string has been queried to avoid multiple instances of the same identifier to be created.
        if (identifierMap.containsKey(identifier)) {
            return identifierMap.get(identifier);
        } else {
            PropositionIdentifierNode newIdentifier = new PropositionIdentifierNode(identifier);
            identifierMap.put(identifier, newIdentifier);
            return newIdentifier;
        }
    }

    /**
     * Sets the value of a identifier for calculation purposes.
     * Since all Nodes in a
     * @param identifierMap the identifier map for unique namespaces between different formulas
     * @param identifier the identifier of the proposition
     * @param value the value to set
     */
    public static void setValue(Map<String, PropositionIdentifierNode> identifierMap, String identifier, boolean value) {
        getIdentifier(identifierMap, identifier).value = value;
    }

    /**
     * Gets the identifier of the proposition.
     * @return the identifier of the proposition.
     */
    public String getStringRepresentation() {
        return identifier;
    }

    @Override
    public NodeType type() {
        return NodeType.VALUE;
    }

    @Override
    public int precedence() {
        return Integer.MAX_VALUE;
    }

    /**
     * Gets the string representation of the node.
     * For instance, this methods returns "Q" for a node created with {@code PropositionIdentifierNode.getIdentifier("Q")}
     * @return the string representation of the node.
     */
    @Deprecated
    public String toString() {
        return getStringRepresentation();
    }

    @Override
    public String toExpression() {
        return getStringRepresentation();
    }

    @Override
    public StringBuilder toExpression(StringBuilder stringBuilder) {
        return stringBuilder.append(getStringRepresentation());
    }

    @Override
    public String toPolishNotation() {
        return getStringRepresentation();
    }

    @Override
    public StringBuilder toPolishNotation(StringBuilder stringBuilder) {
        return stringBuilder.append(getStringRepresentation());
    }

    @Override
    public String toReversePolishNotation() {
        return getStringRepresentation();
    }

    @Override
    public StringBuilder toReversePolishNotation(StringBuilder stringBuilder) {
        return stringBuilder.append(getStringRepresentation());
    }

    @Override
    public boolean calculate() {
        return value;
    }

    /**
     * Sets the value of the identifier node for calculation purposes.
     * @param value the value to set
     */
    public void setValue(boolean value) {
        this.value = value;
    }

    /**
     * A private constructor to avoid unwanted calls.
     * @see PropositionIdentifierNode#getIdentifier(Map, String)
     * @param identifier the identifier
     */
    private PropositionIdentifierNode(String identifier) {
        this.identifier = identifier;
    }

    private final String identifier;
    private boolean value;
    //private static final HashMap<String, PropositionIdentifierNode> identifierMap; //now works as a parameter

    private static final class FinalPropositionNode extends PropositionIdentifierNode {
        private FinalPropositionNode(String identifier, boolean value) {
            super(identifier);
            super.value = value;
        }
        /**
         * Does not work.
         * @param value the value to set
         */
        public void setValue(boolean value) {
            return;
        }
    }
    private static final PropositionIdentifierNode TRUE = new FinalPropositionNode("T", true);
    private static final PropositionIdentifierNode FALSE = new FinalPropositionNode("F", false);

}
