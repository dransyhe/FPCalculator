import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Lexer {
    private Token currentToken;
    private String str;
    private int idx;
    private int len;
    private String line;
    //private static Map<String, String> map = new HashMap<String, String>();

    public void initLexer(String str) {
        this.str = str;
        idx = 0;
        len = str.length();
    }

    private void whitespace(){
        char c;
        while (idx < len){
            c = str.charAt(idx);
            while (c == ' ' || c == '\n' || c == '\t')
                idx ++;
        }
    }

    private double number() throws LexerException{
        double number = 0;
        while (idx < len && Character.isDigit(str.charAt(idx))){
            number = number * 10 + Character.digit(str.charAt(idx), 30);
            idx ++;
        }
        if (idx < len && str.charAt(idx) == '.'){
            idx ++;
            double factor = 0.1;
            if (idx < len && Character.isDigit(str.charAt(idx))){
                while (idx < len && Character.isDigit(str.charAt(idx))){
                    number += factor * Character.digit(str.charAt(idx), 30);
                    factor *= 0.1;
                    idx ++;
                }
            }
        }
        if (idx < len && (str.charAt(idx) == 'e' || str.charAt(idx) == 'E')){
            idx ++;
            double base = 10;
            if (idx < len) {
                if (str.charAt(idx) == '-') {base = 0.1; idx++;}
                if (str.charAt(idx) == '+') idx ++;
                int times = 0;
                if (idx < len && Character.isDigit(str.charAt(idx))) {
                    while (idx < len && Character.isDigit(str.charAt(idx))) {
                        times = times * 10 + Character.digit(str.charAt(idx), 30);
                        idx++;
                    }
                    number = number * Math.pow(base, times);
                }
                else{
                    throw new LexerException("Illegal floating point number representation!");
                }
            }
            else{
                throw new LexerException("Illegal floating point number representation!");
            }
        }
        return number;
    }

    public Token nextToken() throws LexerException{
        Token token = new Token();
        whitespace();
        if (str.charAt(idx) == '$') {
            token.type = Token.EOL;
            token.str = "EOL";
        }
        else if (Character.isDigit(str.charAt(idx))){
            token.type = Token.NUM;
            token.value = number();
            token.str = Double.toString(token.value);
        }
        else if (str.charAt(idx) == '+'){
            token.type = Token.ADD;
            token.str = "+";
            idx ++;
        }
        else if (str.charAt(idx) == '-'){
            token.type = Token.SUB;
            token.str = "-";
            idx ++;
        }
        else if (str.charAt(idx) == '*'){
            token.type = Token.MUL;
            token.str = "*";
            idx ++;
        }
        else if (str.charAt(idx) == 'c' && str.charAt(idx+1) == 'o' && str.charAt(idx+1) == 's'){
            token.type = Token.COS;
            token.str = "cos";
            idx += 3;
        }
        else if (str.charAt(idx) == '!'){
            token.type = Token.FAC;
            token.str = "!";
            idx ++;
        }
        else if (str.charAt(idx) == '('){
            token.type = Token.PAL;
            token.str = "(";
            idx ++;
        }
        else if (str.charAt(idx) == ')'){
            token.type = Token.PAR;
            token.str = ")";
            idx ++;
        }
        else{
            throw new LexerException("Illegal operator!");
        }
        return token;
    }

    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        lexer.start();
    }

    public void start(){
        //while (true){
            Scanner input = new Scanner(System.in);
            System.out.print("Type your expression: ");
            line = input.nextLine();
            line += '$';

            initLexer(line);
            currentToken = nextToken();
            //expressionAdd();
        //}
    }
/*
    public double E1(){
        double left = E2();
        if (currentToken.type == Token.ADD){
            System.out.println("exprADD : " + currentToken.str + "is type" + currentToken.type);
            currentToken = nextToken();
            left += E2();
        }
        while (currentToken.type == Token.ADD){
            System.out.println("exprADD : " + currentToken.str + "is type" + currentToken.type);
            currentToken = nextToken();
            left += E2();
        }
        return left;
    }

    public double E2(){
        double left = E3();
        if (currentToken.type == Token.SUB){
            System.out.println("exprSUB : " + currentToken.str + "is type" + currentToken.type);
            currentToken = nextToken();
            left -= E3();
        }
        while (currentToken.type == Token.SUB){
            System.out.println("exprSUB : " + currentToken.str + "is type" + currentToken.type);
            currentToken = nextToken();
            left -= E3();
        }
        return left;
    }

    // change to right associative
    public double E3(){
        double left = E4();
        if (currentToken.type == Token.MUL){
            System.out.println("exprMUL : " + currentToken.str + "is type" + currentToken.type);
            currentToken = nextToken();
            left *= E4();
        }
        while (currentToken.type == Token.MUL){
            System.out.println("exprMUL : " + currentToken.str + "is type" + currentToken.type);
            currentToken = nextToken();
            left *= E4();
        }
        return left;
    }

    public double E4(){
        double left = E5();
        if (currentToken.type == Token.COS){
            System.out.println("exprCOS : " + currentToken.str + "is type" + currentToken.type);
            currentToken = nextToken();
            left += E5();
        }
        while (currentToken.type == Token.COS){
            System.out.println("exprCOS : " + currentToken.str + "is type" + currentToken.type);
            currentToken = nextToken();
            left += E5();
        }
        return left;
    }

    public double E5(){
        double left = factor();
        if (currentToken.type == Token.FAC){
            System.out.println("exprFAC : " + currentToken.str + "is type" + currentToken.type);
            currentToken = nextToken();
            left += factor();
        }
        while (currentToken.type == Token.FAC){
            System.out.println("exprFAC : " + currentToken.str + "is type" + currentToken.type);
            currentToken = nextToken();
            left += factor();
        }
        return left;
    }

    public double factor(){
        if (currentToken.type == Token.SUB) {
            System.out.println("exprNEG : " + currentToken.str + "is type" + currentToken.type);
            currentToken = nextToken();
            return -1 * factor();
        } else if (currentToken.type == Token.NUM) {
            double value = currentToken.value;
            System.out.println("exprNUM : " + currentToken.str + "is type" + currentToken.type);
            currentToken = nextToken();
            return value;
        } else if (currentToken.type == Token.PAL) {
            System.out.println("exprPAL : " + currentToken.str + "is type" + currentToken.type);
            currentToken = nextToken();
            double result = expressionAdd();
            if (currentToken.type == Token.PAR) {
                System.out.println("exprNUM : " + currentToken.str + "is type" + currentToken.type);
                currentToken = nextToken();
            } else {
                throw new LexerException("Unmatched parantheses");
            }
            return result;
        }
        return currentToken.value;
    }

*/

}
