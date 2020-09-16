public class Token {
    public int type;
    public double value;
    public String str;

    public static final int EOL=0;     // End Of Line
    public static final int PAL=1;     // Left Parenthesis
    public static final int PAR=2;     // Right Parenthesis
    public static final int ADD=3;     // operators
    public static final int SUB=4;
    public static final int MUL=5;
    public static final int COS=6;
    public static final int FAC=7;
    public static final int NUM=7;     // number
    public static final int ID=10;     // identifier
    public static final int END=11;    // exit

}
