import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ParserTable {
    protected Grammar grammar;
    private ArrayList<State> canonicalCollection;
    protected HashMap<String, Integer>[] goToTable;
    protected HashMap<String, Action>[] actionTable;
    protected int size;

    public ParserTable(Grammar grammar){
        this.grammar = grammar;
        createStates();
        createGoToTable();
        createActionTable();
    }

    public HashMap<String, Integer>[] getGoToTable(){ return goToTable; }
    public HashMap<String, Action>[] getActionTable(){ return actionTable; }

    protected void createStates(){
        canonicalCollection = new ArrayList<>();
        HashSet<Item> start = new HashSet<>();
        start.add(new Item(grammar.getRules().get(0)));

        State startState = new State(grammar, start);
        canonicalCollection.add(startState);

        for (int i = 0; i < canonicalCollection.size(); i ++){
            // find all terminals to be 'expanded'
            HashSet<String> stringWithDot = new HashSet<>();
            for (Item item : canonicalCollection.get(i).getItems()){
                if (item.getCurrentTerminal() != null)
                    stringWithDot.add(item.getCurrentTerminal());
            }
            for (String str : stringWithDot){
                HashSet<Item> nextStateItems = new HashSet<>();
                for (Item item : canonicalCollection.get(i).getItems()){
                    if (item.getCurrentTerminal() != null && item.getCurrentTerminal().equals(str)){
                        Item temp = new Item(item);
                        temp.goTo();
                        nextStateItems.add(temp);
                    }
                }
                // check if nextState exists in current canonicalCollection
                // if so, add transition; otherwise, append new state to collection
                State nextState = new State(grammar, nextStateItems);
                boolean isExist = false;
                for (int j = 0; j < canonicalCollection.size(); j ++){
                    if (canonicalCollection.get(i).getItems().containsAll(nextState.getItems())
                            && nextState.getItems().containsAll(canonicalCollection.get(i).getItems())){
                        isExist = true;
                        canonicalCollection.get(i).addTransition(str, canonicalCollection.get(j));
                    }
                }
                if (!isExist) {
                    canonicalCollection.add(nextState);
                    canonicalCollection.get(i).addTransition(str, nextState);
                }
            }
        }

        size = canonicalCollection.size();
    }

    private int findStateIndex(State state){
        for (int i = 0; i < size; i ++)
            if (canonicalCollection.get(i).equals(state))
                return i;
        return -1;
    }

    protected void createGoToTable(){
        goToTable = new HashMap[size];
        for (int i = 0; i < size; i ++) goToTable[i] = new HashMap<>();
        for (int i = 0; i < size; i ++) {
            for (String str : canonicalCollection.get(i).getTransition().keySet())
                if (grammar.getNonterminals().contains(str))
                    goToTable[i].put(str, findStateIndex(canonicalCollection.get(i).getTransition().get(str)));
        }
    }

    protected void createActionTable() throws ParserException{
        actionTable = new HashMap[size];
        for (int i = 0; i < size; i ++) actionTable[i] = new HashMap<>();
        for (int i = 0; i < size; i ++) {
            for (String str : canonicalCollection.get(i).getTransition().keySet())
                if (grammar.getTerminals().contains(str))
                    actionTable[i].put(str, new Action(Action.SHIFT, findStateIndex(canonicalCollection.get(i).getTransition().get(str))));
        }

        for (int i = 0; i < size; i ++){
            for (Item item : canonicalCollection.get(i).getItems()) {
                if (item.getDotPointer() == item.getRightSide().length)
                    if (item.getLeftSide().equals("S")) actionTable[i].put("$", new Action(Action.ACCEPT, 0));
                    else {
                        HashSet<String> terminals = grammar.getTerminals();
                        terminals.add("$");
                        Rule rule = new Rule(item.getLeftSide(), item.getRightSide().clone());
                        int index = grammar.findRuleIndex(rule);
                        Action action = new Action(Action.REDUCE, index);
                        for (String str : terminals) {
                            if (actionTable[i].get(str) != null)
                                throw new ParserException("\"it has a REDUCE-\" + actionTable[i].get(str).getType() + \" confilct in state \" + i");
                            else
                                actionTable[i].put(str, action);
                        }
                    }
            }
        }
    }



    /******** PRINTING goToTable, actionTable, canonicalCollection ********/

    public String goToTableStr() {
        String str = "Go TO Table : \n";
        str += "          ";
        for (String variable : grammar.getNonterminals()) {
            str += String.format("%-6s",variable);
        }
        str += "\n";

        for (int i = 0; i < goToTable.length; i++) {
            for (int j = 0; j < (grammar.getNonterminals().size()+1)*6+2; j++) {
                str += "-";
            }
            str += "\n";
            str += String.format("|%-6s|",i);
            for (String variable : grammar.getNonterminals()) {
                str += String.format("%6s",(goToTable[i].get(variable) == null ? "|" : goToTable[i].get(variable)+"|"));
            }
            str += "\n";
        }
        for (int j = 0; j < (grammar.getNonterminals().size()+1)*6+2; j++) {
            str += "-";
        }
        return str;
    }

    public String actionTableStr() {
        String str = "Action Table : \n";
        HashSet<String> terminals = new HashSet<>(grammar.getTerminals());
        terminals.add("$");
        str += "                ";
        for (String terminal : terminals) {
            str += String.format("%-10s" , terminal);
        }
        str += "\n";

        for (int i = 0; i < actionTable.length; i++) {
            for (int j = 0; j < (terminals.size()+1)*10+2; j++) {
                str += "-";
            }
            str += "\n";
            str += String.format("|%-10s|",i);
            for (String terminal : terminals) {
                str += String.format("%10s",(actionTable[i].get(terminal) == null ? "|" : actionTable[i].get(terminal) + "|"));
            }
            str += "\n";
        }
        for (int j = 0; j < (terminals.size()+1)*10+2; j++) {
            str += "-";
        }
        return str;
    }

    public String canonicalCollectionStr() {
        String str = "Canonical Collection : \n";
        for (int i = 0; i < canonicalCollection.size(); i++) {
            str += "State " + i + " : \n";
            str += canonicalCollection.get(i)+"\n";
        }
        return str;
    }
}
