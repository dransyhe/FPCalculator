import java.util.HashMap;
import java.util.Map;

public class Lexer {
    private Token currentToken;
    private String str;
    private int idx; // current index in source
    private int len; // length of source
    private String line;
    private static Map<String, String> map = new HashMap<String, String>();

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
        /*
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
        }*/
        return number;
    }

    public Token nextToken() throws LexerException{
        Token token = new Token();

        whitespace();
        if (idx >= len) {
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
        else{
            throw new LexerException("Illegal operator!");
        }

        return token;
    }
}
