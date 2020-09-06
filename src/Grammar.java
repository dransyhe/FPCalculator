import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class Grammar {
    private ArrayList<Rule> rules;
    private HashSet<String> terminals;
    private HashSet<String> nonterminals;
    private String start;

    public Grammar(String s){
        rules = new ArrayList<>();
        terminals = new HashSet<>();
        nonterminals = new HashSet<>();

        // read and initialise rules (leftside, rightside, start) line-by-line
        // format: "leftside -> rightside | rightside | rightside |..."
        boolean startflag = false;
        for (String line : s.split("\n")){
            String[] sides = line.split("->");
            String leftside = sides[0].trim();
            nonterminals.add(leftside);
            String[] rightsides = sides[1].trim().split("\\|");    // split by "|"
            for (String rule : rightsides){
                String[] rightside = rule.trim().split("\\s+");    // split by one or more spaces
                for (String terminal : rightside){
                    if (!terminal.equals("epsilon")) terminals.add(terminal);
                }
                if (!startflag){  // initialise start symbol
                    start = leftside;
                    rules.add(new Rule("S'", new String[]{start}));
                    startflag = true;
                }
                rules.add(new Rule(leftside, rightside));
            }
        }
        for (String nonterminal : nonterminals) terminals.remove(nonterminal);
    }

    public ArrayList<Rule> getRules(){
        return rules;
    }

    public HashSet<String> getTerminals(){
        return terminals;
    }

    public HashSet<String> getNonterminals(){
        return nonterminals;
    }

    public String getStartSymbol(){
        return start;
    }

    public int findRuleIndex(Rule rule){
        for(int i = 0 ; i < rules.size(); i ++)
            if(rules.get(i).equals(rule)) return i;
        return -1;
    }

    public boolean isNonTerminal(String str){
        return nonterminals.contains(str);
    }

    public HashSet<Rule> getRuleByLeftside(String leftside){
        HashSet<Rule> ruleset = new HashSet<>();
        for (Rule rule : rules)
            if (rule.getLeftSide().equals(leftside))
                ruleset.add(rule);
        return ruleset;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.rules);
        hash = 37 * hash + Objects.hashCode(this.terminals);
        hash = 37 * hash + Objects.hashCode(this.nonterminals);
        return hash;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Grammar other = (Grammar) obj;
        if (!Objects.equals(this.rules, other.rules)) return false;
        if (!Objects.equals(this.terminals, other.terminals)) return false;
        if (!Objects.equals(this.nonterminals, other.nonterminals)) return false;
        if (this.start != other.start) return false;
        return true;
    }

    @Override
    public String toString(){
        String str = "";
        for (Rule rule : rules) str += rule + "\n";
        return str;
    }
}
