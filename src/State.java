import java.util.*;

public class State {
    LinkedHashSet<Item> items;
    HashMap<String, State> transition;

    // initialise from a grammar and a core item set
    // generate closure from grammar
    public State(Grammar grammar, HashSet<Item> coreItems){
        items = new LinkedHashSet<>(coreItems);
        transition = new HashMap<>();
        closure(grammar);
    }

    // generate closure from grammar
    private void closure(Grammar grammar){
        boolean finished;
        do{
            finished = true;
            HashSet<Item> temp = new HashSet<>();
            for (Item item : items){
                String current = item.getCurrentTerminal();
                if (current != null && grammar.isNonTerminal(current)){
                    HashSet<Rule> rules = grammar.getRuleByLeftside(current);
                    temp.addAll(createItem(rules));
                }
            }
            if (!items.containsAll(temp)){
                items.addAll(temp);
                finished = false;
            }
        } while(!finished);
    }

    // used by closure() to create an item set from rules
    private HashSet<Item> createItem(HashSet<Rule> rules){
        HashSet<Item> itemset = new HashSet<>();
        for (Rule rule : rules)
            itemset.add(new Item(rule));
        return itemset;
    }

    public LinkedHashSet<Item> getItems(){
        return items;
    }

    public HashMap<String, State> getTransition(){
        return transition;
    }

    public void addTransition(String str, State state){
        transition.put(str, state);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.items);
        hash = 83 * hash + Objects.hashCode(this.transition);
        return hash;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final State other = (State) obj;
        if (!(this.items.containsAll(other.items) && other.items.containsAll(this.items))) return false;
        if (!Objects.equals(this.transition, other.transition)) return false;
        return true;
    }

    @Override
    public String toString() {
        String str = "";
        for(Item item : items) str += item + "\n";
        return str;
    }
}
