package ml.rhacoal.polishnotation;

import ml.rhacoal.polishnotation.node.NodeBase;
import ml.rhacoal.polishnotation.node.valuenode.PropositionIdentifierNode;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    static {
        String tempHelp =
                "--- Simple (Reverse) Polish Notation Converter ---\n"    +
                "author: Rhacoal %s\n"    +
                "github: https://github.com/Rhacoal/PolishNotation\n"     +
                "--------------------------------------------------\n"    +
                "to convert a propositional formula, enter the formula\n" +
                "to toggle the outputs, enter ':toggle' for more info\n"  +
                "to end the program, enter ':exit'"
        ;
        String s0 = "";
        try {
            Class<?> personal = Class.forName("ml.rhacoal.polishnotation.AuthorPersonalInfo");
            Field f = personal.getDeclaredField("AUTHOR_ADDITIONAL_INFO");
            s0 = ((String) f.get(null));
        } catch (Exception e) {
            // ignore any exception(s)
        }
        BASIC_HELP = String.format(tempHelp, s0);
    }

    private static final String BASIC_HELP;
    private static final String TOGGLE_HELP =
            "enable/disable an output: ':toggle <output> [on|off]'\n" +
            "available outputs:\n"                                    +
            "PN  : polish notation\n"                                 + //id: 0
            "RPN : reverse polish notation\n"                         + //    1
            "TT  : truth table\n"                                     + //    2
            "PCNF: principal conjunctive normal form\n"               + //    3
            "PDNF: principal disjunctive normal form"                   //    4
            ;
    private static final String[] OUTPUTS = new String[]{"PN", "RPN", "TT", "PCNF", "PDNF"};

    public static void main(String[] args)  {
        Scanner scanner = new Scanner(System.in); // defines and initializes a Scanner instance to read System.in
        System.out.println(BASIC_HELP);
        boolean[] output = new boolean[]{true, true, true, true, true};
        mainLoop: while (scanner.hasNextLine()) { // read until EOF
            try {
                String line = scanner.nextLine();
                if (line.isEmpty()) { // ignore empty lines
                    continue;
                }
                if (line.charAt(0) == ':') { // commands
                    String[] command = line.split(" ");
                    switch(command[0]) {
                        case ":toggle":
                            if (command.length > 1) {
                                // if no <on/off> is provided, on is default
                                // if extra arguments are provided, they are ignored
                                boolean value = command.length == 2 || command[2].equals("on");
                                // find the corresponding id of the output
                                int index = -1;
                                for (int i = 0; i < OUTPUTS.length; ++ i) {
                                    if (OUTPUTS[i].equalsIgnoreCase(command[1])) {
                                        index = i;
                                    }
                                }
                                if (index == -1) { // unsupported output
                                    System.out.println("Unsupported output: " + command[1]);
                                } else {
                                    output[index] = value;
                                    System.out.println("Toggled " + command[1] + " output " + (value ? "on" : "off"));
                                }
                            } else {
                                System.out.println(TOGGLE_HELP);
                            }
                            break;
                        case ":exit":
                            System.out.println("Exiting...");
                            break mainLoop;
                        default:
                            System.out.println("Unknown command: " + command[0]);
                    }
                    continue;
                }
                Calculator.Result result = Calculator.calculates(line);
                NodeBase nb = result.root;
                HashMap<String, PropositionIdentifierNode> identifierMap = result.identifierMap;
                if (output[0]) System.out.println("PN : " + nb.toPolishNotation());
                if (output[1]) System.out.println("RPN: " + nb.toReversePolishNotation());
                if (output[2] || output[3] || output[4]) {
                    Calculator.TruthTable table = Calculator.getTruthTable(nb, 0, identifierMap);
                    if (output[2]) {
                        System.out.println("Truth Table");
                        System.out.println(table.toString());
                    }
                    if (output[3]) {
                        int[] pcnf = table.calculatePrincipalConjunctiveNormalForm();
                        System.out.print("∧");
                        for (int i = 0; i < pcnf.length; ++ i) {
                            System.out.print(pcnf[i]);
                            if (i != pcnf.length - 1) {
                                System.out.print(',');
                            }
                        }
                        System.out.println();
                    }
                    if (output[4]) {
                        int[] pdnf = table.calculatePrincipalDisjunctiveNormalForm();
                        System.out.print("∨");
                        for (int i = 0; i < pdnf.length; ++ i) {
                            System.out.print(pdnf[i]);
                            if (i != pdnf.length - 1) {
                                System.out.print(',');
                            }
                        }
                        System.out.println();
                    }
                }
            } catch (CalculationException ce) {
                System.out.println("Calculation exception occurred: " + ce.getMessage());
            }
        }
    }
}
