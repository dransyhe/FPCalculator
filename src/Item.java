import java.util.Arrays;
import java.util.Objects;

public class Item extends Rule{
    protected int dotPointer;

    public Item(String leftSide, String[] rightSide, int dotPointer){
        super(leftSide, rightSide);
        this.dotPointer = dotPointer;
    }

    public Item(Rule rule){
        super(rule.getLeftSide(), rule.getRightSide());
        if (rule.getRightSide().length == 1 && rule.getRightSide()[0].equals("epsilon"))
            dotPointer = 1;
        else
            dotPointer = 0;
    }

    public Item(Item item){
        super(item);
        this.dotPointer = item.getDotPointer();
    }

    public int getDotPointer(){
        return dotPointer;
    }

    public boolean goTo(){
        if (dotPointer >= rightSide.length) return false;
        dotPointer ++;
        return true;
    }

    public String getCurrentTerminal(){
        if (dotPointer == rightSide.length) return null;
        return rightSide[dotPointer];
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.dotPointer;
        hash = 89 * hash + Objects.hashCode(this.leftSide);
        hash = 89 * hash + Arrays.deepHashCode(this.rightSide);
        return hash;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Item other = (Item) obj;
        if (this.dotPointer != other.dotPointer) return false;
        if (!Objects.equals(this.leftSide, other.leftSide)) return false;
        if (!Arrays.equals(this.rightSide, other.rightSide)) return false;
        return true;
    }

    @Override
    public String toString(){
        String str = leftSide + " -> ";
        for (int i = 0; i < rightSide.length; i++) {
            if (i == dotPointer) str += ".";
            str += rightSide[i];
            if(i != rightSide.length - 1) str += " ";
        }
        if (rightSide.length == dotPointer) str += ".";
        return str;
    }
}
