public class Action {
    private int type;
    private int operand;

    public Action(int type, int operand){
        this.type = type;
        this.operand = operand;
    }

    public int getType(){
        return type;
    }

    public int getOperand(){
        return operand;
    }

    @Override
    public String toString() {
        return type + " " + (type == 0 ? "":operand);
    }

    public static final int ACCEPT = 0;
    public static final int SHIFT = 1;
    public static final int REDUCE = 2;
}
