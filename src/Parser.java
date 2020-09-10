import java.util.ArrayList;
import java.util.Stack;

public class Parser {
    protected Grammar grammar;
    protected ParserTable tables;

    public Parser(Grammar grammar){
        this.grammar = grammar;
        tables = new ParserTable(grammar);
    }

    public void parse(ArrayList<Token> tokens) throws ParserException{
        Stack<String> stack = new Stack<>();
        stack.add("0");

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
                printParseTree();
            }
            else if (action.getType() == Action.ACCEPT){
                break;
            }
        }
    }

    protected void printParseTree(){

    }


}
