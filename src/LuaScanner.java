import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LuaScanner {
    private static final String WHITESPACEREGEX = "(\\s+)|(\\s*,\\s*)|(\\t)";
    private static final String TOKENREGEX = "(\\b[a-z]\\b)|(\\b-?[0-9]+\\b)|(<=)|(<)|(>=)|(>)|(==)|(~=)|(=)|(\\+)|(-)|(\\*)|(/)|(\\bfunction\\b)|(\\bend\\b)|(\\bif\\b)|(\\bthen\\b)|(\\belse\\b)|(\\bwhile\\b)|(\\bdo\\b)|(\\brepeat\\b)|(\\buntil\\b)|(\\bprint\\b)|(\\()|(\\))";

    public static List<Pair<TokenType, String>> tokenList = new ArrayList<>();

    List<String> lines;
    int currentLine = 0;
    int currentIndex = 0;
    Token nextToken;

    public LuaScanner(List<String> lines) {
        this.lines = lines;
        scanNextToken();
    }

    public LuaScanner(String fileName) {
        File file = new File(fileName);
        ArrayList<String> program = new ArrayList<>();
        if(file.exists()) {
            try {
                Scanner scanner = new Scanner(file);
                while(scanner.hasNext()) {
                    program.add(scanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Could not find file: " + fileName);
            System.exit(1);
        }
        lines = program;
        scanNextToken();
    }

    private boolean shouldSkipWhiteSpace() {
        if(currentLine >= lines.size() || currentIndex >= lines.get(currentLine).length()) {
            return false;
        }
        Matcher m = Pattern.compile(WHITESPACEREGEX).matcher(String.valueOf(lines.get(currentLine).charAt(currentIndex)));
        return m.find();
    }

    private void skipWhiteSpace() {
        if(shouldSkipWhiteSpace()) {
            Matcher m = Pattern.compile(WHITESPACEREGEX).matcher(lines.get(currentLine));
            if (m.find(currentIndex)) {
                currentIndex = m.end();
            }
        }
    }

    private void nextMatch() {
        skipWhiteSpace();
        Matcher m = Pattern.compile(TOKENREGEX).matcher(lines.get(currentLine));
        if(m.find(currentIndex)){
            String match = m.group();
            TokenType tokenType = TokenType.getTokenFromString(match);
            if(tokenType != TokenType.UNKNOWN) {
                nextToken = new Token(tokenType, match, currentLine, currentIndex);
                currentIndex = m.end();
            } else {
                System.err.println("Invalid token found at line {" + currentLine + "} at {" + currentIndex + "}.");
            }
        } else {
            System.err.println("Could not find a token at line {" + currentLine +"}.");
        }
    }

    private void scanNextToken() {
        nextToken = null;
        if(currentLine < lines.size()-1) {
            if(lines.get(currentLine).length() <= currentIndex) {
                currentLine++;
                currentIndex = 0;
            }
            nextMatch();
        }
    }

    public Token getLookAhead() {
        return nextToken;
    }

    public boolean hasNextToken() {
        return nextToken != null;
    }

    public Token getNextToken() {
        Token t = nextToken;
        scanNextToken();
        return t;
    }

    @Deprecated
    public static List<Pair<TokenType, String>> scan(List<String> lines) {
        for (String line : lines) {
            System.out.println((char) 27 + "[39m" + "Analyzing line: " + (char) 27 + "[34m" + line.replaceAll("^\\s*", ""));
            String[] tokens = line.split(WHITESPACEREGEX);
            for (int i = 0; i < tokens.length; i++) {
                if (!tokens[i].isEmpty()) {
                    List<Pair<TokenType, String>> possibleTokens = TokenType.getTokens(tokens[i]);
                    for (Pair<TokenType, String> pair : possibleTokens) {
                        TokenType t = pair.getLeft();
                        System.out.println((char) 27 + "[39m" + "found token: " + (char) 27 + "[36m" + t + (char) 27 + "[39m" + " from: " + (char) 27 + "[33m" + pair.getRight());
                        tokenList.add(new Pair<>(t, tokens[i]));
                    }
                }
            }
            System.out.println();
        }
        return tokenList;
    }
}
