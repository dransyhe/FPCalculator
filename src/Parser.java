import java.util.ArrayList;
import java.util.Stack;

public class Parser {
    protected Grammar grammar;
    protected ParserTable tables;
    protected String result;

    public Parser(Grammar grammar){
        this.grammar = grammar;
        tables = new ParserTable(grammar);
    }

    public String parse(ArrayList<Token> tokens) throws ParserException{
        Stack<String> stack = new Stack<>();
        Stack<String> input = new Stack<>();
        Stack<String> output = new Stack<>();
        for (int i = tokens.size() - 1; i > -1; i --) input.push(tokens.get(i).str);
        stack.add("0");
        result = output.toString() + input.toString() + stack.toString() + "\n";

        int idx = 0;
        while (idx < tokens.size()){
            int state = Integer.valueOf(stack.peek());
            String nextInput = tokens.get(idx).str;
            Action action = tables.getActionTable()[state].get(nextInput);
            if (action == null) throw new ParserException("Error");
            else if (action.getType() == Action.SHIFT){
                stack.push(nextInput);
                stack.push(action.getOperand() + "");
                idx ++;
                output.push(input.pop());
            }
            else if (action.getType() == Action.REDUCE){
                int ruleIdx = action.getOperand();
                Rule rule = grammar.getRules().get(ruleIdx);
                String leftside = rule.getLeftSide();
                for (int i = 0; i < 2 * rule.getRightSide().length; i ++) stack.pop();
                int nextState = Integer.valueOf(stack.peek());
                stack.push(leftside);
                int nonTerminalState = tables.getGoToTable()[nextState].get(leftside);
                stack.push(nonTerminalState + "");
                input.push(leftside);
            }
            else if (action.getType() == Action.ACCEPT){
                return output.peek();
            }
            result += output.toString() + input.toString() + stack.toString() + "\n";
        }
        throw new ParserException("Parsing failed");
    }

    public String toString() throws ParserException{
        if (result != "") return result;
        else throw new ParserException("Empty parsing");
    }

}
