import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException{
        // read grammar
        Path path1 = Paths.get("rulelist.txt");
        String input = Files.readString(path1, StandardCharsets.US_ASCII);
        Grammar grammar = new Grammar(input);

        // read input string to be lexed & parsed
        Path path2 = Paths.get("input.txt");
        input = Files.readString(path2, StandardCharsets.US_ASCII);

        // lexer into tokens
        Lexer lexer = new Lexer(input);
        ArrayList<Token> tokens = lexer.lex();
        System.out.println(tokens.toString());

        // parser into parser tree
        Parser parser = new Parser(grammar);
        parser.parse(tokens);
        System.out.println(parser.toString());
    }

}
